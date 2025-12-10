package com.electronic.store.services;

import com.electronic.store.payloads.PageableResponse;
import com.electronic.store.dtos.ProductDto;

public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto, String productId);

    //delete
    void delete(String productId);

    //get single
    ProductDto getSingleProduct(String productId);

    //get all
    PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get all : live
    PageableResponse<ProductDto> getAllLiveProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    //search product
    PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);

    //create product wih category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    //update category of product
    ProductDto updateCategory(String productId, String categoryId);

    PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
    //other methods
}
