package pageParam;


import main.Main;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import java.util.concurrent.TimeUnit;

public class pageConfig extends Main {


	@BeforeSuite
	public void setDriverPath() {

		
		WebDriverWait wait = null;

		if (parameters.getResourceName().equalsIgnoreCase("chrome")) {
			driverPath = "C:\\lib\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", driverPath);

			if (driver == null) {
				Main.driver = new ChromeDriver();
			}
			if (wait == null) {
				wait = new WebDriverWait(Main.driver, 60);
			}

		}

		
		if (parameters.getResourceName().equalsIgnoreCase("IE")) {
			driverPath = "C:\\lib\\IEDriverServer.exe";
			System.setProperty("webdriver.iexplorer.driver", driverPath);
			if (driver == null) {
				Main.driver = new InternetExplorerDriver();
			}
			if (wait == null) {
				wait = new WebDriverWait(Main.driver, 60);
			}

		}


		if (parameters.getResourceName().equalsIgnoreCase("test")) {
			driverPath = "C:\\lib\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", driverPath);

			if (driver == null) {
				Main.driver = new ChromeDriver();
			}
			if (wait == null) {
				wait = new WebDriverWait(Main.driver, 60);
			}

		}

		if (parameters.getResourceName().equalsIgnoreCase("ff")) {
			driverPath = "C:\\lib\\geckodriver.exe";
			System.setProperty("webdriver.firefox.driver", driverPath);
			if (driver == null) {
				Main.driver = new FirefoxDriver();
			}
			if (wait == null) {
				wait = new WebDriverWait(Main.driver, 60);
			}

		}
		
		if (parameters.getResourceName().equalsIgnoreCase("testMac")) {
			driverPath = "/Users/koler/lib/chromedriver";
			System.setProperty("webdriver.chrome.driver", driverPath);

			if (driver == null) {
				Main.driver = new ChromeDriver();
			}
			if (wait == null) {
				wait = new WebDriverWait(Main.driver, 60);
			}

		}
		
		
	}
}
