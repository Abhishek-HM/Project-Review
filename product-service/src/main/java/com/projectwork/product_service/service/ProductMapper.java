package com.projectwork.product_service.service;

import com.projectwork.product_service.dto.ProductResponse;
import com.projectwork.product_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper Instance = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "id", target = "id", resultType = String.class)
    ProductResponse productToProductResponse(Product product);
}
