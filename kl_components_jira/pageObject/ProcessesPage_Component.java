package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import static main.Main.report;
import static utils.Methods.sendText;

public class ProcessesPage_Component extends Driver_Component{

    public ProcessesPage_Component(WebDriver driver){
        super (driver);
    }

    //-----FIELDS----------------------------------------------



    @FindBy (xpath = "//div[2]/app-input-search/div/input")
    WebElement inpSearch;

    @FindBy (xpath = "//div[2]/app-input-search/div/span/button/i")
    WebElement btnSearch;

    @FindBy (xpath = "(//input[@placeholder = 'Argument'])[last()]")
    WebElement inpArgumentName;

    @FindBy (xpath = "(//input[@placeholder = 'Argument'])[last()]/../../../td[2]")
    WebElement inpValueArgument;

    @FindBy (xpath = "//div/div/div/table/tbody/tr//button")
    WebElement btnTrash;

    @FindBy (xpath = "//button[@type = 'submit']")
    WebElement btnSave;




    //-----METHODS----------------------------------------------

    public void editSearch(String name){
        report.logInfo("Wpisanie w pole nazwy procesu: " + name);
        sendText(inpSearch, name);
        report.logPass("Adress changed to: " + name);
    }

    public void deleteArgument(int whichArgument){
        report.logInfo("Try to delete argument.");
        WebElement argumentToDelete = driver.findElement(By.xpath("//div/div/div/table/tbody/tr["+whichArgument+"]//button"));
        argumentToDelete.click();
        report.logPass("Argument deleted.");
    }


}
