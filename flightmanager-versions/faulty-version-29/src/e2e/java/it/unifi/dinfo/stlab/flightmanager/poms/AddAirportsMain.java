package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddAirportsMain {
	
	private WebDriver driver;
	
	// form fields
	private By airportCountrySelectMenu;
	private By airportCityInput;
	private By airportFullNameInput;
	private By airportGmtSelectMenu;
	private By airportZipCodeInput;
	
	// navigation buttons
	private By cancelBtn;
	private By nextBtn;
	
	public AddAirportsMain(WebDriver driver) {
		
		this.driver = driver;

		airportCountrySelectMenu = By.cssSelector("select[id*='airportCountrySelectMenu']");
		airportCityInput = By.cssSelector("input[id*='airportCityInput']");
		airportFullNameInput = By.cssSelector("input[id*='airportFullNameInput']");
		airportGmtSelectMenu = By.cssSelector("select[id*='airportGmtSelectMenu']");
		airportZipCodeInput = By.cssSelector("input[id*='airportZipCodeInput']");

		cancelBtn = By.cssSelector("input[id*='cancelBtn']");
		nextBtn = By.cssSelector("input[id*='nextBtn']");
	}
	
	public void insertAirportInfo(String country, String city, String airport, String gmt, String zip) {
		
		driver.findElement(airportCountrySelectMenu).click();
		driver.findElement(airportCountrySelectMenu)
				.findElement(By.xpath("option[contains(text(), '" + country + "')]")).click();
		
		driver.findElement(airportCityInput).clear();
		driver.findElement(airportCityInput).sendKeys(city);
		
		driver.findElement(airportFullNameInput).clear();
		driver.findElement(airportFullNameInput).sendKeys(airport);
		
		driver.findElement(airportGmtSelectMenu).click();
		driver.findElement(airportGmtSelectMenu)
				.findElement(By.cssSelector("option[value='" + gmt + "']")).click();

		driver.findElement(airportZipCodeInput).clear();
		driver.findElement(airportZipCodeInput).sendKeys(zip);
	}
	
	public void clickCancelButton() {
		
		driver.findElement(cancelBtn).click();
	}
	
	public void clickNextButton() {
		
		driver.findElement(nextBtn).click();
	}
}
