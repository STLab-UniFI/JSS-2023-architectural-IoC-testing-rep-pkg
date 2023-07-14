package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AirportList {
	
	private WebDriver driver;
	
	// navigation buttons
	private By detailsBtn;
	private By backBtn;
	
	public AirportList(WebDriver driver) {
		
		this.driver = driver;
		
		// finds the details button of the first result
		detailsBtn = By.cssSelector("tr:nth-child(2) td:last-child .btn");
		backBtn = By.cssSelector("input[id*='backBtn']");
	}
	
	public void clickDetailsButton() {
		
		driver.findElement(detailsBtn).click();
	}
	
	public void clickBackButton() {
		
		driver.findElement(backBtn).click();
	}
}