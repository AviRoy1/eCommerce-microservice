package com.ecom.product.mapper;


import com.ecom.product.dto.ProductRequest;
import com.ecom.product.dto.ProductResponse;
import com.ecom.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product convertToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setImageUrl(productRequest.getImageUrl());
        return product;
    }

    public ProductResponse convertToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setCategory(product.getCategory());
        productResponse.setPrice(product.getPrice());
        productResponse.setStock(product.getStock());
        productResponse.setImageUrl(product.getImageUrl());

        return productResponse;
    }

    public Product updatedProductDetails(Product product, ProductRequest productRequest) {

        if (productRequest.getName() != null)
            product.setName(productRequest.getName());

        if (productRequest.getDescription() != null)
            product.setDescription(productRequest.getDescription());

        if (productRequest.getCategory() != null)
            product.setCategory(productRequest.getCategory());

        if (productRequest.getPrice() != null)
            product.setPrice(productRequest.getPrice());

        if (productRequest.getStock() != null)
            product.setStock(productRequest.getStock());

        if (productRequest.getImageUrl() != null)
            product.setImageUrl(productRequest.getImageUrl());

        return product;
    }



}
