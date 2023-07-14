package it.unifi.dinfo.stlab.flightmanager.poms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlightDetails {
	
	private WebDriver driver;
	
	// form fields
	private By originSelect;
	private By destinationSelect;
	private By departureDateCalendar;
	private By datepickerDiv;
	private By passengersSelect;
	
	// navigation buttons
	private By backBtn;
	private By confirmBtn;
	private By searchBtn;
	
	public  FlightDetails(WebDriver driver) {
		
		this.driver = driver;
		
		originSelect = By.cssSelector("select[id*='origin-select']");
		destinationSelect = By.cssSelector("select[id*='destination-select']");
		departureDateCalendar = By.cssSelector("input[id*='departure-date-calendar']");
		datepickerDiv = By.cssSelector("div[id*='ui-datepicker-div']");
		passengersSelect = By.cssSelector("select[id*='passengers-select']");

		confirmBtn = By.cssSelector(".confirm-btn");
		backBtn = By.cssSelector(".back-btn");
		searchBtn = By.cssSelector("input[id*='search-btn']");
		
	}

	public void clickBackButton() {
		driver.findElement(backBtn).click();
	}
	
	public void clickConfirmButton() {
		driver.findElement(confirmBtn).click();
	}
	
	public void searchAnotherFlight(String origin, String destination, Date departureDate, String passengers) {
		
		driver.findElement(originSelect).click();
		driver.findElement(originSelect)
				.findElement(By.xpath("option[contains(text(), '" + origin + "')]")).click();
		
		driver.findElement(destinationSelect).click();
		driver.findElement(destinationSelect)
				.findElement(By.xpath("option[contains(text(), '" + destination + "')]")).click();
		
		String day = new SimpleDateFormat("DD").format(departureDate);
		String month = new SimpleDateFormat("MM").format(departureDate);
		String year = new SimpleDateFormat("YYYY").format(departureDate);
		
		driver.findElement(departureDateCalendar).clear();
		driver.findElement(departureDateCalendar).sendKeys(day + "/" + month + "/" + year);
		driver.findElement(datepickerDiv)
				.findElement(By.cssSelector(".ui-state-active")).click();
		
		// wait until calendar disappears
		WebDriverWait w = new WebDriverWait(driver,3);
	    w.until(ExpectedConditions.invisibilityOfElementLocated(datepickerDiv));
		
		driver.findElement(passengersSelect).click();
		driver.findElement(passengersSelect)
				.findElement(By.xpath("option[contains(text(), '" + passengers + "')]")).click();
		
		driver.findElement(searchBtn).click();
	}
}
