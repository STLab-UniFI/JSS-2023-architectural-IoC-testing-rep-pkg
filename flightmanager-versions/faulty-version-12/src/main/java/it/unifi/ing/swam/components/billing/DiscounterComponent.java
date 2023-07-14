package it.unifi.ing.swam.components.billing;

import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.components.billing.discountstrategy.DiscountStrategyComponent;
import it.unifi.ing.swam.components.billing.discountstrategy.qualifiers.*;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.util.Util;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequestScoped
public class DiscounterComponent {

    @Inject
    @Any
    protected Instance<DiscountStrategyComponent> discountComponentSrc;
    protected List<DiscountStrategyComponent> activeDiscountStrategies;

    @Inject
    private LoggedUserComponent loggedUserComponent;

    public float apply(Booking booking){
        float totalDiscount = (float) 0.0;
        initDiscountStrategy(booking);
        for (DiscountStrategyComponent activeDiscount: activeDiscountStrategies) {
            totalDiscount += activeDiscount.applyDiscount(booking);
        }
        return Util.round(totalDiscount, 2);
    }

    private void initDiscountStrategy(Booking booking){
        if(activeDiscountStrategies == null || activeDiscountStrategies.size()==0){
            activeDiscountStrategies = new ArrayList();
            chooseDiscountStrategies(booking);
        }
    }

    private void chooseDiscountStrategies(Booking booking){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(booking.getDate());
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            activeDiscountStrategies.add(discountComponentSrc.select(
                    new AnnotationLiteral<CrazyWednesdayDiscount>(){})
                    .get());
        }
        if(booking.getPassengers().size() > 5){
            activeDiscountStrategies.add(discountComponentSrc.select(
                    new AnnotationLiteral<BigGroupDiscount>(){})
                    .get());
        }
        if(loggedUserComponent.isLoggedIn()){
            int userBookingHistory = loggedUserComponent.getHistory();

            if(userBookingHistory > 20) {
                activeDiscountStrategies.add(discountComponentSrc.select(
                        new AnnotationLiteral<GoldUserDiscount>(){})
                        .get());
            }
            else if(userBookingHistory > 10) {
                activeDiscountStrategies.add(discountComponentSrc.select(
                        new AnnotationLiteral<SilverUserDiscount>(){})
                        .get());
            }
            else if(userBookingHistory > 0) {
                activeDiscountStrategies.add(discountComponentSrc.select(
                        new AnnotationLiteral<BaseUserDiscount>(){})
                        .get());
            }
        }
    }




}
