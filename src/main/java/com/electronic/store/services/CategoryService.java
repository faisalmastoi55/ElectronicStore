package com.electronic.store.services;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.payloads.PageableResponse;

import java.util.List;

public interface CategoryService {
    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //get All Categories
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single category
    CategoryDto getSingleCategory(String categoryId);

    //search category if you want
    List<CategoryDto> searchCategory(String keyword);
}
