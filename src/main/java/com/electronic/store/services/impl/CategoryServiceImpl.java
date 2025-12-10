package com.electronic.store.services.impl;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.payloads.PageableResponse;
import com.electronic.store.entities.Category;
import com.electronic.store.exceptions.ResourceNoteFoundException;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.profile.image.path}")
    private String categoryImagePath;

    //create
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        //create categoryId randomly
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);

        return mapper.map(savedCategory, CategoryDto.class);
    }

    //update
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        //get category by given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNoteFoundException("Category not found by given id"));

        //updated category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);

        return mapper.map(updatedCategory, CategoryDto.class);
    }

    //delete
    @Override
    public void deleteCategory(String categoryId) {
        //get category by given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNoteFoundException("Category not found by given id"));

        //delete category cover image
        String fullPath = categoryImagePath + category.getCoverImage();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (IOException e) {
            e.printStackTrace();
        }

        //delete category
        categoryRepository.delete(category);

    }

    //get all
    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> all = categoryRepository.findAll(pageable);

        PageableResponse<CategoryDto> response = Helper.getPageableResponse(all, CategoryDto.class);
        return response;
    }

    //get single
    @Override
    public CategoryDto getSingleCategory(String categoryId) {
        //get category by given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNoteFoundException("Category not found by given id"));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> categoryList = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> dtoList = categoryList.stream().map(category -> new ModelMapper().map(category,CategoryDto.class)).collect(Collectors.toList());
        return dtoList;
    }
}
