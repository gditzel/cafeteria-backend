package com.gonzalo.cafeteriabackend.application.port.in.product;

import com.gonzalo.cafeteriabackend.domain.model.Product;
import java.util.List;

public interface GetActiveProducts {
    GetActiveProductsResponse execute(GetActiveProductsRequest request);

    record GetActiveProductsRequest() {}

    record GetActiveProductsResponse(List<Product> products) {}
}
