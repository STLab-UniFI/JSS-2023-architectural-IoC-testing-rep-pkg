package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddAirportsDetails {
	
	private WebDriver driver;
	
	// form fields
	private By airportAddressInput;
	private By airportInternationalRadioBtn;
	private By airportMailAddressInput;
	private By airportPhoneNumberInput;
	
	// navigation buttons
	private By backBtn;
	private By cancelBtn;
	private By saveBtn;
	private By saveAndAddNewBtn;
	
	public AddAirportsDetails(WebDriver driver) {
		
		this.driver = driver;
		
		airportAddressInput = By.cssSelector("input[id*='airportAddressInput']");
		airportInternationalRadioBtn = By.cssSelector("table[id*='airportInternationalRadioBtn']");
		airportMailAddressInput = By.cssSelector("input[id*='airportMailAddressInput']");
		airportPhoneNumberInput = By.cssSelector("input[id*='airportPhoneNumberInput']");
		
		backBtn = By.cssSelector("input[id*='backBtn']");
		cancelBtn = By.cssSelector("input[id*='cancelBtn']");
		saveBtn = By.cssSelector("input[id*='saveBtn']");
		saveAndAddNewBtn = By.cssSelector("input[id*='saveAndAddNewBtn']");
	}
	
	public void insertAirportDetails(String address, boolean international, String mail, String phone) {

		driver.findElement(airportAddressInput).clear();
		driver.findElement(airportAddressInput).sendKeys(address);
		
		driver.findElement(airportInternationalRadioBtn)
				.findElement(By.cssSelector("input[value='" + String.valueOf(international) + "']")).click();

		driver.findElement(airportMailAddressInput).clear();
		driver.findElement(airportMailAddressInput).sendKeys(mail);

		driver.findElement(airportPhoneNumberInput).clear();
		driver.findElement(airportPhoneNumberInput).sendKeys(phone);
	}
	
	public void clickBackButton() {
		
		driver.findElement(backBtn).click();
	}
	
	public void clickCancelButton() {
		
		driver.findElement(cancelBtn).click();
	}
	
	public void clickSaveButton() {
		
		driver.findElement(saveBtn).click();
	}
	
	public void clickSaveAndAddNewButton() {
		
		driver.findElement(saveAndAddNewBtn).click();
	}
}
