package com.victor.hands_on_7;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.victor.hands_on_7.security.MainSecurity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponsService service;

    @MockBean
    private MainSecurity security;


    @WithMockUser(value = "spring")
    @Test
    public void getAllCoupons() throws Exception {
        Coupon res1 = new Coupon(1, "FREER1", 1, 42.0);
        //Resulting one should have id
        Coupon res2 = new Coupon(2, "FREER2", 1, 69.12);
        ArrayList<Coupon> toReturn = new ArrayList<>();
        toReturn.add(res1);
        toReturn.add(res2);
        when(service.getAllCoupons()).thenReturn(toReturn);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @WithMockUser(value = "spring")
    @Test
    public void addCouponNoDuplicated() throws Exception {
        Coupon resNew = new Coupon(null, "FREER1", 1, 42.0);
        Coupon resCreated = new Coupon(1, "FREER1", 1, 42.0);

        when(service.addCoupon(any())).thenReturn(resCreated);
        String content = getJsonOf(resNew);
        System.out.println(content);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/coupon/add").content(content).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201)).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @WithMockUser(value = "spring")
    @Test
    public void addCouponWithDuplicated() throws Exception {
        Coupon resNew = new Coupon(1, "FREER1", 1, 42.0);
        when(service.addCoupon(any())).thenReturn(null);
        String content = getJsonOf(resNew);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/coupon/add").content(content).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(208)).andDo(print());
    }


    @WithMockUser(value = "spring")
    @Test
    public void getExistentCouponById() throws Exception {
        Coupon resExistent = new Coupon(1, "FREER1", 1, 42.0);
        when(service.getCouponByID(1)).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/byid/1").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200)).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @WithMockUser(value = "spring")
    @Test
    public void getNonExistentCouponById() throws Exception {
        Coupon resExistent = new Coupon(2, "FREER1", 1, 42.0);
        when(service.getCouponByID(1)).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/byid/5").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(print());
    }

    @WithMockUser(value = "spring")
    @Test
    public void getExistentCouponByCode() throws Exception {
        Coupon resExistent = new Coupon(1, "FREER1", 1, 42.0);
        ArrayList<Coupon> existent = new ArrayList<>();
        existent.add(resExistent);
        when(service.getAllCoupons()).thenReturn(existent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/bycode/FREER1").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200)).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(resExistent.getCode()));
    }

    @WithMockUser(value = "spring")
    @Test
    public void getNonExistentCouponByCode() throws Exception {
        Coupon resExistent = new Coupon(1, "FREER1", 1, 42.0);
        ArrayList<Coupon> existent = new ArrayList<>();
        existent.add(resExistent);
        when(service.getAllCoupons()).thenReturn(existent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/bycode/FREER12").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(print());
    }

    @WithMockUser(value = "spring")
    @Test
    public void getExistentDiscountCouponByCode() throws Exception {
        Coupon resExistent = new Coupon(1, "FREER1", 1, 42.0);
        when(service.getCouponByCode("FREER1")).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/discount/bycode/FREER1").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200)).andDo(print());
    }

    @WithMockUser(value = "spring")
    @Test
    public void getNonExistentDiscountCouponByCode() throws Exception {
        Coupon resExistent = new Coupon(2, "FREER1", 1, 42.0);
        when(service.getCouponByID(1)).thenReturn(resExistent);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/coupon/discount/bycode/ASDF").contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound()).andDo(print());
    }

    private String getJsonOf(Coupon res1) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(res1);
    }
}

/*.andExpect(content().string(containsString("Hello, Mock")))*/