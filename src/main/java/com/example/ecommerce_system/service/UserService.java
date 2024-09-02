package com.example.ecommerce_system.service;

import com.example.ecommerce_system.dto.request.CartAddRequest;
import com.example.ecommerce_system.dto.request.LoginUserRequest;
import com.example.ecommerce_system.dto.request.BuyRequest;
import com.example.ecommerce_system.dto.request.RegisterUserRequest;
import com.example.ecommerce_system.dto.response.CartAddResponse;
import com.example.ecommerce_system.dto.response.LoginUserResponse;
import com.example.ecommerce_system.dto.response.BuyResponse;
import com.example.ecommerce_system.dto.response.OrderResponse;
import com.example.ecommerce_system.entity.*;
import com.example.ecommerce_system.enums.ResponseStatus;
import com.example.ecommerce_system.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private BuyRepo orderRepo;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public Optional<User> findByEmailOrUsername(String email, String username) {
        return userRepo.findByEmailOrUsername(email,username);
    }

    public User register(RegisterUserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setUserType(userRequest.getUserType());
        return userRepo.save(user);
    }

    public LoginUserResponse verify(LoginUserRequest userRequest) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        LoginUserResponse response = new LoginUserResponse();

        try{
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userRequest.getUsername());
                response.setMessage("Login successful! Token: " + token);
                response.setResponseStatus(ResponseStatus.SUCCESS);
            }else{
                response.setMessage("Login failed. Invalid credentials.");
                response.setResponseStatus(ResponseStatus.FAILED);
            }
        }
            catch(Exception e){
                response.setMessage("Unexpected error!!");
                response.setResponseStatus(ResponseStatus.FAILED);
            }
        return response;
    }

//    public CartAddResponse addToCart(String username, CartAddRequest cartAddRequest) {
//        CartAddResponse response = new CartAddResponse();
//
//        try{
//
//            Product product = productRepo.findById(cartAddRequest.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//
//            if (product.getStock() <= 0) {
//                response.setMessage("Product is not available");
//                return response;
//            }
//
//            User user = userRepo.findByUsername(username);
//
//            product.setStock(product.getStock() - 1);
//            productRepo.save(product);
//
//            Optional<Cart> cart = cartRepo.findById(cartAddRequest.getCartId());
//            Cart cartHistory;
//
//            if(cart.isPresent()){
//                cartHistory = cart.get();
//            }
//            else {
//                cartHistory = new Cart();
//            }
//
//            cartHistory.setUser(user);
//            List<Product> products = cartHistory.getProducts();
//
//            if(products == null){
//                products = new ArrayList<>();
//            }
//            products.add(product);
//
//            cartHistory.setProducts(products);
//
//            response.setId(cartAddRequest.getCartId());
//            response.setResponseStatus(ResponseStatus.SUCCESS);
//            response.setMessage("Product added to the cart successfully");
//
//            cartRepo.save(cartHistory);
//
//        }catch (Exception e){
//            e.printStackTrace();
//            response.setMessage(e.getMessage());
//        }
//
//        return response;
//
//    }

    public CartAddResponse addToCart(String username, CartAddRequest cartAddRequest) {
        CartAddResponse response = new CartAddResponse();

        try {
            // Find the product by ID
            Product product = productRepo.findById(cartAddRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Check if the product has enough stock
            if (product.getStock() <= 0) {
                response.setMessage("Product is not available");
                response.setResponseStatus(ResponseStatus.FAILED);
                return response;
            }

            // Find the user by username
            User user = userRepo.findByUsername(username);

            // Check if the user already has a cart
            Cart cart = cartRepo.findByUser(user);

            if (cart == null) {
                // Create a new cart if the user doesn't already have one
                cart = new Cart();
                cart.setUser(user);
                cart = cartRepo.save(cart); // Save the cart to generate an ID
            }

            // Find or create CartItem
            CartItem existingCartItem = cartItemRepo.findByCartAndProduct(cart, product);

            if (existingCartItem != null) {
                // Update quantity if the item already exists in the cart
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                cartItemRepo.save(existingCartItem);
            } else {
                // Create a new CartItem
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(cart);
                newCartItem.setProduct(product);
                newCartItem.setQuantity(1);
                cartItemRepo.save(newCartItem);
            }

            // Decrease product stock and save  // this logic is not needed while adding product to the cart
//            product.setStock(product.getStock() - 1);
//            productRepo.save(product);

            response.setId(cart.getId());
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Product added to the cart successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResponseStatus(ResponseStatus.FAILED);
        }

        return response;
    }



    public List<Cart> getCart() {
        return cartRepo.findAll();
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public BuyResponse buyProduct(String username, BuyRequest orderRequest) {
        BuyResponse response = new BuyResponse();
        Optional<Cart> optionalCart = cartRepo.findById(orderRequest.getCartId());

        if (!optionalCart.isPresent()) {
            response.setResponseStatus(ResponseStatus.FAILED);
            response.setMessage("Cart not found.");
            return response;
        }

        Cart cart = optionalCart.get();

        if (cart.getUser() == null || !cart.getUser().getUsername().equals(username)) {
            response.setResponseStatus(ResponseStatus.FAILED);
            response.setMessage("You are not authorized to make this purchase.");
            return response;
        }

        int totalAmount = 0;
        List<Product> purchasedProducts = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                response.setResponseStatus(ResponseStatus.FAILED);
                response.setMessage("Insufficient stock for product: " + product.getProductName());
                return response;
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepo.save(product);

            totalAmount += product.getPrice() * cartItem.getQuantity();
            purchasedProducts.add(product);
        }

        // Create and save the order
        Buy order = new Buy();
        order.setCart(cart);
        order.setUser(cart.getUser());
        order.setAmount(totalAmount);
        orderRepo.save(order);

        // Clear the cart after purchase
        cart.getCartItems().clear();
        cartRepo.save(cart);

        // Populate the response
        response.setProducts(purchasedProducts);
        response.setTotalAmount(totalAmount);
        response.setResponseStatus(ResponseStatus.SUCCESS);
        response.setMessage("Purchase successful!");

        return response;
    }


    public List<OrderResponse> getOrdersForUser(String username) {
        List<Buy> orders = orderRepo.findByUserUsername(username);

        // Convert entities to DTOs
        return orders.stream()
                .map(order -> {
                    OrderResponse response = new OrderResponse();
                    response.setOrderId(order.getId());
                    response.setAmount(order.getAmount());
                    response.setProducts(order.getCart().getCartItems().stream()
                            .map(CartItem::getProduct)
                            .collect(Collectors.toList()));
                    return response;
                })
                .collect(Collectors.toList());
    }
}
