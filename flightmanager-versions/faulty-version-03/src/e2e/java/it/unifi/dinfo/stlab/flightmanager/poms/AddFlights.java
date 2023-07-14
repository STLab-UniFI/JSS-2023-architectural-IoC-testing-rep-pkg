package it.unifi.dinfo.stlab.flightmanager.poms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddFlights {
	
	private WebDriver driver;
	
	// form fields
	private By flightNumberInput;
	private By departureDateCalendar;
	private By datepickerDiv;
	private By departureTimeInput;
	private By durationInput;
	private By passengersInput;
	private By pricePerPersonInput;
	private By originSelect;
	private By destinationSelect;
	
	// navigation buttons
	private By cancelBtn;
	private By nextBtn;
	
	public AddFlights(WebDriver driver) {
		
		this.driver = driver;
		
		flightNumberInput = By.cssSelector("input[id*='flight-number-input']");
		departureDateCalendar = By.cssSelector("input[id*='departure-date-calendar']");
		datepickerDiv = By.cssSelector("div[id*='ui-datepicker-div']");
		departureTimeInput = By.cssSelector("input[id*='departure-time-input']");
		durationInput = By.cssSelector("input[id*='duration-input']");
		passengersInput = By.cssSelector("input[id*='passengers-input']");
		pricePerPersonInput = By.cssSelector("input[id*='price-per-person-input']");
		originSelect = By.cssSelector("select[id*='origin-select']");
		destinationSelect = By.cssSelector("select[id*='destination-select']");
		
		cancelBtn = By.cssSelector("input[id*='cancel-btn']");
		nextBtn = By.cssSelector("input[id*='next-btn']");
	}
	
	public void insertFlightInfo(String flightNumber, Date departureDate, String departureTime,
				int duration, int passengers, float pricePerPerson, String origin, String destination) {
		
		driver.findElement(flightNumberInput).clear();
		driver.findElement(flightNumberInput).sendKeys(flightNumber);
		
		String day = new SimpleDateFormat("DD").format(departureDate);
		String month = new SimpleDateFormat("MM").format(departureDate);
		String year = new SimpleDateFormat("YYYY").format(departureDate);
		
		driver.findElement(departureDateCalendar).clear();
		driver.findElement(departureDateCalendar).sendKeys(day + "/" + month + "/" + year);
		driver.findElement(datepickerDiv)
				.findElement(By.cssSelector(".ui-state-active")).click();
		
		// wait until calendar disappears
		WebDriverWait w = new WebDriverWait(driver,3);
	    w.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ui-datepicker")));
		
		driver.findElement(departureTimeInput).clear();
		driver.findElement(departureTimeInput).sendKeys(departureTime);
		
		driver.findElement(durationInput).clear();
		driver.findElement(durationInput).sendKeys(Integer.toString(duration));
		
		driver.findElement(passengersInput).clear();
		driver.findElement(passengersInput).sendKeys(Integer.toString(passengers));
		
		driver.findElement(pricePerPersonInput).clear();
		driver.findElement(pricePerPersonInput).sendKeys(Float.toString(pricePerPerson));
		
		driver.findElement(originSelect).click();
		driver.findElement(originSelect)
				.findElement(By.xpath("option[contains(text(), '" + origin + "')]")).click();
		
		driver.findElement(destinationSelect).click();
		driver.findElement(destinationSelect)
				.findElement(By.xpath("option[contains(text(), '" + destination + "')]")).click();
	}
	
	public void clickCancelButton() {
		
		driver.findElement(cancelBtn).click();
	}
	
	public void clickNextButton() {
		
		driver.findElement(nextBtn).click();
	}
}
