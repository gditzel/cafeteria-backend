package com.gonzalo.cafeteriabackend.application.service;

import com.gonzalo.cafeteriabackend.application.port.in.ProductUseCase;
import com.gonzalo.cafeteriabackend.application.port.out.ProductRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.BinaryData;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductService implements ProductUseCase {
    private final ProductRepositoryPort productRepository;

    public ProductService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAllOrderBySortOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findActive();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public BinaryData getBinaryDataByProductId(Long productId) {
        return productRepository.findById(productId)
                .map(Product::getBinaryData)
                .orElse(null);
    }

    @Override
    @Transactional
    public Product saveProduct(Product product, BinaryData imageData) {
        if (product.getId() != null) {
            Product existing = productRepository.findById(product.getId()).orElse(null);
            if (existing != null) {
                boolean hasNewImage = imageData != null
                        && imageData.getData() != null
                        && imageData.getData().length > 0;
                if (!hasNewImage) {
                    product.setBinaryData(existing.getBinaryData());
                }
                product.setSortOrder(existing.getSortOrder());
            }
        } else {
            Integer maxOrder = productRepository.findAll().stream()
                    .mapToInt(p -> p.getSortOrder() != null ? p.getSortOrder() : 0)
                    .max()
                    .orElse(0);
            product.setSortOrder(maxOrder + 1);
        }

        if (imageData != null && imageData.getData() != null && imageData.getData().length > 0) {
            product.setBinaryData(imageData);
        }

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void toggleProductStatus(Long id) {
        productRepository.findById(id).ifPresent(p -> {
            p.setIsActive(!p.getIsActive());
            productRepository.save(p);
        });
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void reorder(Long id, String direction) {
        List<Product> products = productRepository.findAllOrderBySortOrderAsc();
        Product current = products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);

        if (current == null) {
            return;
        }

        int index = products.indexOf(current);
        Product neighbor = null;

        if ("up".equals(direction) && index > 0) {
            neighbor = products.get(index - 1);
        } else if ("down".equals(direction) && index < products.size() - 1) {
            neighbor = products.get(index + 1);
        }

        if (neighbor != null) {
            Integer tempOrder = current.getSortOrder();
            current.setSortOrder(neighbor.getSortOrder());
            neighbor.setSortOrder(tempOrder);

            productRepository.save(current);
            productRepository.save(neighbor);
            productRepository.flush();
        }
    }

    /**
     * Devuelve los productos para el menú público.
     * Filtra solo por isActive. Mantiene los de stock 0 para mostrarlos como "Agotado".
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsForMenu() {
        return productRepository.findActiveOrderBySortOrderAsc();
    }
}
