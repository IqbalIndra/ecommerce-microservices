package com.learn.ws.products.service;

import com.learn.ws.products.rest.CreateProductRestModel;

public interface ProductService {
    String createProduct(CreateProductRestModel product);
}
