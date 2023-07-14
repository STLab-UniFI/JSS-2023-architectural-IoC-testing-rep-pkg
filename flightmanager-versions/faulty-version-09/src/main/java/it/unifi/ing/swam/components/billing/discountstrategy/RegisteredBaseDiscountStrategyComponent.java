package it.unifi.ing.swam.components.billing.discountstrategy;

import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.components.billing.discountstrategy.qualifiers.BaseUserDiscount;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.util.Util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Any
@RequestScoped
@BaseUserDiscount
public class RegisteredBaseDiscountStrategyComponent implements DiscountStrategyComponent {

    @Inject
    LoggedUserComponent loggedUserComponent;

    private static final float discountRatio_base = (float) 0.02;

    @Override
    public float applyDiscount(Booking booking) {
        System.out.println("sono RegisteredBaseDiscountStrategyComponent !");
        int numBooking = loggedUserComponent.getHistory();
        System.out.println(numBooking+ " <--------");
        return Util.round(booking.getPrice() * discountRatio_base, 2);
    }


}
