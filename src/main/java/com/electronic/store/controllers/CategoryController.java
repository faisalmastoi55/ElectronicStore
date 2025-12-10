package com.electronic.store.controllers;

import com.electronic.store.dtos.*;
import com.electronic.store.payloads.ApiResponseMessage;
import com.electronic.store.payloads.ImageResponse;
import com.electronic.store.payloads.PageableResponse;
import com.electronic.store.services.CategoryService;
import com.electronic.store.services.FileService;
import com.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String categoryImageUploadPath;

    //create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    //update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid
            @PathVariable String categoryId,
            @RequestBody CategoryDto categoryDto
    ) {
        CategoryDto updateCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Category is deleted successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<CategoryDto> all = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId) {
        CategoryDto singleCategory = categoryService.getSingleCategory(categoryId);
        return ResponseEntity.ok(singleCategory);
    }

    //search category
    @GetMapping("/search/{categoryId}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.searchCategory(categoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @PathVariable String categoryId,
            @RequestParam("categoryImage")MultipartFile image
            ) throws IOException {

        String imageName = fileService.uploadFile(image, categoryImageUploadPath);

        CategoryDto category = categoryService.getSingleCategory(categoryId);
        category.setCoverImage(imageName);
        CategoryDto updateCategory = categoryService.updateCategory(category, categoryId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("Category image is successfully uploaded !!")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

    }

    @GetMapping("/image/{categoryId}")
    public void serveCategoryImage(
            @PathVariable String categoryId,
            HttpServletResponse response
    ) throws IOException {
        CategoryDto category = categoryService.getSingleCategory(categoryId);
        InputStream resource = fileService.getResource(categoryImageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

    //create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable String categoryId,
            @RequestBody ProductDto productDto
    ) {
        ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }

    //update category of product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(
            @PathVariable String categoryId,
            @PathVariable String productId
    ) {
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    //get product of categories
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allOfCategory = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allOfCategory, HttpStatus.OK);
    }
}
