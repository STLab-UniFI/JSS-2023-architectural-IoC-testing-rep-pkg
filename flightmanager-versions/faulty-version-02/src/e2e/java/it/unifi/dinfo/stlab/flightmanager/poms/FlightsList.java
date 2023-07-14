package it.unifi.dinfo.stlab.flightmanager.poms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlightsList {
	
	private WebDriver driver;
	
	// form fields
	private By startingDateCalendar;
	private By datepickerDiv;
	private By originSelect;
	private By destinationSelect;
	
	// navigation buttons
	private By searchBtn;
	private By backBtn;
	private By deleteBtn;
	private By detailsBtn;
	private By returnAfterDeleteBtn;
	
	public FlightsList(WebDriver driver) {
		
		this.driver = driver;
		
		startingDateCalendar = By.cssSelector("input[id*='starting-date-calendar']");
		datepickerDiv = By.cssSelector("div[id*='ui-datepicker-div']");
		originSelect = By.cssSelector("select[id*='origin-select']");
		destinationSelect = By.cssSelector("select[id*='destination-select']");

		searchBtn = By.cssSelector("input[id*='search-btn']");
		backBtn = By.cssSelector("input[id*='back-btn']");
		deleteBtn = By.cssSelector("tr:nth-child(2) .delete-btn");
		detailsBtn = By.cssSelector("tr:nth-child(2) .details-btn");
		returnAfterDeleteBtn = By.cssSelector("input[id*='returnBtn']");
	}
	
	public void search(Date startingDate, String origin, String destination) {
		
		String day = new SimpleDateFormat("DD").format(startingDate);
		String month = new SimpleDateFormat("MM").format(startingDate);
		String year = new SimpleDateFormat("YYYY").format(startingDate);
		
		driver.findElement(startingDateCalendar).clear();
		driver.findElement(startingDateCalendar).sendKeys(day + "/" + month + "/" + year);
		driver.findElement(datepickerDiv)
				.findElement(By.cssSelector(".ui-state-active")).click();
		
		// wait until calendar disappears
		WebDriverWait w = new WebDriverWait(driver,3);
	    w.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ui-datepicker")));
		
		driver.findElement(originSelect).click();
		driver.findElement(originSelect)
				.findElement(By.xpath("option[contains(text(), '" + origin + "')]")).click();
		
		driver.findElement(destinationSelect).click();
		driver.findElement(destinationSelect)
				.findElement(By.xpath("option[contains(text(), '" + destination + "')]")).click();
		
		driver.findElement(searchBtn).click();
	}
	
	public void clickBackButton() {
		driver.findElement(backBtn).click();
	}
	
	public void clickDeleteButtonAndReturn() {
		driver.findElement(deleteBtn).click();
		driver.switchTo().alert().accept();
		driver.findElement(returnAfterDeleteBtn).click();
	}
	
	public void clickDetailsButton() {
		driver.findElement(detailsBtn).click();
	}
}
