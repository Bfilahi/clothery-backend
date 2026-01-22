package com.filahi.springboot.clothery.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.filahi.springboot.clothery.dto.ProductResponseDTO;
import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.entity.Image;
import com.filahi.springboot.clothery.entity.Product;
import com.filahi.springboot.clothery.entity.Size;
import com.filahi.springboot.clothery.repository.CategoryRepository;
import com.filahi.springboot.clothery.repository.ImageRepository;
import com.filahi.springboot.clothery.repository.ProductRepository;
import com.filahi.springboot.clothery.repository.SizeRepository;
import com.filahi.springboot.clothery.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ImageRepository imageRepository;
    private final Cloudinary cloudinary;


    @Autowired
    public ProductServiceImpl(CategoryRepository CategoryRepository,
                              ProductRepository ProductRepository,
                              SizeRepository SizeRepository, ImageRepository imageRepository, Cloudinary cloudinary){

        this.categoryRepository = CategoryRepository;
        this.productRepository = ProductRepository;
        this.sizeRepository = SizeRepository;
        this.imageRepository = imageRepository;
        this.cloudinary = cloudinary;
    }


    @Override
    @Transactional
    public ProductResponseDTO addNewProduct(String productName,
                                 String description,
                                 BigDecimal price,
                                 int unitsInStock,
                                 long categoryId,
                                 List<Long> sizeIds,
                                 MultipartFile[] images) throws IOException {

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Product product = new Product();
        product.setId(0);
        product.setProductName(productName);
        product.setDescription(description);
        product.setUnitPrice(price);
        product.setUnitsInStock(unitsInStock);
        product.setCategory(category);
        product.setGender(category.getGender());
        product.setSizes(getSizesById(sizeIds));

        this.productRepository.save(product);
        saveImages(product, images);

        List<Image> productImages = this.imageRepository.findByProductId(product.getId());

        return convertToProductResponseDTO(product, productImages);
    }


    @Override
    @Transactional
    public ProductResponseDTO updateProduct(long productId,
                                 String productName,
                                 String description,
                                 BigDecimal price,
                                 int unitsInStock,
                                 long categoryId,
                                 List<Long> sizeIds,
                                 MultipartFile[] images) throws IOException {

        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        List<Image> oldImages = this.imageRepository.findByProductId(productId);

        product.setProductName(productName);
        product.setDescription(description);
        product.setUnitPrice(price);
        product.setUnitsInStock(unitsInStock);
        product.setCategory(category);
        product.setGender(category.getGender());
        product.setSizes(getSizesById(sizeIds));

        deleteImagesFromCloudinary(oldImages);
        this.productRepository.save(product);
        saveImages(product, images);

        List<Image> productImages = this.imageRepository.findByProductId(product.getId());

        return convertToProductResponseDTO(product, oldImages);
    }

    @Override
    @Transactional
    public void deleteProduct(long productId) throws IOException {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        List<Image> images = this.imageRepository.findByProductId(productId);

        if(!images.isEmpty()){
            this.productRepository.delete(product);
            deleteImagesFromCloudinary(images);
        }
    }

    @Override
    public List<ProductResponseDTO> getProducts(Character gender) {
        List<Product> products = this.productRepository.findByGender(gender);

        return convertToProductResponseDTO(products);
    }

    @Override
    public ProductResponseDTO getProduct(long productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        List<Image> images = this.imageRepository.findByProductId(product.getId());

        return convertToProductResponseDTO(product, images);
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(long id) {
        List<Product> products = this.productRepository.findProductsByCategory(id);

        return convertToProductResponseDTO(products);
    }

    @Override
    public List<ProductResponseDTO> searchProducts(String keyword) {
        List<Product> products = this.productRepository.searchByKeyword(keyword);

        return convertToProductResponseDTO(products);
    }

    private void saveImages(Product product, MultipartFile[] images) throws IOException {
        if(images != null && images.length > 0){
            this.imageRepository.clearImages(product.getId());

            List<Image> imageList = new ArrayList<>();

            for (MultipartFile image : images) {
                Image imageEntity = new Image();

                Map<?,?> uploadResult = uploadImage(image);
                imageEntity.setPublicId((String) uploadResult.get("public_id"));
                imageEntity.setImgUrl((String) uploadResult.get("url"));

                imageEntity.setProduct(product);
                imageList.add(imageEntity);
            }
            this.imageRepository.saveAll(imageList);
        }
    }

    private Map<?,?> uploadImage(MultipartFile file) throws IOException {
        try{
            return (Map<?,?>) this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "clothery"
            ));
        }catch (IOException exception){
            throw new IOException("Failed to upload image to Cloudinary.", exception);
        }
    }

    private void deleteImagesFromCloudinary(List<Image> images) throws IOException {
        try{
            for(Image image : images){
                this.cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.asMap(
                        "folder", "clothery"
                ));
            }
        }catch (IOException exception){
            throw new IOException("Failed to delete image from Cloudinary.", exception);
        }
    }

    private List<Size> getSizesById(List<Long> sizeIds) {
        if(sizeIds == null || sizeIds.isEmpty())
            throw new IllegalArgumentException("size ids cannot be empty");

        List<Size> sizes = this.sizeRepository.findAllById(sizeIds);

        if(sizes.size() != sizeIds.size())
            throw new EntityNotFoundException("size ids not found");

        return sizes;
    }

    private static ProductResponseDTO convertToProductResponseDTO(Product product, List<Image> productImages) {
        return new ProductResponseDTO(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getGender(),
                product.getUnitPrice(),
                product.getUnitsInStock(),
                product.getCategory(),
                product.getSizes(),
                productImages
        );
    }

    private List<ProductResponseDTO> convertToProductResponseDTO(List<Product> products) {
        return products.stream().map(product -> new ProductResponseDTO(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getGender(),
                product.getUnitPrice(),
                product.getUnitsInStock(),
                product.getCategory(),
                product.getSizes(),
                this.imageRepository.findByProductId(product.getId())
        )).toList();
    }
}
