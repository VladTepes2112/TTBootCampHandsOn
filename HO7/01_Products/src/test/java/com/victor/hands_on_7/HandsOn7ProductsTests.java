/*
TO RUN THE TESTS YOU HAVE TO COMMENT THE ANNOTATION LINES IN MAINSECURITY.JAVA
I COULDN'T FIGURE OUT HOW TO TEST WITHOUT DOING SO
 */

package com.victor.hands_on_7;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.victor.hands_on_7.coupons_stuff.Coupon;
import com.victor.hands_on_7.coupons_stuff.ServiceCouponsClient;
import com.victor.hands_on_7.security.MainSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class HandsOn7ProductsTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductsService service;

    @MockBean
    private ServiceCouponsClient couponer;

    @MockBean
    private MainSecurity sec;

    @WithMockUser(value = "spring")
    @Test
    public void getAllProducts() throws Exception {
        Product res1 = new Product(1, "CocaCola1", 69.0);
        //Resulting one should have id
        Product res2 = new Product(2, "CocaCola2", 4.20);
        ArrayList<Product> toReturn = new ArrayList<>();
        toReturn.add(res1);
        toReturn.add(res2);
        when(service.getAllProducts()).thenReturn(toReturn);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @WithMockUser(value = "spring")
    @Test
    public void addProductNoDuplicated() throws Exception {
        Product resNew = new Product(null, "coca1", 42.0);
        Product resCreated = new Product(1, "coca1", 42.0);

        when(service.addProduct(any())).thenReturn(resCreated);
        String content = getJsonOf(resNew);
        System.out.println(content);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/product/admin/add").content(content).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201)).andDo(print());
    }

    @WithMockUser(value = "spring")
    @Test
    public void addProductWithDuplicated() throws Exception {
        Product resNew = new Product(1, "coca1", 42.0);
        when(service.addProduct(any())).thenReturn(null);
        String content = getJsonOf(resNew);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/product/admin/add").content(content).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(208)).andDo(print());
    }


    @WithMockUser(value = "spring")
    @Test
    public void getExistentProductById() throws Exception {
        Product resExistent = new Product(1, "Cocacola", 69.0);
        when(service.getProductById(1)).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/byid/1").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @WithMockUser(value = "spring")
    @Test
    public void getNonExistentProductById() throws Exception {
        Product resExistent = new Product(2, "coca1", 42.0);
        when(service.getProductById(2)).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/Product/byid/1").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(print());
    }


    @WithMockUser(value = "spring")
    @Test
    public void getProductPriceWithoutDiscount() throws Exception {
        Product resExistent = new Product(1, "coca1",  42.0);
        when(service.getProductById(1)).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/price").contentType(MediaType.APPLICATION_JSON).queryParam("productId", "1")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.unitPrice").value(42));
    }

    @WithMockUser(value = "spring")
    @Test
    public void getProductPriceWithDiscount() throws Exception {
        Product resExistent = new Product(1, "coca1",  42.0);
        Coupon c = new Coupon("MINUS10", 1, 10.0);
        when(couponer.getCouponByCode("MINUS10")).thenReturn(c);
        when(service.getProductById(1)).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/price").contentType(MediaType.APPLICATION_JSON)
                    .queryParam("productId", "1").queryParam("discountCode", "MINUS10")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andDo(print())
                //After applying the discount of 10, the price should be 32 not 42 as in the product declaration
                .andExpect(MockMvcResultMatchers.jsonPath("$.unitPrice").value(32));

    }

    @WithMockUser(value = "spring")
    @Test
    public void getProductPriceWithDiscountNonexistent() throws Exception {
        Product resExistent = new Product(1, "coca1",  42.0);
        Coupon c = new Coupon("MINUS10", 1, 10.0);
        when(couponer.getCouponByCode("MINUS10")).thenReturn(c);
        when(service.getProductById(1)).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/price").contentType(MediaType.APPLICATION_JSON)
                    .queryParam("productId", "1").queryParam("discountCode", "MINUS1000")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andDo(print())
                //After applying the discount of 10, the price should be 32 not 42 as in the product declaration
                .andExpect(MockMvcResultMatchers.jsonPath("$.unitPrice").value(42));

    }

    @WithMockUser(value = "spring")
    @Test
    public void getProductPriceWithDiscountOfAnotherProduct() throws Exception {
        Product resExistent = new Product(1, "coca1",  42.0);
        Product otherProduct = new Product(2, "Peksi",  6.9);
        Coupon c = new Coupon("MINUS10", 2, 6.0);
        when(service.getProductById(1)).thenReturn(resExistent);
        when(service.getProductById(2)).thenReturn(otherProduct);
        when(couponer.getCouponByCode("MINUS10")).thenReturn(c);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/price").contentType(MediaType.APPLICATION_JSON)
                    .queryParam("productId", "1").queryParam("discountCode", "MINUS10")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andDo(print())
                //Won't apply the discount because the code belongs to another product.
                .andExpect(MockMvcResultMatchers.jsonPath("$.unitPrice").value(42));
    }

    @WithMockUser(value = "spring")
    @Test
    public void getProductByName() throws Exception {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1, "coca1",  42.0));
        products.add(new Product(3, "coca1",  42.0));

        when(service.getProductsByName("coca1")).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/byname/coca1").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("coca1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("coca1"));

    }

    private String getJsonOf(Product res1) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(res1);
    }
}