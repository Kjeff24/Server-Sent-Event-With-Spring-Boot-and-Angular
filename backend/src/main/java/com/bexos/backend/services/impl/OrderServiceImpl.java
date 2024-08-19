package com.bexos.backend.services.impl;

import com.bexos.backend.dto.NotificationResponse;
import com.bexos.backend.entitites.Notification;
import com.bexos.backend.entitites.Order;
import com.bexos.backend.entitites.Product;
import com.bexos.backend.entitites.User;
import com.bexos.backend.handler.NotFoundException;
import com.bexos.backend.repositories.NotificationRepository;
import com.bexos.backend.repositories.OrderRepository;
import com.bexos.backend.repositories.ProductRepository;
import com.bexos.backend.repositories.UserRepository;
import com.bexos.backend.services.NotificationService;
import com.bexos.backend.services.OrderService;
import com.bexos.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    public Product purchaseProduct(Integer productId) {
        User user = userService.getAuthenticatedUser();
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));

        orderRepository.save(Order.builder().user(user).product(product).build());
        Notification notification = notificationRepository.save(Notification.builder()
                .event("notification")
                .title("Order was successful")
                .message("Your order has been placed, you would be notify during transit")
                .user(user)
                .build());
        NotificationResponse notificationResponse = new NotificationResponse(notification);
        notificationService.notifyUser(user.getId(), notificationResponse);


        return product;
    }


}
