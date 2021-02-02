package pageObject;

import java.awt.*;
import org.openqa.selenium.WebDriver;
import main.Main;



public class TestWorker_Component extends Driver_Component {


	public TestWorker_Component(WebDriver driver) throws InterruptedException{
		super(driver);
	}

	 String urltest = "https://www.google.com/";

	public void openPage() throws InterruptedException {
		Main.report.logInfo("Open page: " + urltest);
		driver.get(urltest);
		driver.manage().window().maximize();
		Thread.sleep(2000);
		//driver.switchTo().defaultContent();
		Main.report.logPass("Open page: " + urltest);
	}
	

	
}
