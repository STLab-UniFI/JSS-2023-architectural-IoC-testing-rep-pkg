package it.unifi.dinfo.stlab.flightmanager.poms;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightsResult {
	
	private WebDriver driver;
	
	// table
	private By flightRows;
	
	// navigation buttons
	private By detailsBtn;
	private By confirmBtn;
	private By cancelBtn;
	
	public  FlightsResult(WebDriver driver) {
		
		this.driver = driver;
		
		flightRows = By.cssSelector("table[id*='flights-table'] tr");
		
		detailsBtn = By.cssSelector("table[id*='flights-table'] tr:nth-child(2) .details-btn");
		confirmBtn = By.cssSelector(".confirm-btn");
		cancelBtn = By.cssSelector(".cancel-btn");
	}
	
	public void clickDetailsButton() {
		
		driver.findElement(detailsBtn).click();
	}
	
	public void clickConfirmButton() {
		
		driver.findElement(confirmBtn).click();
	}
	
	public void clickCancelButton() {
		
		driver.findElement(cancelBtn).click();
	}
	
	public int getTotalFlightVisualized() {
		List<WebElement> flights = driver.findElements(flightRows);
		// decremented because the first tr is the table header 
		return flights.size() - 1;
	}
	
	public String getSourceAirportOfFirstResult() {
		List<WebElement> flights = driver.findElements(flightRows);
		
		String flightRow = flights.get(1).getText();
		String[] flightRowStrings = flightRow.split("\n");
		return flightRowStrings[0].split(" ")[0];
	}
	
	public String getDestinationAirportOfFirstResult() {
		List<WebElement> flights = driver.findElements(flightRows);
		
		String flightRow = flights.get(1).getText();
		String[] flightRowStrings = flightRow.split("\n");
		return flightRowStrings[3].split(" ")[0];
	}
}
