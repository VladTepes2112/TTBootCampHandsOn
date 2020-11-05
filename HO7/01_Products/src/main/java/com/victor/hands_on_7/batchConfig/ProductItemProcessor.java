package com.victor.hands_on_7.batchConfig;

import com.victor.hands_on_7.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.sql.Date;

public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    private static final Logger log = LoggerFactory.getLogger(ProductItemProcessor.class);
    @Override
    public Product process(final Product product) throws Exception {
        final Integer id = product.getId();
        final String name = product.getName();
        final Date lastUpdateTime = product.getLastUpdateTime();
        final Double unitPrice = product.getUnitPrice();

        Product newProduct = new Product(id, name, unitPrice, lastUpdateTime);
        if (lastUpdateTime != null)
            newProduct.setLastUpdateTime(lastUpdateTime);
        return newProduct;
    }
}
