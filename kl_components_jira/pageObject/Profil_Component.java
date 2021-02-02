package pageObject;

import main.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Methods;

public class Profil_Component extends Driver_Component {


    public Profil_Component(WebDriver driver) throws InterruptedException {
        super(driver);
    }


    @FindBy (id ="workspaces")
    WebElement lstWorkSpaces;

    @FindBy (xpath = "//div/a[contains(.,'Reload')]")
    WebElement btnReload;


    public void workSpaces(String workSpace){
        Main.report.logInfo("click on work space list");
        Methods.clickOn(lstWorkSpaces);
        Main.report.logPass("click on work space list");
        Main.report.logInfo("orderScenario work space");
        WebElement worksSpace = driver.findElement(By.xpath("//select[@id='workspaces']/option[contains(.,'" + workSpace + "')]"));
        Methods.clickOn(worksSpace);
        Main.report.logPass("orderScenario work space");
    }

    public void reload(){
        Main.report.logInfo("click on reload");
        Methods.clickOn(btnReload);
        Main.report.logPass("click on reload");
    }

    public void setWorkSpace(String workSpace){
        this.workSpaces(workSpace);
        this.reload();
    }

    public String selectedWorkSpace(){
        String selectedWorkSpace = lstWorkSpaces.getAttribute("selected-workspace");
        return selectedWorkSpace;
    }

    public boolean assertionWorkSpace(String workSpace){
        Main.report.logInfo("checking if visible");
        String sWorkSpace = selectedWorkSpace();
        if (workSpace.equals(sWorkSpace)) {
            Main.report.logPass("chiecking if visible");
            return true;
        }else{
            Main.report.logFail("chiecking if visible");
            return false;
        }

    }
}
