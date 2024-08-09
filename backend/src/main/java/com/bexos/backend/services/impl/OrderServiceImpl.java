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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public Product purchaseProduct(Integer userId, Integer productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));

        orderRepository.save(Order.builder().user(user).product(product).build());
        notificationRepository.save(Notification.builder()
                .event("notification")
                .title("Order was successful")
                .message("Your order has been placed, you would be notify during transit")
                .user(user)
                .build());

        List<NotificationResponse> notifications = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user).stream()
                .map(NotificationResponse::new)
                .toList();;


//        NotificationResponse notificationResponse = new NotificationResponse(notification);

        notificationService.notifyUser(String.valueOf(user.getId()), notifications);


        return product;
    }


}
