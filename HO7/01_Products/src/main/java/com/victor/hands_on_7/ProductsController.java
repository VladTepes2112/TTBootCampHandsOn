package com.victor.hands_on_7;

import com.victor.hands_on_7.coupons_stuff.Coupon;
import com.victor.hands_on_7.coupons_stuff.ServiceCouponsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ProductsController {

    private final ServiceCouponsClient cuponer;
    private final ProductsService products;

    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
    public ProductsController(@Autowired ServiceCouponsClient cuponer, @Autowired ProductsService products) {
        this.cuponer = cuponer;
        this.products = products;
    }

    @GetMapping("/")
    public String index() {
        log.trace("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return "SPRING BOOT HANDS-ON-1 (7)";
    }

    @PostMapping(path="/product/admin/add")
    public @ResponseBody ResponseEntity<String> addProduct(@RequestBody Product product) {
        product.setLastUpdateTime(null);
        Product newProduct = products.addProduct(product);
        if(newProduct == null){
            log.trace("Already exists" + HttpStatus.ALREADY_REPORTED);
            return new ResponseEntity<>("Already exists", HttpStatus.ALREADY_REPORTED);
        }
        log.trace("Saved successfully " + HttpStatus.CREATED);
        return new ResponseEntity<>("Saved", HttpStatus.CREATED);
    }


    @PutMapping(path="/product/admin/update/{id}")
    public @ResponseBody ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product product) {
        if(products.exists(id)){
            products.updateProduct(id, product);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }

        log.trace("File to update doesn't exist " + HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    @GetMapping(path="/product/all")
    public @ResponseBody ResponseEntity<Iterable<Product>> getAllProducts() {
        log.trace("products sent " + HttpStatus.OK);
        Iterable<Product> results = products.getAllProducts();
        results.forEach(person -> log.trace("Returned <" + person + "> "));
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(path="/product/byid/{id}")
    public @ResponseBody ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product p = products.getProductById(id);
        if(p != null) {
            log.trace("PRODUCT SENT " + HttpStatus.FOUND);
            return new ResponseEntity<>(p, HttpStatus.FOUND);
        }
        log.trace("PRODUCT REQUESTED NOT FOUND" + HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/product/price")
    public @ResponseBody ResponseEntity<Product> getFinalPrice(@RequestParam int productId, @RequestParam(defaultValue= "") String discountCode) {
        Product product = products.getProductById(productId);
        if(product == null) {
            log.trace("Product not found " + HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Double discount = 0.0;
        if(!discountCode.isEmpty()) {
            Coupon coupon = cuponer.getCouponByCode(discountCode);
            if (coupon != null && coupon.getProductId() == productId){
                discount = coupon.getDiscount() != null ? coupon.getDiscount() : 0;
            }
        }
        log.trace("Product price pre coupon: " + product.getUnitPrice());
        product.setUnitPrice(product.getUnitPrice()-discount);
        log.trace("Product price pos coupon: " + product.getUnitPrice());
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping(path="/product/admin/byid/{id}") //Testing purposes
    public @ResponseBody ResponseEntity<Product> getProductByAdminId(@PathVariable int id) {
        Product product = products.getProductById(id);
        if(product != null) {
            log.trace("Product returned with status " + HttpStatus.FOUND);
            return new ResponseEntity<>(product, HttpStatus.FOUND);
        }
        log.trace("Product not found " + HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/product/byname/{name}")
    public @ResponseBody ResponseEntity<Iterable<Product>> getProductByName(@PathVariable String name) {
        ArrayList<Product> cases = products.getProductsByName(name);
        if(cases.size() > 0) {
            log.trace("PRODUCT RETURNED WITH STATUS: " + HttpStatus.FOUND);
            return new ResponseEntity<>(cases, HttpStatus.FOUND);
        }
        log.trace("PRODUCT NAME NOT FOUND " + HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cases, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/product/admin/delete/{id}")
    public @ResponseBody ResponseEntity<Product> deleteById(@PathVariable int id){
        Product product = products.deleteById(id);
        if(product != null) {
            log.trace("PRODUCT DELETED " + HttpStatus.OK);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        log.trace("PRODUCT NOT FOUND " + HttpStatus.NOT_MODIFIED);
        return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
    }

}
/*
version: '3'
services:
  coupons:
    build: ./02_Coupons
    ports:
      - "55:55"
    depends_on:
      - "coupons_database"

  coupons_database:
    image: "mysql:5.7"
    ports:
      - "3308:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=hands_on_7
      - MYSQL_USER=coupons_user
      - MYSQL_PASSWORD=coupons_password

  products:
    build: ./01_Products
    ports:
      - "44:44"
    depends_on:
      - "products_database"
  products_database:
    image: "mysql:5.7"
    ports:
      - "3307:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=hands_on_7
      - MYSQL_USER=products_user
      - MYSQL_PASSWORD=products_password
* */