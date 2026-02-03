package com.gonzalo.cafeteriabackend.application.port.in.product;

import com.gonzalo.cafeteriabackend.domain.model.BinaryData;
import com.gonzalo.cafeteriabackend.domain.model.Product;

public interface SaveProduct {
    SaveProductResponse execute(SaveProductRequest request);

    record SaveProductRequest(Product product, BinaryData imageData) {}

    record SaveProductResponse(Product product) {}
}
