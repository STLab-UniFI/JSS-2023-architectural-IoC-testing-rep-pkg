package it.unifi.ing.swam.components.billing.discountstrategy;

import it.unifi.ing.swam.model.Booking;

public interface DiscountStrategyComponent {


    public float applyDiscount(Booking booking);

}
