package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdministratorPage {

	private WebDriver driver;

	// navigation buttons
	private By airportListBtn;
	private By addAirportBtn;
	private By flightsListBtn;
	private By addFlightBtn;
	private By countryListBtn;
	private By addCountryBtn;

	public AdministratorPage(WebDriver driver) {

		this.driver = driver;

		airportListBtn = By.cssSelector("input[id*='airportListBtn']");
		addAirportBtn = By.cssSelector("input[id*='addAirportBtn']");
		flightsListBtn = By.cssSelector("input[id*='flightsListBtn']");
		addFlightBtn = By.cssSelector("input[id*='addFlightBtn']");
		countryListBtn = By.cssSelector("input[id*='countriesListBtn']");
		addCountryBtn = By.cssSelector("input[id*='addCountryBtn']");
	}

	public void clickAirportListButton() {

		driver.findElement(airportListBtn).click();
	}

	public void clickAddAirportButton() {

		driver.findElement(addAirportBtn).click();
	}

	public void clickFlightsListButton() {

		driver.findElement(flightsListBtn).click();
	}

	public void clickAddFlightButton() {

		driver.findElement(addFlightBtn).click();
	}

	public void clickCountryListButton() {
		driver.findElement(countryListBtn).click();
	}

	public void clickAddCountryButton() {
		driver.findElement(addCountryBtn).click();
	}
}
