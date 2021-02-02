package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import static main.Main.report;
import static utils.Methods.sendText;

public class EditApplication_Component extends Driver_Component{

    public EditApplication_Component(WebDriver driver){
        super (driver);
    }

    //-----FIELDS----------------------------------------------

    @FindBy (id = "application-address")
    WebElement inpAdress;

    @FindBy (xpath = "//div[2]/div/div/div/button")
    WebElement btnAddNewArgument;

    @FindBy (xpath = "//div/div/button[2]")
    WebElement btnClearEverythig;

    @FindBy (xpath = "(//input[@placeholder = 'Argument'])[last()]")
    WebElement inpArgumentName;

    @FindBy (xpath = "(//input[@placeholder = 'Argument'])[last()]/../../../td[2]")
    WebElement inpValueArgument;

    @FindBy (xpath = "//div/div/div/table/tbody/tr//button")
    WebElement btnTrash;

    @FindBy (xpath = "//button[@type = 'submit']")
    WebElement btnSave;




    //-----METHODS----------------------------------------------

    public void editAdress(String adress){
        report.logInfo("Try to edit adress.");
        sendText(inpAdress, adress);
        report.logPass("Adress changed to: " + adress);
    }

    public void deleteArgument(int whichArgument){
        report.logInfo("Try to delete argument.");
        WebElement argumentToDelete = driver.findElement(By.xpath("//div/div/div/table/tbody/tr["+whichArgument+"]//button"));
        argumentToDelete.click();
        report.logPass("Argument deleted.");
    }


}
