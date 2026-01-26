package com.gonzalo.cafeteriabackend.application.port.in.product;

import com.gonzalo.cafeteriabackend.domain.model.Product;
import java.util.List;

public interface GetAllProducts {
    GetAllProductsResponse execute(GetAllProductsRequest request);

    record GetAllProductsRequest() {}

    record GetAllProductsResponse(List<Product> products) {}
}
