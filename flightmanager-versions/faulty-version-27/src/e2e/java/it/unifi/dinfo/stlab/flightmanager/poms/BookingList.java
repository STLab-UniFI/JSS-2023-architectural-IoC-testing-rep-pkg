package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BookingList {
	
	private WebDriver driver;
	
	// navigation buttons
	private By detailsBtn;
	private By backBtn;
	
	public BookingList(WebDriver driver) {
		
		this.driver = driver;
		
		detailsBtn = By.cssSelector("tr:nth-child(2) .details-btn");
		backBtn = By.cssSelector("input[id*='back-btn']");
	}
	
	public void goToBookingView() {
		
		driver.findElement(detailsBtn).click();
	}
	
	public void backToHome() {
		
		driver.findElement(backBtn).click();
	}
}
