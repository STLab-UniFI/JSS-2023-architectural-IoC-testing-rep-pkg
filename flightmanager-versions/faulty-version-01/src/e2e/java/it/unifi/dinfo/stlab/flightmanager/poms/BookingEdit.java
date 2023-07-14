package it.unifi.dinfo.stlab.flightmanager.poms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookingEdit {
	
	private WebDriver driver;
	
	// form fields
	private By firstNameInput;
	private By lastNameInput;
	private By idCardNumberInput;
	private By birthDateCalendar;
	private By datepickerDiv;
	
	// navigation buttons
	private By cancelBtn;
	private By updateBtn;
	
	// confirmation buttons
	private By returnBtn;
	
	
	public BookingEdit(WebDriver driver) {
		
		this.driver = driver;
		
		firstNameInput = By.cssSelector("tr:nth-child(2) .first-name-input");
		lastNameInput = By.cssSelector("tr:nth-child(2) .last-name-input");
		idCardNumberInput = By.cssSelector("tr:nth-child(2) .id-card-number-input");
		birthDateCalendar = By.cssSelector("tr:nth-child(2) .birth-date-calendar input");
		datepickerDiv = By.cssSelector("div[id*='ui-datepicker-div']");
		
		cancelBtn = By.cssSelector("input[id*='cancel-btn']");
		updateBtn = By.cssSelector("input[id*='update-btn']");
		
		returnBtn = By.cssSelector("input[id*='return-btn']");
	}
	
	public void updateBooking(String firstName, String lastName, String idCardNumber, Date birthDate) {
		
		driver.findElement(firstNameInput).clear();
		driver.findElement(firstNameInput).sendKeys(firstName);
		
		driver.findElement(lastNameInput).clear();
		driver.findElement(lastNameInput).sendKeys(lastName);
		
		driver.findElement(idCardNumberInput).clear();
		driver.findElement(idCardNumberInput).sendKeys(idCardNumber);
		
		String day = new SimpleDateFormat("DD").format(birthDate);
		String month = new SimpleDateFormat("MM").format(birthDate);
		String year = new SimpleDateFormat("YYYY").format(birthDate);
		
		driver.findElement(birthDateCalendar).clear();
		driver.findElement(birthDateCalendar).sendKeys(day + "/" + month + "/" + year);
		driver.findElement(datepickerDiv)
				.findElement(By.cssSelector(".ui-state-active")).click();
		
		// wait until calendar disappears
		WebDriverWait w = new WebDriverWait(driver,3);
	    w.until(ExpectedConditions.invisibilityOfElementLocated(datepickerDiv));
		
		driver.findElement(updateBtn).click();
		
		driver.findElement(returnBtn).click();
	}
	
	public void clickCancelButton() {
		
		driver.findElement(cancelBtn).click();
	}
}
