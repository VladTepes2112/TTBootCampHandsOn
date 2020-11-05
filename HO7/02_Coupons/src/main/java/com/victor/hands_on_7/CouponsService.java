package com.victor.hands_on_7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Service
public class CouponsService {

    @Autowired
    private couponREPO couponREPO;

    public CouponsService(@Autowired couponREPO couponREPO) {
        this.couponREPO = couponREPO;
    }

    public boolean existsById(Integer id){
        return id != null && couponREPO.existsById(id);
    }

    public Coupon addCoupon(Coupon coupon) {
        if(existsById(coupon.getId()))
            return null;
        couponREPO.save(coupon);
        return coupon;
    }

    public Iterable<Coupon> getAllCoupons() {
        Iterable<Coupon> coupons = couponREPO.findAll();
        return coupons;
    }

    public Coupon getCouponByID(int id) {
        if(existsById(id))
            return null;
        return couponREPO.findById(id).get();
    }

    public Coupon getCouponByCode(String code) {
        Iterable<Coupon> all = getAllCoupons();
        Coupon coupon = null;

        for(Coupon a : all)
            if(a.getCode().equals(code)) coupon = a;
        return coupon;
    }

    public Double getDiscountByCode(String code) {
        Coupon couponByCode = getCouponByCode(code);

        return couponByCode == null ? null : couponByCode.getDiscount();
    }

    public Coupon deleteById(int id){
        if(existsById(id)) {
            Coupon couponByID = getCouponByID(id);
            couponREPO.deleteById(id);
            return couponByID;
        }
        return null;
    }
}
