package com.gonzalo.cafeteriabackend.controller;

import com.gonzalo.cafeteriabackend.model.Product;
import com.gonzalo.cafeteriabackend.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping
    public List<Product> getAll() {
        return productRepo.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productRepo.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product updated) {
        Product product = productRepo.findById(id).orElseThrow();
        product.setName(updated.getName());
        product.setPrice(updated.getPrice());
        product.setStock(updated.getStock());
        product.setActive(updated.isActive());
        product.setDescription(updated.getDescription());
        product.setImageUrl(updated.getImageUrl());
        return productRepo.save(product);
    }
}
