package com.filahi.springboot.clothery.constant;


public class Constants {
    public static final String CATEGORY_FOLDER = System.getProperty("user.home") + "/clothery/categories/";
    public static final String PRODUCT_FOLDER = System.getProperty("user.home") + "/clothery/products/";
    public static final String HERO_FOLDER = System.getProperty("user.home") + "/clothery/hero/";
    public static final String DEFAULT_CATEGORY_IMAGE_PATH = "/images/categories/";
    public static final String CATEGORY_IMAGE_PATH = "/api/categories/image/";
    public static final String PRODUCT_IMAGE_PATH = "/api/products/image/";
    public static final String HERO_IMAGE_PATH = "/api/hero/";
    public static final String DIRECTORY_CREATED = "Created directory for: ";
    public static final String FILE_SAVED_IN_FILE_SYSTEM = "Saved file in file system by name: ";
    public static final String AVIF_EXTENSION = "avif";
    public static final String DOT = ".";
    public static final String SEPARATOR = "/";
    public static final String UNDERSCORE = "_";
    public static final String INCORRECT_IMAGE_FILE = "Please upload the right image file (.avif)";

    public static final String CATEGORY_NOT_FOUND = "Category not found.";
    public static final String PRODUCT_NOT_FOUND = "Product not found.";
    public static final String FILE_TOO_BIG = "File too large! Maximum allowed is 1MB";
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
}
