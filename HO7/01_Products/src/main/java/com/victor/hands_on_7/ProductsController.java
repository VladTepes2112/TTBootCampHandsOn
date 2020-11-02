package com.victor.hands_on_7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ProductsController {

    @RequestMapping("/")
    public String index() {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return "SPRING BOOT HANDS-ON-1 (7)";
    }

    @Autowired
    private ProductsREPO productsREPO;

    @PostMapping(path="/product/admin/add")
    public @ResponseBody ResponseEntity<String> addProduct(@RequestBody Product product) {
        product.setLastUpdateTime(null);
        if(product.getId() != null){
            if(productsREPO.existsById(product.getId()))
                return new ResponseEntity<>("Already exists", HttpStatus.ALREADY_REPORTED);
        }

        try{
            productsREPO.save(product);
            return new ResponseEntity<>("Saved", HttpStatus.CREATED);
        }catch (Exception e){
           return new ResponseEntity<>("not saved", HttpStatus.NOT_MODIFIED);
        }
    }


    @PutMapping(path="/product/admin/update/{id}")
    public @ResponseBody ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product product) {
        if(productsREPO.existsById(id)){
            try{
                product.setId(id);
                if(product.getLastUpdateTime() == null)
                    product.setLastUpdateTime(null);
                productsREPO.save(product);
                return new ResponseEntity<>(product, HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    @GetMapping(path="/product/all")
    public @ResponseBody ResponseEntity<Iterable<Product>> getAllProducts() {
        return new ResponseEntity<>(productsREPO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path="/product/byid/{id}")
    public @ResponseBody ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productsREPO.findById(id);
        if(product.isPresent()) return new ResponseEntity<>(product.get(), HttpStatus.FOUND);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/product/admin/byid/{id}")
    public @ResponseBody ResponseEntity<Product> getProductByAdminId(@PathVariable int id) {
        Optional<Product> product = productsREPO.findById(id);
        if(product.isPresent()) return new ResponseEntity<>(product.get(), HttpStatus.FOUND);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/product/byname/{name}")
    public @ResponseBody ResponseEntity<Iterable<Product>> getProductByName(@PathVariable String name) {
        Iterable<Product> all = productsREPO.findAll();
        ArrayList<Product> cases = new ArrayList<Product>();
        all.forEach(productTemp -> {
            if(productTemp.getName().equals(name)){
               cases.add(productTemp);
            }
        });
        if(cases.size() > 0) return new ResponseEntity<>(cases, HttpStatus.FOUND);
        return new ResponseEntity<>(cases, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/product/admin/delete/{id}")
    public @ResponseBody ResponseEntity<Product> deleteById(@PathVariable int id){
        Optional<Product> found = productsREPO.findById(id);
        if(found.isPresent()) {
            productsREPO.deleteById(id);
            return new ResponseEntity<>(found.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
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