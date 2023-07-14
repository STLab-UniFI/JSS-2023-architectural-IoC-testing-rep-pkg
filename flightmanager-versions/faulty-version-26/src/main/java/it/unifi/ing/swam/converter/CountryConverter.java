package it.unifi.ing.swam.converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.ing.swam.dao.CountryDao;
import it.unifi.ing.swam.model.Country;

@Named
@SessionScoped
public class CountryConverter implements Converter<Country>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private CountryDao countryDao;
	
	private List<Country> countryList = new ArrayList<Country>();

	@Override
	public String getAsString(FacesContext context, UIComponent component, Country modelValue) {
	    if (modelValue == null) {
	        return "";
	    }

	    if (modelValue instanceof Country) {
	        return String.valueOf(((Country) modelValue).getId());
	    } else {
	        throw new ConverterException(new FacesMessage(modelValue + " is not a valid Country"));
	    }
	}
	
	@Override
	public Country getAsObject(FacesContext context, UIComponent component, String submittedValue) {
	    if (submittedValue == null || submittedValue.isEmpty()) {
	        return null;
	    }

	    try {
	        return findCountry(Long.valueOf(submittedValue));
	    } catch (NumberFormatException e) {
	        throw new ConverterException(new FacesMessage(submittedValue + " is not a valid Warehouse ID"), e);
	    }
	}
	
	private Country findCountry(Long id) {
		Country result = countryList.stream()
				.filter(country -> id.equals(country.getId()))
				.findAny()
				.orElse(null);
		if(result == null)
			result = countryDao.findById(id);
		if(result != null)
			countryList.add(result);
		return result;
		
	}


	

}
