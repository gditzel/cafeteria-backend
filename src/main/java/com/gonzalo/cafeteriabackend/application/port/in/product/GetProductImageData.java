package com.gonzalo.cafeteriabackend.application.port.in.product;

import com.gonzalo.cafeteriabackend.domain.model.BinaryData;

public interface GetProductImageData {
    GetProductImageDataResponse execute(GetProductImageDataRequest request);

    record GetProductImageDataRequest(Long productId) {}

    record GetProductImageDataResponse(BinaryData imageData) {}
}
