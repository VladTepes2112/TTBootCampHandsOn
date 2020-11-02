package com.victor.hands_on_7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private couponREPO couponREPO;

    @PostMapping(path="add")
    public @ResponseBody ResponseEntity<CouponEntity> addCoupon(@RequestBody CouponEntity couponEntity) {
        if(couponEntity.getId() != null)
            if(couponREPO.existsById(couponEntity.getId())) {
                ResponseEntity<CouponEntity> result = new ResponseEntity<>(couponEntity, HttpStatus.ALREADY_REPORTED);

                return result;
            }

        couponREPO.save(couponEntity);
        return new ResponseEntity<>(couponEntity, HttpStatus.CREATED);
    }

    @GetMapping(path="all")
    public @ResponseBody ResponseEntity<Iterable<CouponEntity>> getAllUsers() {
        return new ResponseEntity<>(couponREPO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path="byid/{id}")
    public @ResponseBody ResponseEntity<CouponEntity> getProductById(@PathVariable int id) {
        Optional<CouponEntity> product = couponREPO.findById(id);
        if(product.isPresent()) return new ResponseEntity<>(product.get(), HttpStatus.FOUND);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="bycode/{code}")
    public @ResponseBody ResponseEntity<CouponEntity> getCouponByCode(@PathVariable String code) {
        Iterable<CouponEntity> all = couponREPO.findAll();
        CouponEntity coupon = null;

        for(CouponEntity a : all)
            if(a.getCode().equals(code)) coupon = a;

        if(coupon != null) return new ResponseEntity<>(coupon, HttpStatus.FOUND);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="discount/bycode/{code}")
    public @ResponseBody ResponseEntity<Double> getDiscountByCode(@PathVariable String code) {
        Iterable<CouponEntity> all = couponREPO.findAll();
        CouponEntity product = null;

        for(CouponEntity a : all)
            if(a.getCode().equals(code)) product = a;

        if(product != null) return new ResponseEntity<>(product.getDiscount(), HttpStatus.FOUND);
        return new ResponseEntity<>(0.0, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/product/delete/{id}")
    public @ResponseBody ResponseEntity<CouponEntity> deleteById(@PathVariable int id){
        Optional<CouponEntity> found = couponREPO.findById(id);
        if(found.isPresent()) {
            couponREPO.deleteById(id);
            return new ResponseEntity<>(found.get(), HttpStatus.OK);
        }else{
            ResponseEntity<CouponEntity> couponEntityResponseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            return couponEntityResponseEntity;
        }
    }

}
