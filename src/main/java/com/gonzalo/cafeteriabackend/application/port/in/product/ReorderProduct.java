package com.gonzalo.cafeteriabackend.application.port.in.product;

public interface ReorderProduct {
    ReorderProductResponse execute(ReorderProductRequest request);

    record ReorderProductRequest(Long id, String direction) {}

    record ReorderProductResponse(boolean reordered) {}
}
