package com.bexos.backend.controllers;

import com.bexos.backend.entitites.Product;
import com.bexos.backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{productId}")
    public ResponseEntity<Product> PurchaseProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(orderService.purchaseProduct(productId));
    }
}
