package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonzalo.cafeteriabackend.application.port.in.ProductUseCase;
import com.gonzalo.cafeteriabackend.domain.model.BinaryData;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.ProductDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productUseCase.getAllProducts().stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @PostMapping("/{id}/reorder")
    public ResponseEntity<Void> reorder(@PathVariable Long id, @RequestParam String direction) {
        productUseCase.reorder(id, direction);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public List<ProductDto> getActiveProducts() {
        return productUseCase.getActiveProducts().stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Product product = productUseCase.getProductById(id);
        return product != null
                ? ResponseEntity.ok(ProductMapper.toDto(product))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        BinaryData image = productUseCase.getBinaryDataByProductId(id);
        if (image != null && image.getData() != null && image.getData().length > 0) {
            String contentType = (image.getContentType() != null) ? image.getContentType() : "image/png";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(image.getData());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> saveProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto productDto = objectMapper.readValue(productJson, ProductDto.class);
            Product product = ProductMapper.toEntity(productDto);

            BinaryData imageData = null;
            if (image != null && !image.isEmpty()) {
                BinaryData bin = new BinaryData();
                bin.setContentType(image.getContentType());
                bin.setData(image.getBytes());
                imageData = bin;
            }

            return ResponseEntity.ok(ProductMapper.toDto(productUseCase.saveProduct(product, imageData)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id) {
        productUseCase.toggleProductStatus(id);
        return ResponseEntity.ok().build();
    }
}
