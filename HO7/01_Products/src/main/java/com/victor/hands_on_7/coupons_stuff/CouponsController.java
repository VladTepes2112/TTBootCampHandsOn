package com.victor.hands_on_7.coupons_stuff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

/*
I'm not sure about this part, this is more or less all it takes to fulfill the diagram
This endpoints are here because in the given diagram the only way to interact with the coupons is trough products api.

Not sure if in the diagram there's some hidden thing interacting with Coupon service to add coupons (diagram doesn't show that happening anyway so I'll assume so).
 */

@RestController
@RequestMapping("coupons")
public class CouponsController {
    private final ServiceCouponsClient couponer;

    private static final Logger log = LoggerFactory.getLogger(CouponsController.class);

    public CouponsController(@Autowired ServiceCouponsClient couponer) {
        this.couponer = couponer;
    }

    @GetMapping("all")
    public ResponseEntity<Coupon[]> getCoupons() throws Exception {
        Coupon[] coupons = couponer.getCoupons();
        ResponseEntity<Coupon[]> response = new ResponseEntity<>(coupons, HttpStatus.OK);
        log.info("all :");
        for (Coupon c: coupons) {
            log.info(c.toString());
        }
        return response;
    }

    @GetMapping("bycode/{code}")
    public ResponseEntity<Coupon> getCouponByCode(@PathVariable String code) throws Exception {
        Coupon coupon = couponer.getCouponByCode(code);
        ResponseEntity<Coupon> response = new ResponseEntity<>(coupon, HttpStatus.OK);
        log.info("bycode/"+code + ": " + coupon);
        return response;
    }

    @GetMapping("get_discount/{code}")
    public ResponseEntity<Double> getDiscountByCode(@PathVariable String code) throws Exception {
        Double discount = couponer.getDiscountByCode(code);
        ResponseEntity<Double> response = new ResponseEntity<>(discount, HttpStatus.OK);
        log.info("get_discount/"+code + ": " + discount);
        return response;
    }
}
