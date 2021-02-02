package main;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.NotConnectedException;


/**
 * Factory class for getting browsers.
 */
public class BrowserFactory {
	
	/* pola klasy */
		public final static int IMPLICIT_WAIT = 500;	// globalna wartość timeout'u dla webdrivera
		public final static int EXPLICIT_WAIT = 5;		// globalna wartość timeout'u dla webdrivera podczas oczekiwania na obiekt
		
		
	/**
	 * Zainicjowanie nowej instancji WebDriver'a.
	 * @param browserName nazwa przeglądarki
	 * @return selenium WebDriver dedykowany przeglądarce
	 * @throws NotConnectedException 
	 */
	public WebDriver setNewBrowser(String browserName) {
		WebDriver driver = null;
		switch (browserName.toLowerCase()) {
		case "firefox" :
			System.setProperty("webdriver.gecko.driver", "C:\\lib\\" + "geckodriver.exe");
			driver = new FirefoxDriver();
			break;
		case "chrome" :
			System.setProperty("webdriver.chrome.driver", "C:\\lib\\" + "chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-infobars=true");
			options.addArguments("--window-size=1280,1024");
//			options.addArguments("--disk-cache-dir=" + "C:\\temp\\chrome");
//			options.addArguments("--user-data-dir=" + "C:\\temp\\chrome");
//			options.addArguments("--start-maximized");
			driver = new ChromeDriver(options);
			break;
		default :
			return null;
		}
		driver.manage().timeouts().pageLoadTimeout(EXPLICIT_WAIT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.MILLISECONDS);
		return driver;
	}

}

