package com.gonzalo.cafeteriabackend.application.port.in.product;

import com.gonzalo.cafeteriabackend.domain.model.Product;
import java.util.List;

public interface GetProductsForMenu {
    GetProductsForMenuResponse execute(GetProductsForMenuRequest request);

    record GetProductsForMenuRequest() {}

    record GetProductsForMenuResponse(List<Product> products) {}
}
