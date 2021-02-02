package pageObject;

import main.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Methods;

import static utils.Methods.clickOn;

public class AllCatalog_Component extends Driver_Component {

    public AllCatalog_Component(WebDriver driver) throws InterruptedException{
        super (driver);
    }

    MainPage_Component main = new MainPage_Component(driver);

    //-----FIELDS-----------------------------

    @FindBy (xpath = "//div[@class='input-group input-group-sm mb-3']/input")
    WebElement inpSearchScenario;

    @FindBy (xpath = "//app-tree-catalog/div[1]/span/button")
    public WebElement btnLoupeImage_SearchCatalog;

    @FindBy (xpath = "//app-service-view/div/section/button[1]")
    WebElement btnGoToOrder;

    @FindBy (xpath = "//app-service-view/div/section/button[2]")
    WebElement btnGoToCyclicOrder;



    //-----METHODS----------------------------

    public void setScenarioName_SearchScenario(String scenarioName) throws InterruptedException {
        Main.report.logInfo("Enter catalog name: "+scenarioName+"' to element 'Search catalog'");
        Thread.sleep(5000);//Wiem, jest sleep, ale kurna raz mi test przechodzi tutaj a raz nie, zaczęło się wysypywać w piątek o 15, nie dałem rady poprawić, PRZEPRASZAM.
        Methods.sendText(inpSearchScenario, scenarioName);
        Main.report.logPass("Catalog named: '"+scenarioName+"' was entered to element 'Search catalog'");
    }

    public void clickButtonLoupeImage_SearchCatalog() {
        Main.report.logInfo("Click button 'Loupe Image' for search catalog");
        clickOn(btnLoupeImage_SearchCatalog);
        Main.report.logPass("Button 'Loupe Image' for search catalog was clicked");
    }

    public void clickOnScenario(String scenarioName){
        Main.report.logInfo("Try to click on scenario.");
        WebElement scenario = driver.findElement(By.xpath("//span[contains(text(),'" + scenarioName + "')]"));
        clickOn(scenario);
        Main.report.logPass("Scenario " + scenarioName + " clicked.");
    }

    public void clickGoToOrder(){
        Main.report.logInfo("Try to click 'Go to order'.");
        clickOn(btnGoToOrder);
        Main.report.logPass("'Go to order clicked'.");
    }

    public void clickGoToCyclicOrder(){
        Main.report.logInfo("Try to click 'Go to cyclic order'.");
        clickOn(btnGoToCyclicOrder);
        Main.report.logPass("'Go to cyclic order' clicked.");
    }

    public void searchScenario(String scenarioName) throws InterruptedException {
        setScenarioName_SearchScenario(scenarioName);
        clickButtonLoupeImage_SearchCatalog();
        clickOnScenario(scenarioName);
    }

    public void searchAndClickOrder(String scenarioName) throws InterruptedException {
        searchScenario(scenarioName);
        clickGoToOrder();
    }

    public void searchAndClickOrderCyclic(String scenarioName) throws InterruptedException {
        searchScenario(scenarioName);
        clickGoToCyclicOrder();
    }

}
