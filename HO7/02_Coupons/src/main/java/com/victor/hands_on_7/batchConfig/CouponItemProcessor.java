package com.victor.hands_on_7.batchConfig;

import com.victor.hands_on_7.Coupon;
import org.springframework.batch.item.ItemProcessor;

public class CouponItemProcessor implements ItemProcessor<Coupon, Coupon> {

    @Override
    public Coupon process(final Coupon coupon) throws Exception {
        final Integer id = coupon.getId();
        final String name = coupon.getCode();
        final Integer productId = coupon.getProductId();
        final Double discount = coupon.getDiscount();

        return new Coupon(id, name, productId, discount);
    }
}
