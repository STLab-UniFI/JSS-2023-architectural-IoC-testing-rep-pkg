package it.unifi.dinfo.stlab.flightmanager.poms;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookingDetails {
	
	private WebDriver driver;
	
	// form fields
	private By emailInput;
	private By passengerRows;
	private By nameInput;
	private By surnameInput;
	private By idCardInput;
	private By birthDateCalendar;
	private By datepickerDiv;
	
	// price widget
	private By totalPriceText;
	
	// navigation buttons
	private By cancelBtn;
	private By confirmBtn;
	
	// confirmation buttons
	private By backBtn;
	
	public BookingDetails(WebDriver driver) {
		
		this.driver = driver;
		
		emailInput = By.cssSelector(".email-input");
		passengerRows = By.cssSelector(".passengers-table tr");
		nameInput = By.cssSelector(".name-input");
		surnameInput = By.cssSelector(".surname-input");
		idCardInput = By.cssSelector(".id-card-input");
		birthDateCalendar = By.cssSelector(".birth-date-calendar input");
		datepickerDiv = By.cssSelector("div[id*='ui-datepicker-div']");
		
		totalPriceText = By.cssSelector(".full-price-text");
		
		cancelBtn = By.cssSelector(".cancel-btn");
		confirmBtn = By.cssSelector(".confirm-btn");
		
		backBtn = By.cssSelector(".back-btn");
	}

	public float getTotalPriceWithoutDiscount() {
		
		String price = driver.findElement(totalPriceText).getText().split("€")[1];
		return Float.parseFloat(price);
	}

	public void clickCancelButton() {
		
		driver.findElement(cancelBtn).click();
	}

	public void compileBookingDataFormAndConfirm(String email, String name, String surname,
			String id, String birthDate) {

		driver.findElement(emailInput).sendKeys(email);
		
		List<WebElement> passengers = driver.findElements(passengerRows);
		passengers.remove(0); // the first element represents the header row
		
		for (WebElement passenger : passengers) {
			passenger.findElement(nameInput).sendKeys(name);
			passenger.findElement(surnameInput).sendKeys(surname);
			passenger.findElement(idCardInput).sendKeys(id);
			
			passenger.findElement(birthDateCalendar).clear();
			passenger.findElement(birthDateCalendar).sendKeys(birthDate);
			driver.findElement(datepickerDiv)
					.findElement(By.cssSelector(".ui-state-active")).click();
			
			// wait for the calendar widget to disappear
			WebDriverWait w = new WebDriverWait(driver, 3);
			w.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ui-datepicker")));
		}
		
		clickConfirmButton();
	}

	private void clickConfirmButton() {
		
		driver.findElement(confirmBtn).click();
	}
	
	public void clickBackButton() {
		
		driver.findElement(backBtn).click();
	}

}
