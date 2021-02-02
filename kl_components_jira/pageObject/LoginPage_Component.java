package pageObject;


import java.awt.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import main.Main;
import utils.Generators;
import utils.Methods;



public class LoginPage_Component extends Driver_Component {
	

	
	public LoginPage_Component(WebDriver driver) throws InterruptedException {
		super(driver);
    }

	String url = "https://keycloak-pf02.makeitright.pl/powerfarm/";
		
	
	@FindBy(id = "username")
	public WebElement inpUser;
	
	@FindBy(id = "password")
	public WebElement inpPassword;
	
	@FindBy(id = "kc-login")
	public WebElement btnLogin;
	
	public void openPage()  {
		Main.report.logInfo("Open page: " + url);
		driver.get(url);
		driver.manage().window().maximize();
		Main.report.logPass("Open page: " + url);
	}
	
	
	public void setUser(String user)  {
		Main.report.logInfo("Select user: " + user);
		Methods.sendText(inpUser, user);
		Main.report.logPass("Select user: " + user);
	}

	public void setPassword(String password ) {
		Main.report.logInfo("Select password");
		Methods.sendText(inpPassword, password);
		Main.report.logPass("Select password");
	}
	
	public void selectLogin() {
		Main.report.logInfo("Select Login");
		Methods.clickOn(btnLogin);
		Main.report.logPass("Select Login");
		
	}
}
