package com.filahi.springboot.clothery.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filahi.springboot.clothery.dto.SizeQuantityDTO;
import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.entity.Image;
import com.filahi.springboot.clothery.entity.Product;
import com.filahi.springboot.clothery.entity.Size;
import com.filahi.springboot.clothery.exception.domain.NotTheCorrectImageFileException;
import com.filahi.springboot.clothery.repository.ICategoryRepository;
import com.filahi.springboot.clothery.repository.IProductRepository;
import com.filahi.springboot.clothery.repository.ISizeRepository;
import com.filahi.springboot.clothery.service.IProductService;
import jakarta.persistence.NoResultException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.filahi.springboot.clothery.constant.Constants.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class ProductServiceImpl implements IProductService {

    private final ICategoryRepository ICategoryRepository;
    private final IProductRepository IProductRepository;
    private final ISizeRepository ISizeRepository;

    private final MediaType IMAGE_AVIF = new MediaType("image", "avif");
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Autowired
    public ProductServiceImpl(ICategoryRepository ICategoryRepository,
                              IProductRepository IProductRepository,
                              ISizeRepository ISizeRepository){

        this.ICategoryRepository = ICategoryRepository;
        this.IProductRepository = IProductRepository;
        this.ISizeRepository = ISizeRepository;
    }


    @Override
    @Transactional
    public Product addNewProduct(String productName,
                                 String description,
                                 BigDecimal price,
                                 String categoryId,
                                 String sizesJson,
                                 MultipartFile[] images) throws NotTheCorrectImageFileException, IOException {

        Optional<Category> theCategory = this.ICategoryRepository.findById(Long.parseLong(categoryId));

        if(theCategory.isEmpty())
            throw new NoResultException(CATEGORY_NOT_FOUND);

        Product existingProduct = this.IProductRepository.find(productName, description, theCategory.get().getGender());

        if(existingProduct != null)
            return setAndSave(existingProduct, productName, description, price, theCategory, sizesJson, images);

        Product theProduct = new Product();
        return setAndSave(theProduct, productName, description, price, theCategory, sizesJson, images);
    }


    @Override
    public List<Product> getProducts(Character gender) {
        return this.IProductRepository.findByGender(gender);
    }

    @Override
    public Product getProduct(Long id) {
        Product theProduct = this.IProductRepository.findProductById(id);

        if(theProduct == null)
            throw new NoResultException(PRODUCT_NOT_FOUND);

        return theProduct;
    }

    @Override
    public List<Product> getProductsByCategory(Long id) {
        return this.IProductRepository.findProductsByCategory(id);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return this.IProductRepository.searchByKeyword(keyword);
    }


    @Override
    @Transactional
    public void deleteProduct(Long id) throws IOException {
        Optional<Product> product = this.IProductRepository.findById(id);

        if(product.isEmpty())
            throw new NoResultException(PRODUCT_NOT_FOUND);

        Path productFolder = Paths.get(PRODUCT_FOLDER + getGender(product.get().getGender()) + SEPARATOR +
                product.get().getCategory().getType() + SEPARATOR + product.get().getProductName().replace(" ", "_")).toAbsolutePath().normalize();

        FileUtils.deleteDirectory(productFolder.toFile());

        this.IProductRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id,
                                 String productName,
                                 String description,
                                 BigDecimal price,
                                 String categoryId,
                                 String sizesJson,
                                 MultipartFile[] images) throws IOException {

        Optional<Product> existingProduct  = this.IProductRepository.findById(id);
        Optional<Category> theCategory = this.ICategoryRepository.findById(Long.parseLong(categoryId));

        if(existingProduct.isEmpty())
            throw new NoResultException(PRODUCT_NOT_FOUND);

        return setAndSave(existingProduct.get(), productName, description, price, theCategory, sizesJson, images);
    }

    @Transactional
    private Product setAndSave(Product theProduct,
                              String productName,
                              String description,
                              BigDecimal price,
                              Optional<Category> category,
                              String sizesJson,
                              MultipartFile[] images) throws NotTheCorrectImageFileException, IOException {


        theProduct.setProductName(productName);
        theProduct.setDescription(description);
        theProduct.setUnitPrice(price);
        theProduct.setGender(category.get().getGender());
        theProduct.setCategory(category.get());

        ObjectMapper objectMapper = new ObjectMapper();
        List<SizeQuantityDTO> sizeQuantities = objectMapper.readValue(
                sizesJson,
                new TypeReference<List<SizeQuantityDTO>>() {}
        );


        this.ISizeRepository.deleteByProductId(theProduct.getId());

        theProduct.clearSizes();
        for(SizeQuantityDTO sizeQty: sizeQuantities)
            theProduct.addSizes(sizeQty.getSize(), sizeQty.getQuantity());


        Integer unitsInSock = 0;
        for(Size sq : theProduct.getSizes()){
            unitsInSock += sq.getQuantity();
        }

        theProduct.setUnitsInStock(unitsInSock);

        theProduct.getImages().clear();
        if(images != null && images.length > 0){
            List<Image> imageEntities = new ArrayList<>();
            for(int i = 0; i < images.length; i++){
                MultipartFile image = images[i];
                Image imageEntity = saveImage(theProduct, image, i);
                imageEntities.add(imageEntity);
            }
            theProduct.getImages().addAll(imageEntities);
        }


        this.IProductRepository.save(theProduct);

        return theProduct;
    }


    private Image saveImage(Product product, MultipartFile image, int index) throws NotTheCorrectImageFileException, IOException {
        if(!Objects.equals(image.getContentType(), IMAGE_AVIF.toString()))
            throw new NotTheCorrectImageFileException(image.getOriginalFilename() + " " + INCORRECT_IMAGE_FILE);

        if(image.getSize() > MAX_FILE_SIZE)
            throw new NotTheCorrectImageFileException(FILE_TOO_BIG);

        Path productFolder = Paths.get(PRODUCT_FOLDER + getGender(product.getGender()) + SEPARATOR +
                product.getCategory().getType() + SEPARATOR + product.getProductName().replace(" ", "_")).toAbsolutePath().normalize();


        if(!Files.exists(productFolder)){
            Files.createDirectories(productFolder);
            LOGGER.info(DIRECTORY_CREATED + "{}", productFolder);
        }

        String fileName = product.getProductName().replace(" ", "_") + UNDERSCORE + index + DOT + AVIF_EXTENSION;
        Path targetFolder = productFolder.resolve(fileName);

        Files.deleteIfExists(targetFolder);
        Files.copy(image.getInputStream(), targetFolder, REPLACE_EXISTING);

        Image imageEntity = new Image();
        imageEntity.setImgUrl(setTheImgUrl(product.getProductName(), getGender(product.getGender()), product.getCategory().getType(), fileName));
        imageEntity.setProduct_img(product);

        LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + "{}", image.getOriginalFilename());

        return imageEntity;
    }

    private String setTheImgUrl(String productName, String gender, String type, String fileName){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(PRODUCT_IMAGE_PATH + gender + SEPARATOR + type + SEPARATOR +
                productName.replace(" ", "_") + SEPARATOR + fileName).toUriString();
    }

    private String getGender(Character g){
        if(g.equals('M'))
            return "men";
        else
            return "women";
    }
}
