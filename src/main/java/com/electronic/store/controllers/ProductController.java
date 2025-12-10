package com.electronic.store.controllers;

import com.electronic.store.dtos.*;
import com.electronic.store.payloads.ApiResponseMessage;
import com.electronic.store.payloads.ImageResponse;
import com.electronic.store.payloads.PageableResponse;
import com.electronic.store.services.FileService;
import com.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
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

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    //create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @RequestBody ProductDto productDto,
            @PathVariable String productId) {

        ProductDto updatedProduct = productService.update(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        productService.delete(productId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Product is deleted successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get Single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId) {
        ProductDto singleProduct = productService.getSingleProduct(productId);
        return new ResponseEntity<>(singleProduct, HttpStatus.OK);
    }

    //get All
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProduct = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }

    //get All Live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProduct = productService.getAllLiveProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }

    //search all
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchAll(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProduct = productService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }

    //upload image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> updateProductImage(
            @PathVariable String productId,
            @RequestParam("productImages")MultipartFile file
            ) throws IOException {

        String fileName = fileService.uploadFile(file, imagePath);
        ProductDto product = productService.getSingleProduct(productId);
        product.setProductImageName(fileName);
        ProductDto updatedProduct = productService.update(product, productId);

        ImageResponse response = ImageResponse.builder()
                .imageName(fileName)
                .message("Product image is successfully uploaded!!")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //serve image
    @GetMapping("/image/{productId}")
    public void serveProductImage(
            @PathVariable String productId,
            HttpServletResponse response
    ) throws IOException {
        ProductDto product = productService.getSingleProduct(productId);
        InputStream resource = fileService.getResource(imagePath, product.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }
}
