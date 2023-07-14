package it.unifi.ing.swam.components.billing.discountstrategy;

import it.unifi.ing.swam.components.billing.discountstrategy.qualifiers.CrazyWednesdayDiscount;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.util.Util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import java.util.Calendar;

@Any
@RequestScoped
@CrazyWednesdayDiscount
public class CrazyWednesdayDiscountStrategyComponent implements DiscountStrategyComponent {

    private static final float discountRatio = (float) 0.1;

    @Override
    public float applyDiscount(Booking booking) {
        System.out.println("sono CrazyWednesdayDiscountStrategyComponent !");
        return Util.round(booking.getPrice() * discountRatio, 2);
    }


}
