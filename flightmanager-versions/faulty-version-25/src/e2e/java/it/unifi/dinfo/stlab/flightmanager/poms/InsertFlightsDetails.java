package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InsertFlightsDetails {
	
	private WebDriver driver;
	
	// form fields
	private By flightRepetitionsInput;
	private By returnFlightCheckbox;
	
	// navigation buttons
	private By cancelBtn;
	private By saveAndAddNewBtn;
	private By saveBtn;
	
	public InsertFlightsDetails(WebDriver driver) {
		
		this.driver = driver;
		
		flightRepetitionsInput = By.cssSelector("input[id*='flight-repetitions-input']");
		returnFlightCheckbox = By.cssSelector("input[id*='return-flight-checkbox']");
		
		cancelBtn = By.cssSelector("input[id*='cancel-btn']");
		saveAndAddNewBtn = By.cssSelector("input[id*='save-and-add-new-btn']");
		saveBtn = By.cssSelector("input[id*='save-btn']");
	}
	
	public void insertFlightDetails(int flightRepetitions, boolean returnFlight) {
		
		driver.findElement(flightRepetitionsInput).clear();
		driver.findElement(flightRepetitionsInput).sendKeys(Integer.toString(flightRepetitions));
		
		// inverts the logic if the checkbox is already selected
		if (driver.findElement(returnFlightCheckbox).isSelected()) {
			returnFlight = !returnFlight;
		}
		
		if (returnFlight) {
			driver.findElement(returnFlightCheckbox).click();
		}
	}
	
	public void clickCancelButton() {
		
		driver.findElement(cancelBtn).click();
	}
	
	public void clickSaveAndAddNewButton() {
		
		driver.findElement(saveAndAddNewBtn).click();
	}
	
	public void clickSaveButton() {
		
		driver.findElement(saveBtn).click();
	}
}
