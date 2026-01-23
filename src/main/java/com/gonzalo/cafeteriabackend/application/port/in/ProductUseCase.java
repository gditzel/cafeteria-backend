package com.gonzalo.cafeteriabackend.application.port.in;

import com.gonzalo.cafeteriabackend.domain.model.BinaryData;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import java.util.List;

public interface ProductUseCase {
    List<Product> getAllProducts();

    List<Product> getActiveProducts();

    Product getProductById(Long id);

    BinaryData getBinaryDataByProductId(Long productId);

    Product saveProduct(Product product, BinaryData imageData);

    void toggleProductStatus(Long id);

    void deleteProduct(Long id);

    void reorder(Long id, String direction);

    List<Product> getProductsForMenu();
}
