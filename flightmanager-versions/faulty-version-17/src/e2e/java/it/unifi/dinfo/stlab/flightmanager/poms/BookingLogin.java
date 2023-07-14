package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BookingLogin {
	
	private WebDriver driver;
	
	// form fields
	private By reservationIdInput;
	private By emailInput;
	
	// navigation buttons
	private By backBtn;
	private By confirmBtn;
	
	public BookingLogin(WebDriver driver) {
		
		this.driver = driver;

		reservationIdInput = By.cssSelector("input[id*='reservation-id-input']");
		emailInput = By.cssSelector("input[id*='email-input']");
		
		backBtn = By.cssSelector("input[id*='back-btn']");
		confirmBtn = By.cssSelector("input[id*='confirm-btn']");
	}
	
	public void login(String reservationId, String email) {
		
		driver.findElement(reservationIdInput).clear();
		driver.findElement(reservationIdInput).sendKeys(reservationId);
		
		driver.findElement(emailInput).clear();
		driver.findElement(emailInput).sendKeys(email);
		
		driver.findElement(confirmBtn).click();
	}
	
	public void clickBackButton() {
		
		driver.findElement(backBtn).click();
	}
}
