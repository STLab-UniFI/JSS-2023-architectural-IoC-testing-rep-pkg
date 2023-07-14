package it.unifi.ing.swam.components;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class RouterComponent {
    
    public String navigate(String pageName) {
    	return pageName + "?faces-redirect=true";
    }

}
