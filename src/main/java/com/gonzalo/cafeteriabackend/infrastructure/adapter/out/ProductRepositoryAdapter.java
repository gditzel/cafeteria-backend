package com.gonzalo.cafeteriabackend.infrastructure.adapter.out;

import com.gonzalo.cafeteriabackend.application.port.out.ProductRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.ProductRepository;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper.ProductEntityMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private final ProductRepository productRepository;

    public ProductRepositoryAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAllOrderBySortOrderAsc() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder")).stream()
                .map(ProductEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findActive() {
        return productRepository.findByIsActiveTrue().stream()
                .map(ProductEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findActiveOrderBySortOrderAsc() {
        return productRepository.findByIsActiveTrueOrderBySortOrderAsc().stream()
                .map(ProductEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(ProductEntityMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(ProductEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Product save(Product product) {
        return ProductEntityMapper.toDomain(
                productRepository.save(ProductEntityMapper.toEntity(product)));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void flush() {
        productRepository.flush();
    }
}
