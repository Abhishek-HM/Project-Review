package com.projectwork.product_service;

import com.projectwork.product_service.dto.ProductRequest;
import com.projectwork.product_service.dto.ProductResponse;
import com.projectwork.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceApplicationTests.class);
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/product");
    }

    @Test
    public void shouldAddProduct() {
        ProductRequest productRequest = new ProductRequest("iPhone", "iPhone-13", BigDecimal.valueOf(100));
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, productRequest, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product Successfully Created", response.getBody());
    }

    @Test
    public void shouldGetAllProduct() {
        ProductRequest productRequest = new ProductRequest("iPhone", "iPhone-13", BigDecimal.valueOf(100));
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, productRequest, String.class);

        ResponseEntity<ProductResponse[]> responseEntity = restTemplate.getForEntity(baseUrl, ProductResponse[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ProductResponse> responseList = Arrays.asList(responseEntity.getBody());
        int size = responseList.size()-1;
        assertEquals("iPhone",responseList.get(size).getName());
        assertEquals("iPhone-13",responseList.get(size).getDescription());
        assertEquals(BigDecimal.valueOf(100),responseList.get(size).getPrice());
    }

    @Test
    public void shouldReturnNotFoundWhenThereIsNoProducts() {
        productRepository.deleteAll();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(baseUrl, String.class);
        }
        catch (HttpClientErrorException.NotFound ex) {
            assertEquals(ex.getStatusCode(),HttpStatus.NOT_FOUND);
            assertEquals(ex.getResponseBodyAsString(),"No Products Found In Product Service.");
        }
    }
}