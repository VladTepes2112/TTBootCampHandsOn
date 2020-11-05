package com.victor.hands_on_7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("coupon")
public class CouponController {

    private static final Logger log = LoggerFactory.getLogger(CouponController.class);
    private final CouponsService coupons;

    public CouponController(@Autowired CouponsService coupons) {
        this.coupons = coupons;
    }

    @PostMapping(path="add")
    public @ResponseBody ResponseEntity<Coupon> addCoupon(@RequestBody Coupon coupon) {
        log.trace("Added: " + coupon);
        Coupon existentC = coupons.addCoupon(coupon);
        if(existentC == null) {
            log.trace("Already existed " + HttpStatus.ALREADY_REPORTED + " id = " + coupon.getId());
            return new ResponseEntity<>(existentC, HttpStatus.ALREADY_REPORTED);
        }
        log.trace("Saved: " + existentC + " " + HttpStatus.CREATED);
        return new ResponseEntity<>(existentC, HttpStatus.CREATED);
    }

    @GetMapping(path="all")
    public @ResponseBody ResponseEntity<Iterable<Coupon>> getAllCoupons() {
        Iterable<Coupon> results = coupons.getAllCoupons();
        log.trace("FOUNDED COUPONS! " );
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(path="byid/{id}")
    public @ResponseBody ResponseEntity<Coupon> getProductById(@PathVariable int id) {
        Coupon c = coupons.getCouponByID(id);
        if(c != null) {
            log.trace("RETURNED <" + c + "> httpS: " + HttpStatus.OK);
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        log.trace("NOT FOUND -> " + id);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="bycode/{code}")
    public @ResponseBody ResponseEntity<Coupon> getCouponByCode(@PathVariable String code) {
        Iterable<Coupon> all = coupons.getAllCoupons();
        Coupon coupon = null;

        for(Coupon a : all)
            if(a.getCode().equals(code)) coupon = a;

        if(coupon != null) return new ResponseEntity<>(coupon, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="discount/bycode/{code}")
    public @ResponseBody ResponseEntity<Double> getDiscountByCode(@PathVariable String code) {
        Coupon result = coupons.getCouponByCode(code);

        if(result != null) return new ResponseEntity<>(result.getDiscount(), HttpStatus.OK);
        return new ResponseEntity<>(0.0, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/product/delete/{id}")
    public @ResponseBody ResponseEntity<Coupon> deleteById(@PathVariable int id){
        if(coupons.existsById(id)) {
            Coupon coupon = coupons.deleteById(id);
            return new ResponseEntity<>(coupon, HttpStatus.OK);
        }
        return null;
    }

}
