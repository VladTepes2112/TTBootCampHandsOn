package com.victor.hands_on_7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductsService {

    private final ProductsREPO productsREPO;

    public ProductsService(@Autowired ProductsREPO productsREPO){
        this.productsREPO = productsREPO;
    }
    public Product addProduct(Product product) {
        product.setLastUpdateTime(null);
        if(product.getId() != null){
            if(productsREPO.existsById(product.getId()))
                return null;
        }
        productsREPO.save(product);
        return product;
    }

    public boolean exists(int id){
        return productsREPO.existsById(id);
    }

    public Product updateProduct(int id, Product product) {
        product.setId(id);
        if(product.getLastUpdateTime() == null)
            product.setLastUpdateTime(null);
        productsREPO.save(product);
        return product;
    }

    public Iterable<Product> getAllProducts() {
        return productsREPO.findAll();
    }

    public Product getProductById(int id) {
        Optional<Product> product = productsREPO.findById(id);
        if(product.isPresent()) {
            return product.get();
        }
        return null;
    }

    public ArrayList<Product> getProductByName(String name) {
        Iterable<Product> allProducts = getAllProducts();
        ArrayList<Product> cases = new ArrayList<>();
        allProducts.forEach(productTemp -> {
            if(productTemp.getName().equals(name)){
                cases.add(productTemp);
            }
        });

        return cases;
    }

    public Product deleteById(int id){
        Optional<Product> found = productsREPO.findById(id);
        if(found.isPresent()) {
            Product p = found.get();
            productsREPO.deleteById(id);
            return p;
        }
        return null;
    }


}
