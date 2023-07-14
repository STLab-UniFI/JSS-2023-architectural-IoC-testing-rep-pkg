package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FlightView {
	
	private WebDriver driver;
	
	// navigation buttons
	private By returnBtn;
	
	public FlightView(WebDriver driver) {
		
		this.driver = driver;
		returnBtn = By.cssSelector("input[id*='return-btn']");
	}
	
	public void clickReturnButton() {
		
		driver.findElement(returnBtn).click();
	}
}
