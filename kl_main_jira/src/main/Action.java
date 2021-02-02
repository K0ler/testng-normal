package main;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Methods;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class Action {
	
	private WebDriver driver;
	private WebDriverWait waitShort = new WebDriverWait(Main.driver, 3);
	
	
	public Action(WebDriver driver) {
		this.driver = driver;
	}
	
	
	public WebElement getItemFromMenu(List<WebElement> menu, String option ) throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Main.report.logInfo("Search option '" + option + "'");
		for (WebElement item : menu) {
			if(item.getText().equals(option)) {
				Main.report.logPass("The option '" + item.getText() + "' was found");
				return item;
			}
		}
		Main.report.logFail("The option '" + option + "' doesn't exist");
		throw new InterruptedException("The option " + option + " doesn't exist");
	}
	
	
	public WebElement getItemFromPopDropdown(WebElement popDropdown, List<WebElement> popResults, String option) throws InterruptedException {
		waitShort.until(ExpectedConditions.visibilityOf(popDropdown));
		Main.report.logInfo("Click pop dropdown");
		Methods.clickOn(popDropdown);
		Main.report.logPass("Dropdown was clicked");
		waitShort.until(ExpectedConditions.visibilityOf(popResults.get(0)));
		for (WebElement item : popResults) {
			Main.report.logInfo("#Check option " + item.getText());
			if (item.getText().trim().equals(option)) {
				Main.report.logInfo("+The option '" + option + " 'was found");
				return item;
			}
		}
		Main.report.logFail("The option '" + option + "' doesn't exist");
		throw new InterruptedException("The option " + option + " doesn't exist");
	}
	
	
}
