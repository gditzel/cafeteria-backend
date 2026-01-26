package com.gonzalo.cafeteriabackend.application.port.in.product;

import com.gonzalo.cafeteriabackend.domain.model.Product;

public interface GetProductById {
    GetProductByIdResponse execute(GetProductByIdRequest request);

    record GetProductByIdRequest(Long id) {}

    record GetProductByIdResponse(Product product) {}
}
