package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static main.Main.report;
import static utils.Methods.clickOn;
import static utils.Methods.sendText;

public class Environment_Component extends Driver_Component {

    public Environment_Component(WebDriver driver){
        super(driver);
    }

    //-----FIELDS--------------------------------

    @FindBy (xpath = "(//input)[1]")
    WebElement inpSearchApplication;

    @FindBy (xpath = "(//i[@class = 'fa fa-search'])[1]")
    WebElement inpLoopApplication;

    @FindBy (xpath = "//app-environment-applications/div/div[2]/table/tbody/tr/td/button[2]")
    WebElement inpEditApplication;

    @FindBy (xpath = "//app-environment-applications/div/div[2]/table/tbody/tr/td/button[1]")
    WebElement inpArgumentsApplication;

    //-----METHODS-------------------------------

    public void clickEnvironment(String environment){
        report.logInfo("Try to click on environment.");
        WebElement environmentToClick = driver.findElement(By.xpath("//a[text() = '"+environment+"']"));
        environmentToClick.click();
        report.logPass("Clicked on environment: " + environment);
    }

    public void enterNameApplication(String application){
        report.logInfo("Try to enter application.");
        sendText(inpSearchApplication, application);
        report.logPass("Application entered.");
    }

    public void clickLoopNearApplication(){
        report.logInfo("Try to click application's loop.");
        clickOn(inpLoopApplication);
        report.logPass("Loop clicked.");
    }

    public void searchApplication(String application){
        enterNameApplication(application);
        clickLoopNearApplication();
    }

    public void clickArgumentsApplication(String application){
        report.logInfo("Try to click on 'Arguments' button.");
        WebElement applicationArguments = driver.findElement(By.xpath("//label[contains(text(),'" + application + "')]/../..//button[1]"));
        applicationArguments.click();
        report.logPass("'Arguments' button clicked.");
    }

    public void clickEditApplication(String application){
        report.logInfo("Try to click 'Edit' button.");
        WebElement applicationToEdit = driver.findElement(By.xpath("//label[contains(text(),'" + application + "')]/../..//button[2]"));
        applicationToEdit.click();
        report.logPass("'Edit' button clicked.");
    }


}
