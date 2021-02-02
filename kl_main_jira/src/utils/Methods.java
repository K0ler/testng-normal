package utils;


import main.Action;
import main.Main;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;


public class Methods extends Main {
	//static Actions action = new Actions(driver);
	
	
	public static void clickOn(WebElement element) {
		SyncMethods.waitUntilClickable(element);
		System.out.println("Clicking on [" + element.getText() + "]");
//		report.logInfo("Clicking on [" + element.getText() + "]");
		element.click();
	}
	
	
	public static void clickOn(By by) {
		System.out.println("Clicking on [" + by.toString() + "]");
//		report.logInfo("Clicking on [" + by.toString() + "]");
		wait.until(ExpectedConditions.elementToBeClickable(by)).click();
	}
	
	
	public static void ClickOnWithJSExecutor(WebElement we) {
		WebDriverWait wait2 = new WebDriverWait(driver, 20);
		System.out.println(we.toString());
		wait2.until(ExpectedConditions.elementToBeClickable(we));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", we);
	}
	
	
	public static void ClickOnWithJSExecutor(By by) {
		WebDriverWait wait2 = new WebDriverWait(driver, 20);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", wait2.until(ExpectedConditions.elementToBeClickable(by)));
	}
	
	
	public static WebElement expandRootElement(WebElement element) {
		WebElement ele = (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", element);
		return ele;
	}
	
	
	public static void sendText(By by, String text) {
//		report.logInfo("Send text to [" + by.toString() + "] text: <" + text +">");
		System.out.println("Send text to [" + by.toString() + "] text: <" + text +">");
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
		element.clear();
		element.sendKeys(text);
	}
	
	
	public static void sendText(WebElement element, String text) {
//		System.out.println("Send text to [" + by.toString() + "] text: <" + text +">");
//		wait.until(ExpectedConditions.elementToBeClickable(element));
		SyncMethods.waitUntilVisible(element);
		element.clear();
		element.sendKeys(text);
	}
	
	
	public static void sendTextWClick(WebElement element, String text) {
		SyncMethods.waitUntilVisible(element);
		SyncMethods.waitUntilClickable(element);
		element.click();
		if (text.isEmpty()) {
			element.sendKeys(Keys.CONTROL + "a");
			element.sendKeys(Keys.DELETE);
		} else {
			element.clear();
			element.sendKeys(text);
		}
	}
	
	
	public static void switchToFrame(WebElement element) {
		System.out.println(element.getClass().getName());
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
	}
	
	
	public static WebElement visible(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	
	public static WebElement visible(By by) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	
	public static WebElement clickable(By by) {
		return wait.until(ExpectedConditions.elementToBeClickable(by));
	}
	
	
	public static WebElement clickable(WebElement element) {
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	
	public static boolean isVisible1(WebElement element) {
		try {
			SyncMethods.waitUntilVisible(element);
			return true;
		} catch (TimeoutException exception) {
			return false;
		}
	}
	
	
	public static boolean isVisible(By by) {
		try {
			SyncMethods.waitUntilVisible(by);
			return true;
		} catch (TimeoutException exception) {
			return false;
		}
	}
	
	
	public static boolean isNotVisible(By by) {
		try {
			SyncMethods.waitUntilVisible(by);
			return false;
		} catch (TimeoutException exception) {
			return true;
		}
	}
	
	
	public static boolean isNotVisible(WebElement element) {
		try {
			SyncMethods.waitUntilVisible(element);
			return false;
		} catch (TimeoutException exception) {
			return true;
		}
	}
	
	
	public static void waitForInvisible(WebElement element, int timeout, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
	}
	
	
	public static boolean isVisible(WebElement element) {
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (TimeoutException exception) {
			return false;
		}
	}
	
	
	public static void scrollToAndClick(WebDriver driver, WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView()", element);
		clickOn(element);
	}
	
	
	public static String getDateTime(String format) {
		return new SimpleDateFormat(format).format(new GregorianCalendar().getTime());
	}
	
	
	public static void scrollTo(WebDriver driver, WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView()", element);
	}
	
	
	public static void focusNewWindow() {
		driver.switchTo().window(driver.getWindowHandle());
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	}
	
	
	public static void refreshPage() {
		driver.navigate().refresh();
	}
	
	
	public static WebElement getItemFromPopDropdown(WebElement popDropdown, List<WebElement> popResults, String option) {
		try {
			return new Action(driver).getItemFromPopDropdown(popDropdown, popResults, option);
		} catch (InterruptedException e) {
			Main.report.logError("There was error while selected option from dropdown");
			return null;
		}
	}
	
	
	public static WebElement getItemFromMenu(List<WebElement> navigationMenu, String option) {
		try {
			return new Action(driver).getItemFromMenu(navigationMenu, option);
		} catch (InterruptedException e) {
			Main.report.logError(e.getMessage());
			return null;
		}
	}
	
	
	public static ExpectedCondition<Boolean> visibilityOfNElementsLocatedBy(List<WebElement> elements, final int elementsCount) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				if (elements.size() != elementsCount)
					return false;
				return true;
			}
		};
	}
	
	
	public static String getXpath(WebElement element) {
		String str = element.toString();
		String[] listString = {"",""};
		if(str.contains("xpath"))
			listString = str.split("xpath:");
		String last = listString[1].trim();
		return last.substring(0, last.length() - 1);
	}
	
	
/*	//csv writer
	private static void createCSV(String[] cname, int row, List<WebElement>[] object) throws IOException {
		//String csv = "\\worldcupwrite.csv";
		String item = null;
		ArrayList<String> Sitem = new ArrayList<String>();
		//String[] Sitem = new ArrayList<String>();
		
		//CSVWriter writer = new CSVWriter(new FileWriter(csv));
		ArrayList<String[]> data = new ArrayList<String[]>();
		data.add(cname);
		for (int i=0;i<row;i++) {
			for (int l=0;l<cname.length;l++) {
				 item = object[l].get(i).getText();
				Sitem.add(item);
			}
			
			System.out.println(Sitem);
			//String[] x = Sitem;
			//data.add();
		}
		//writer.writeAll(data);
		System.out.println("CSV File written successfully All at a time");
		//writer.close();
	}
*/

}
