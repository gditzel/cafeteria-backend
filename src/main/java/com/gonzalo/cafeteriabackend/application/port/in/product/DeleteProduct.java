package com.gonzalo.cafeteriabackend.application.port.in.product;

public interface DeleteProduct {
    DeleteProductResponse execute(DeleteProductRequest request);

    record DeleteProductRequest(Long id) {}

    record DeleteProductResponse(boolean deleted) {}
}
