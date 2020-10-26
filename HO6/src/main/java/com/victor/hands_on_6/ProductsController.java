package com.victor.hands_on_6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ProductsController {

    @RequestMapping("/")
    public String index() {
        return "SPRING BOOT HANDS-ON-1 (5)";
    }

    @Autowired // This means to get the bean called userRepository
    private ProductsREPO productsREPO;

    @PostMapping(path="/product/add") // Map ONLY POST Requests
    public @ResponseBody ResponseEntity<String>  addNewUser (@RequestBody Product product) {
        if(product.getId() != null){
            if(productsREPO.existsById(product.getId()))
                return new ResponseEntity<>("Already exists", HttpStatus.ALREADY_REPORTED);
        }

        if(product.areYouComplete()) {
            product.setLastUpdateTime(new Date(System.currentTimeMillis()));
            productsREPO.save(product);
            return new ResponseEntity<>("Saved successfully", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("The product information is incomplete or bad formed", HttpStatus.NOT_MODIFIED);
    }


    @PutMapping(path="/product/update/{id}") // Map ONLY POST Requests
    public @ResponseBody ResponseEntity<Product>  updateUser (@PathVariable int id, @RequestBody Product product) {
        if(product.areYouComplete() && productsREPO.existsById(product.getId())) {
            product.setId(id);
            productsREPO.save(product);

            return new ResponseEntity<>(product, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(path="/product/all")
    public @ResponseBody ResponseEntity<Iterable<Product>> getAllUsers() {
        return new ResponseEntity<>(productsREPO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path="/product/byid/{id}")
    public @ResponseBody ResponseEntity<Product> getProductById(@PathVariable int id) {
        // This returns a JSON or XML with the users
        Optional<Product> product = productsREPO.findById(id);
        if(product.isPresent()) return new ResponseEntity<>(product.get(), HttpStatus.FOUND);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/product/byname/{name}")

    public @ResponseBody ResponseEntity<Iterable<Product>> getProductByName(@PathVariable String name) {
        // This returns a JSON or XML with the users
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

    @DeleteMapping(path = "/product/delete/{id}")
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
