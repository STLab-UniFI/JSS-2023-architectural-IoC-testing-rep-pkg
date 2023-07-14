package it.unifi.dinfo.stlab.flightmanager.poms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
	private WebDriver driver;
	
	// login/logout buttons
	private By loginBtn;
	private By logoutBtn;
	
	// form fields
	private By originSelect;
	private By destinationSelect;
	private By departureDateCalendar;
	private By datepickerDiv;
	private By passengersSelect;
	
	// navigation buttons
	private By searchBookingBtn;
	private By myBookingsBtn;
	private By searchFlightsBtn;
	
	public Home(WebDriver driver) {
		this.driver = driver;
		
		loginBtn = By.cssSelector("input[id*='login-btn']");
		logoutBtn = By.cssSelector("input[id*='logout-btn']");
		
		originSelect = By.cssSelector("select[id*='origin-select']");
		destinationSelect = By.cssSelector("select[id*='destination-select']");
		departureDateCalendar = By.cssSelector("input[id*='departure-date-calendar']");
		datepickerDiv = By.cssSelector("div[id*='ui-datepicker-div']");
		passengersSelect = By.cssSelector("select[id*='passengers-select']");
		
		searchBookingBtn = By.cssSelector("input[id*='search-booking-btn']");
		myBookingsBtn = By.cssSelector("input[id*='my-bookings-btn']");
		searchFlightsBtn = By.cssSelector("input[id*='search-flights-btn']");
	}
	
	public void clickLoginButton() {
		
		driver.findElement(loginBtn).click();
	}
	
	public void clickLogoutButton() {
		
		driver.findElement(logoutBtn).click();
	}
	
	public void clickSearchBookingButton() {
		
		driver.findElement(searchBookingBtn).click();
	}
	
	public void clickMyBookingsButton() {
		
		driver.findElement(myBookingsBtn).click();
	}
	
	public void searchFlights(String origin, String destination, Date departureDate, String passengers) {
		
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
		
		driver.findElement(searchFlightsBtn).click();
	}
}
