package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AirportView {
	
	private WebDriver driver;
	
	// navigation buttons
	private By returnBtn;
	
	public AirportView(WebDriver driver) {
		
		this.driver = driver;
		
		returnBtn = By.cssSelector("input[id*='returnBtn']");
	}
	
	public void clickReturnButton() {
		
		driver.findElement(returnBtn).click();
	}
}