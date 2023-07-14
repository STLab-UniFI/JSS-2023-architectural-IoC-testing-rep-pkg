package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddCountry {

	private WebDriver driver;

	// form fields
	private By countryNameInput;
	private By countryCodeInput;
	private By countryFeeInput;
	private By ueCountryRadioBtn;

	// navigation buttons
	private By cancelBtn;
	private By saveBtn;

	public AddCountry(WebDriver driver) {

		this.driver = driver;

		countryNameInput = By.cssSelector("input[id*='countryNameInput']");
		countryCodeInput = By.cssSelector("input[id*='countryCodeInput']");
		countryFeeInput = By.cssSelector("input[id*='countryFeeInput']");
		ueCountryRadioBtn = By.cssSelector("table[id*='ueCountryRadioBtn']");

		cancelBtn = By.cssSelector("input[id*='cancelBtn']");
		saveBtn = By.cssSelector("input[id*='saveBtn']");
	}

	public void insertCountryInfo(String name, String code, Float fee, boolean eu) {

		driver.findElement(countryNameInput).clear();
		driver.findElement(countryNameInput).sendKeys(name);

		driver.findElement(countryCodeInput).clear();
		driver.findElement(countryCodeInput).sendKeys(code);

		driver.findElement(countryFeeInput).clear();
		driver.findElement(countryFeeInput).sendKeys(fee.toString());

		driver.findElement(ueCountryRadioBtn)
				.findElement(By.cssSelector("input[value='" + String.valueOf(eu) + "']")).click();
	}

	public void clickCancelButton() {
		driver.findElement(cancelBtn).click();
	}

	public void clickSaveButton() {
		driver.findElement(saveBtn).click();
	}

}
