package com.ecom.product.service;

import com.ecom.product.dto.ProductRequest;
import com.ecom.product.dto.ProductResponse;
import com.ecom.product.entity.Product;
import com.ecom.product.mapper.ProductMapper;
import com.ecom.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public @Nullable ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.convertToProduct(productRequest);
        product = productRepository.save(product);
        return productMapper.convertToProductResponse(product);
    }


    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElse(null);
        if(null == product) {
            return null;
        }
        product = productMapper.updatedProductDetails(product, productRequest);
        productRepository.save(product);

        return productMapper.convertToProductResponse(product);
    }

    public ProductResponse findProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::convertToProductResponse)
                .orElseThrow(() -> new RuntimeException("Product Not Found !!"));
    }

}
