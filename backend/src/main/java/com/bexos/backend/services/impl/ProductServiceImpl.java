package com.bexos.backend.services.impl;

import com.bexos.backend.entitites.Product;
import com.bexos.backend.repositories.OrderRepository;
import com.bexos.backend.repositories.ProductRepository;
import com.bexos.backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public Product purchaseProduct(Integer userId, Integer productId) {

        return null;
    }
}
