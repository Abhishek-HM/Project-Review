package com.projectwork.product_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.projectwork.product_service.dto.ProductRequest;
import com.projectwork.product_service.dto.ProductResponse;
import com.projectwork.product_service.model.Product;
import com.projectwork.product_service.repository.ProductRepository;
import com.projectwork.product_service.service.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    public void setProduct() {
        product = Product.builder()
                .id("1")
                .name("iPhone")
                .description("iPhone-13")
                .price(BigDecimal.valueOf(120))
                .build();

       productRequest = ProductRequest.builder()
                .name("iPhone")
                .description("iPhone-13")
                .price(BigDecimal.valueOf(120))
                .build();

        productResponse = ProductResponse.builder()
                .id("1")
                .name("iPhone")
                .description("iPhone-13")
                .price(BigDecimal.valueOf(120))
                .build();
    }

    @Test
    public void creatingProduct() {

        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.createProduct(productRequest);
        verify(productRepository).save(any(Product.class));

        assertNotNull(product);
        assertEquals(productRequest.getName(),product.getName());
        assertEquals(productRequest.getDescription(), product.getDescription());
        assertEquals(productRequest.getPrice(), product.getPrice());
    }

    @Test
    public void getAllProduct() {

        when(productRepository.findAll()).thenReturn(List.of(product));

        when(productMapper.productToProductResponse(product)).thenReturn(productResponse);

        List<ProductResponse> productResponses = productService.getAllProducts();

        verify(productRepository).findAll();

        verify(productMapper).productToProductResponse(product);

        assertNotNull(productResponses);
        assertEquals(1, productResponses.size());
        assertEquals(productResponse, productResponses.get(0));
    }
}