package it.unifi.dinfo.stlab.flightmanager.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Login {
	
	private WebDriver driver;
	
	// form fields
	private By usernameInput;
	private By passwordInput;
	
	// navigation buttons
	private By cancelBtn;
	
	// login buttons
	private By userLoginBtn;
	private By adminLoginBtn;
	
	public Login(WebDriver driver) {
		
		this.driver = driver;

		usernameInput = By.cssSelector("input[id*='username-input']");
		passwordInput = By.cssSelector("input[id*='password-input']");
		
		cancelBtn = By.cssSelector("input[id*='cancel-btn']");
		
		userLoginBtn = By.cssSelector("input[id*='user-login-btn']");
		adminLoginBtn = By.cssSelector("input[id*='admin-login-btn']");
	}
	
	public void clickCancelBtn() {
		
		driver.findElement(cancelBtn).click();
	}
	
	public void loginAsUser(String username, String password) {
		
	    driver.findElement(usernameInput).sendKeys(username);
	    driver.findElement(passwordInput).sendKeys(password);
	    driver.findElement(userLoginBtn).click();	
	}
	
	public void loginAsAdmin(String username, String password) {
		
	    driver.findElement(usernameInput).sendKeys(username);
	    driver.findElement(passwordInput).sendKeys(password);
	    driver.findElement(adminLoginBtn).click();
	}
}
