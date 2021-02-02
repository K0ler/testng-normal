package pageObject;

import main.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Methods;




import java.awt.*;

public class NewScenario_Component extends Driver_Component{

    public NewScenario_Component(WebDriver driver) throws InterruptedException{
        super(driver);
    }

    @FindBy(id = "formService-name")
    public WebElement inpNameOfScenario;

    @FindBy(xpath = "//iframe[@title = 'Rich Text Area. Press ALT-F9 for menu. Press ALT-F10 for toolbar. Press ALT-0 for help']")
    public WebElement inpDescriptionScenario;

    @FindBy(id = "biz_contact")
    public WebElement lstBusinessConsultant;

    @FindBy(id = "tech_contact")
    public WebElement lstTechnicalConsultant;

    @FindBy(id = "formService-external-application")
    public WebElement inpExternalApplication;

    //@FindBy(xpath = "//div[@class= 'btn btn-outline-secondary dropdown-toggle']")
    @FindBy(xpath = "//span[contains(.,'Add a Tag')]")
    public WebElement lstChooseTag;

    @FindBy(xpath = "//input[@placeholder= 'Add a Tag']")
    public WebElement inpAddNewTag;

    @FindBy(id = "regular")
    public WebElement btnStandardScenario;

    @FindBy(xpath = "//div/label[@for='formService-application']/../div/ss-multiselect-dropdown/div/button")
    public WebElement lstApplication;

    @FindBy(xpath = "//select[@id='expected_usage_period']")
    public WebElement lstFrequencyType;

    @FindBy(id = "expected_usage_amount")
    public WebElement inpAmount;

    @FindBy(id = "manual_time")
    public WebElement inpManualTime;

    @FindBy(xpath = "//div/span/button/i[@class='fa fa-plus']")
    public WebElement btnplus;

    @FindBy(xpath = "//div/span/button/i[@class='fa fa-search']")
    public WebElement btnsearch;

    @FindBy(id = "implementation_time")
    public WebElement inpImplementationTime;

    @FindBy(id = "btn-create-scenario")
    public WebElement btnSave;

    @FindBy(xpath= "//div[@id='cart-panel']/section/div/div/div/div/div/app-input-search/div/input")
    public WebElement inpAddScenario;

    @FindBy(id = "cart")
    public WebElement btnBatchScenario;

    @FindBy(id = "complex")
    public WebElement btnComplexScenario;


    public void setNameOfScenario(String scenarioName){
        Main.report.logInfo("fill scenario name");
        Methods.sendText(inpNameOfScenario, scenarioName);
        Main.report.logPass("fill scenario name");
    }



    public void setDescriptionScenario(String descriptionScenario){
        Main.report.logInfo("fill scenario description");
        driver.switchTo().frame(inpDescriptionScenario);
        WebElement edit = driver.switchTo().activeElement();
        edit.sendKeys(descriptionScenario);
        Main.report.logPass("fill scenario description");
        driver.switchTo().defaultContent();
     }


    public void setBusinessConsultant(String businessConsultant){
        Main.report.logInfo("click business consultant");
        Methods.clickOn(lstBusinessConsultant);
        Main.report.logPass("click business consultant");
        Main.report.logInfo("orderScenario business consultant");
        WebElement listBusinessConsultant = driver.findElement(By.xpath("//button/ngb-highlight[contains(.,'" + businessConsultant + "')]"));
        Methods.clickOn(listBusinessConsultant);
        Main.report.logPass("orderScenario business consultant");
    }

    public void setTechnicalConsultant(String technicalConsultant){
        Main.report.logInfo("click technical consultant");
        Methods.clickOn(lstTechnicalConsultant);
        Main.report.logPass("click technical consultant");
        Main.report.logInfo("orderScenario technical consultant");
        WebElement listTechnicalConsultant = driver.findElement(By.xpath("//button/ngb-highlight[contains(.,'"+ technicalConsultant +"')]"));
        Methods.clickOn(listTechnicalConsultant);
        Main.report.logPass("orderScenario technical consultant");
    }

    public void setExternalApplication(String externalApplication){
        Main.report.logInfo("fill external application");
        Methods.sendText(inpExternalApplication,externalApplication);
        Main.report.logPass("fill external application");
    }

    public void setTag(String tag){
        Main.report.logInfo("click tag");
        Methods.clickOn(lstChooseTag);
        Main.report.logPass("click tag");
        Main.report.logInfo("orderScenario tag");
        WebElement listTag = driver.findElement(By.id(tag));
        Methods.clickOn(listTag);
        Main.report.logPass("orderScenario tag");
    }

    public void setNewTag(String newTag){
        Main.report.logInfo("set new tag");
        Methods.sendText(inpAddNewTag,newTag);
        Methods.clickOn(btnplus);
        Main.report.logPass("set new tag");
    }

    public void mainFiller(String scenarioName,String descriptionScenario,String businessConsultant,String technicalConsultant,String externalApplication,String tag,String newTag ){
        this.setNameOfScenario(scenarioName);
        this.setDescriptionScenario(descriptionScenario);
        this.setBusinessConsultant(businessConsultant);
        this.setTechnicalConsultant(technicalConsultant);
        this.setExternalApplication(externalApplication);
        this.setTag(tag);
        this.setNewTag(newTag);
    }

    public void clickStandardScenario(){
        Main.report.logInfo("click standard scenario butoon");
        Methods.clickOn(btnStandardScenario);
        Main.report.logPass("click standard scenario butoon");
    }

    public void setApplication(String application){
        Main.report.logInfo("click application");
        Methods.clickOn(lstApplication);
        Main.report.logPass("click application");
        Main.report.logInfo("orderScenario application");
        WebElement listapplication = driver.findElement(By.xpath("//div/a/span/span[contains(.,'" + application + "')]"));
        Methods.clickOn(listapplication);
        Main.report.logPass("orderScenario application");
    }

    public void setPredictedFrequency(String frequency){
        Main.report.logInfo("click predicted frequency");
        Methods.clickOn(lstFrequencyType);
        Main.report.logPass("click predicted frequency");
        Main.report.logInfo("orderScenario predicted frequency");
        WebElement lstPredictedFrequency = driver.findElement(By.xpath("//select[@id='expected_usage_period']//option[contains(.,'" + frequency + "')]"));
        Methods.clickOn(lstPredictedFrequency);
        Main.report.logPass("orderScenario predicted frequency");
    }

    public void setAmountFrequency(String amountFrequency){
        Main.report.logInfo("set amount frequency");
        Methods.sendText(inpAmount,amountFrequency);
        Main.report.logPass("set amount frequency");
    }

    public void setManualTime(String manualTime){
        Main.report.logInfo("set manual time");
        Methods.sendText(inpManualTime,manualTime);
        Main.report.logPass("set manual time");
    }

    public void setImplementationTime(String implementationTime){
        Main.report.logInfo("set implementation time");
        Methods.sendText(inpImplementationTime,implementationTime);
        Main.report.logPass("set implementation time");
    }

    public void clickSave(){
        Main.report.logInfo("click save");
        Methods.clickOn(btnSave);
        Main.report.logPass("click save");
    }

    public void standardScenarioFiller(String application,String frequency,String amountFrequency,String manualTime,String implementationtime){
        this.clickStandardScenario();
        this.setApplication(application);
        this.setPredictedFrequency(frequency);
        this.setAmountFrequency(amountFrequency);
        this.setManualTime(manualTime);
        this.setImplementationTime(implementationtime);
        this.clickSave();
    }

    public void chooseNameOfScenario(String batchScenarioName){
        Main.report.logInfo("orderScenario name scenario in batch scenario");
        Methods.sendText(inpAddScenario,batchScenarioName);
        Main.report.logPass("orderScenario name scenario in batch scenario");
        Main.report.logInfo("click on search");
        inpAddScenario.sendKeys(Keys.ENTER);
        Main.report.logPass("click on search");
        Main.report.logInfo("click on name sceanrio in batch scenario");
        WebElement lstNameOfScenario = driver.findElement(By.xpath("//div[@id='cart-panel']//span[contains(.,'" + batchScenarioName + "')]"));
        Methods.clickOn(lstNameOfScenario);
        Main.report.logPass("click on name scenario in batch scenario");
    }

    public void clickBatchScenario(){
        Main.report.logInfo("click batch scenario");
        Methods.clickOn(btnBatchScenario);
        Main.report.logPass("click batch scenario");
    }

    public void batchScenarioFiller(String batchScenarioName){
        this.clickBatchScenario();
        this.chooseNameOfScenario(batchScenarioName);
        this.clickSave();

    }

    public void clickComplexScenartio(){
        Main.report.logInfo("click complex scenario");
        Methods.clickOn(btnComplexScenario);
        Main.report.logPass("click complex scenario");
    }

    public void complexScenario(){
        this.clickComplexScenartio();
        this.clickSave();
    }


}
