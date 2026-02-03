package com.gonzalo.cafeteriabackend.application.usecase.product;

import com.gonzalo.cafeteriabackend.application.port.in.product.DeleteProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetActiveProducts;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetAllProducts;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetProductById;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetProductImageData;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetProductsForMenu;
import com.gonzalo.cafeteriabackend.application.port.in.product.ReorderProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.SaveProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.ToggleProductStatus;
import com.gonzalo.cafeteriabackend.application.port.out.ProductRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.BinaryData;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductUseCases implements
        GetAllProducts,
        GetActiveProducts,
        GetProductById,
        GetProductImageData,
        SaveProduct,
        ToggleProductStatus,
        DeleteProduct,
        ReorderProduct,
        GetProductsForMenu {
    private final ProductRepositoryPort productRepository;

    public ProductUseCases(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllProductsResponse execute(GetAllProductsRequest request) {
        return new GetAllProductsResponse(productRepository.findAllOrderBySortOrderAsc());
    }

    @Override
    @Transactional(readOnly = true)
    public GetActiveProductsResponse execute(GetActiveProductsRequest request) {
        return new GetActiveProductsResponse(productRepository.findActive());
    }

    @Override
    @Transactional(readOnly = true)
    public GetProductByIdResponse execute(GetProductByIdRequest request) {
        return new GetProductByIdResponse(productRepository.findById(request.id()).orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
    public GetProductImageDataResponse execute(GetProductImageDataRequest request) {
        BinaryData imageData = productRepository.findById(request.productId())
                .map(Product::getBinaryData)
                .orElse(null);
        return new GetProductImageDataResponse(imageData);
    }

    @Override
    @Transactional
    public SaveProductResponse execute(SaveProductRequest request) {
        Product product = request.product();
        BinaryData imageData = request.imageData();

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

        return new SaveProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ToggleProductStatusResponse execute(ToggleProductStatusRequest request) {
        boolean toggled = productRepository.findById(request.id()).map(product -> {
            product.setIsActive(!product.getIsActive());
            productRepository.save(product);
            return true;
        }).orElse(false);
        return new ToggleProductStatusResponse(toggled);
    }

    @Override
    @Transactional
    public DeleteProductResponse execute(DeleteProductRequest request) {
        productRepository.deleteById(request.id());
        return new DeleteProductResponse(true);
    }

    @Override
    @Transactional
    public ReorderProductResponse execute(ReorderProductRequest request) {
        List<Product> products = productRepository.findAllOrderBySortOrderAsc();
        Product current = products.stream()
                .filter(p -> p.getId().equals(request.id()))
                .findFirst()
                .orElse(null);

        if (current == null) {
            return new ReorderProductResponse(false);
        }

        int index = products.indexOf(current);
        Product neighbor = null;

        if ("up".equals(request.direction()) && index > 0) {
            neighbor = products.get(index - 1);
        } else if ("down".equals(request.direction()) && index < products.size() - 1) {
            neighbor = products.get(index + 1);
        }

        if (neighbor != null) {
            Integer tempOrder = current.getSortOrder();
            current.setSortOrder(neighbor.getSortOrder());
            neighbor.setSortOrder(tempOrder);

            productRepository.save(current);
            productRepository.save(neighbor);
            productRepository.flush();
            return new ReorderProductResponse(true);
        }

        return new ReorderProductResponse(false);
    }

    /**
     * Devuelve los productos para el menú público.
     * Filtra solo por isActive. Mantiene los de stock 0 para mostrarlos como "Agotado".
     */
    @Override
    @Transactional(readOnly = true)
    public GetProductsForMenuResponse execute(GetProductsForMenuRequest request) {
        return new GetProductsForMenuResponse(productRepository.findActiveOrderBySortOrderAsc());
    }
}
