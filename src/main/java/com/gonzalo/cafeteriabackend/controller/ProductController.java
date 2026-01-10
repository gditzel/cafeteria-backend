package com.gonzalo.cafeteriabackend.controller;

import com.gonzalo.cafeteriabackend.model.Product;
import com.gonzalo.cafeteriabackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200") // Soluciona el error de CORS
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") Product product,
            @RequestPart("image") MultipartFile image) {
        try {
            Product savedProduct = productService.saveProduct(product, image);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}