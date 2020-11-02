package com.victor.hands_on_7.coupons_stuff;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Service
public class ServiceCouponsClient {
    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(ServiceCouponsClient.class);

    public ServiceCouponsClient(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Coupon getCouponByCode(String code) {
        String uri = "http://localhost:55/coupon/bycode/"+code;

        ResponseEntity<Coupon> responseEntity =
                restTemplate.exchange(
                        uri, HttpMethod.GET,
                        new HttpEntity<Coupon>(createHeaders("home_user", "home_password")),
                        Coupon.class);
        Coupon coupon = responseEntity.getBody();
        log.info("Coupon founded " + coupon.getCode() + " discount of: " + coupon.getDiscount());
        return coupon;
    }

    public Coupon[] getCoupons() {
        ResponseEntity<Coupon[]> responseEntity = restTemplate.getForEntity("http://localhost:55/coupon/all", Coupon[].class);
        Coupon[] coupons = responseEntity.getBody();
        log.info("Response: " + responseEntity.getStatusCode() + " body: " + responseEntity.getBody());
        return coupons;
    }

    public Double getDiscountByCode(String code) {
        String uri = "http://localhost:55/coupon/bycode/"+code;

        ResponseEntity<Double> responseEntity =
                restTemplate.exchange(
                        uri, HttpMethod.GET,
                        new HttpEntity<Double>(createHeaders("home_user", "home_password")),
                        Double.class);
        Double discount = responseEntity.getBody();

        return discount;
    }

    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}