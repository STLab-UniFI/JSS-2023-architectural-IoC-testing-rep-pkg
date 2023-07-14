package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BookingView {
	
	private WebDriver driver;
	
	// navigation buttons
	private By backBtn;
	private By printBtn;
	private By deleteBtn;
	private By editBtn;
	private By bookingsBtn;
	
	// confirmation buttons
	private By returnBtn;
	
	public BookingView(WebDriver driver) {
		
		this.driver = driver;

		backBtn = By.cssSelector("input[id*='back-btn']");
		printBtn = By.cssSelector("input[id*='print-btn']");
		deleteBtn = By.cssSelector("input[id*='delete-btn']");
		editBtn = By.cssSelector("input[id*='edit-btn']");
		bookingsBtn = By.cssSelector("input[id*='bookings-btn']");
		
		returnBtn = By.cssSelector("input[id*='return-btn']");
	}
	
	public void backToHome() {
		
		driver.findElement(backBtn).click();
	}
	
	public void printBooking() {
		
		driver.findElement(printBtn).click();
	}
	
	public void deleteBooking() {
		
		driver.findElement(deleteBtn).click();
		
		driver.switchTo().alert().accept();
		
		driver.findElement(returnBtn).click();
	}
	
	public void goToBookingEdit() {
		
		driver.findElement(editBtn).click();
	}
	
	public void goToBookingList() {
		
		driver.findElement(bookingsBtn).click();
	}
}
