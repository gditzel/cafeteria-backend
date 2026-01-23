package com.gonzalo.cafeteriabackend.service;

import com.gonzalo.cafeteriabackend.model.*;
import com.gonzalo.cafeteriabackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
    }

    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public BinaryData getBinaryDataByProductId(Long productId) {
        return productRepository.findById(productId)
                .map(Product::getBinaryData)
                .orElse(null);
    }

    @Transactional
    public Product saveProduct(Product product, MultipartFile image) throws IOException {
        if (product.getId() != null) {
            Product existing = productRepository.findById(product.getId()).orElse(null);
            if (existing != null) {
                if (image == null || image.isEmpty()) {
                    product.setBinaryData(existing.getBinaryData());
                }
                product.setSortOrder(existing.getSortOrder());
            }
        } else {
            // Si es nuevo, lo ponemos al final
            Integer maxOrder = productRepository.findAll().stream()
                    .mapToInt(p -> p.getSortOrder() != null ? p.getSortOrder() : 0)
                    .max().orElse(0);
            product.setSortOrder(maxOrder + 1);
        }

        if (image != null && !image.isEmpty()) {
            BinaryData bin = new BinaryData();
            bin.setContentType(image.getContentType());
            bin.setData(image.getBytes());
            product.setBinaryData(bin);
        }

        if (product.getTranslations() != null) {
            product.getTranslations().forEach(t -> t.setProduct(product));
        }
        return productRepository.save(product);
    }

    @Transactional
    public void toggleProductStatus(Long id) {
        productRepository.findById(id).ifPresent(p -> {
            p.setIsActive(!p.getIsActive());
            productRepository.save(p);
        });
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void reorder(Long id, String direction) {
        // Obtenemos todos los productos ordenados para identificar correctamente al vecino
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
        Product current = products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);

        if (current == null) return;

        int index = products.indexOf(current);
        Product neighbor = null;

        if ("up".equals(direction) && index > 0) {
            neighbor = products.get(index - 1);
        } else if ("down".equals(direction) && index < products.size() - 1) {
            neighbor = products.get(index + 1);
        }

        if (neighbor != null) {
            // Intercambiamos los valores de sortOrder
            Integer tempOrder = current.getSortOrder();
            current.setSortOrder(neighbor.getSortOrder());
            neighbor.setSortOrder(tempOrder);

            // Persistimos ambos cambios
            productRepository.save(current);
            productRepository.save(neighbor);
        }
    }
}