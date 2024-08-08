package com.bexos.backend.services;

import com.bexos.backend.entitites.Product;

public interface OrderService {
    Product purchaseProduct(Integer userId, Integer productId);
}
