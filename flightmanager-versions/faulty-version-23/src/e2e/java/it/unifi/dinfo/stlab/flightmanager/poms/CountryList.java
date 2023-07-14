package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CountryList {

	private WebDriver driver;

	private By backBtn;

	public CountryList(WebDriver driver) {
		this.driver = driver;
		this.backBtn = By.cssSelector("input[id*='backBtn']");
	}

	public void clickBackButton() {
		driver.findElement(backBtn).click();
	}

}
