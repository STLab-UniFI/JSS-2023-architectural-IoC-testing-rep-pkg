package it.unifi.ing.swam.components.billing.discountstrategy;

import it.unifi.ing.swam.components.billing.discountstrategy.DiscountStrategyComponent;
import it.unifi.ing.swam.components.billing.discountstrategy.qualifiers.BigGroupDiscount;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.util.Util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;

@Any
@RequestScoped
@BigGroupDiscount
public class GroupDiscountStrategyComponent implements DiscountStrategyComponent {

    private static final float discountRatio = (float) 0.05;

    @Override
    public float applyDiscount(Booking booking) {
        System.out.println("sono GroupDiscountStrategyComponent !");
        int nPassengers = booking.getPassengers().size();
        int c = Math.floorDiv(nPassengers, 5);
        return Util.round(booking.getPrice() * c * discountRatio, 2);
    }


}
