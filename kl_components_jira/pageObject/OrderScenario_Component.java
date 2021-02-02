package pageObject;

import main.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static main.Main.report;
import static utils.Methods.clickOn;
import static utils.Methods.sendText;

public class OrderScenario_Component extends Driver_Component{

    public OrderScenario_Component(WebDriver driver) throws InterruptedException{
        super (driver);
    }

    MainPage_Component mainPageComponent = new MainPage_Component(driver);

    //-----FIELDS---------------------------------

    @FindBy (id = "service-bot-version")
    WebElement lstVersion;

    @FindBy (id = "service-bot-version-branch")
    WebElement lstBranch;

    @FindBy (id = "service-bot-environment")
    WebElement lstEnvironment;

    @FindBy (xpath = "//button[contains(text(),'Create Job')]")
    WebElement btnOrder;

    @FindBy (xpath = "//button[contains(text(),'YES')]")
    WebElement btnConfirmOrder;

    @FindBy (id = "input-date-start")
    WebElement inpStartDate;

    @FindBy (xpath = "//ngb-timepicker/fieldset/div/div[1]/input")
    WebElement inpStartHour;

    @FindBy (xpath = "//ngb-timepicker/fieldset/div//input[@aria-label = 'Minutes']")
    WebElement inpStartMinute;

    @FindBy (id = "run")
    WebElement lstRun;

    @FindBy (xpath = "//app-scheduler/div/section/div[3]/label/input")
    WebElement radFinalDate;

    @FindBy (id = "input-date-end")
    WebElement inpFinalDate;

    @FindBy (xpath = "//div[1]/div[2]/ngb-timepicker/fieldset/div/div[1]/input")
    WebElement inpEndHour;

    @FindBy (xpath = "//div[1]/div[2]/ngb-timepicker/fieldset/div/div[3]/input")
    WebElement inpEndMinute;

    @FindBy (xpath = "//div[4]/label/input")
    WebElement radNumberOfStarts;

    @FindBy (id = "rrule-count")
    WebElement inpNumberOfStarts;




    //-----METHODS-------------------------------

    public void chooseVersion(String version){
        report.logInfo("Try to click on version.");
        clickOn(lstVersion);
        WebElement versionToClick = driver.findElement(By.xpath("//*[@id='service-bot-version']/option[contains(text(),'" + version + "')]"));
        clickOn(versionToClick);
        report.logPass("Choosen version: " + version);
    }

    public void chooseBranch(String branch){
        report.logInfo("Try to click on branch.");
        clickOn(lstBranch);
        WebElement branchToClick = driver.findElement(By.xpath("//*[@id='service-bot-version-branch']/option[contains(text(),'" + branch + "')]"));
        clickOn(branchToClick);
        report.logPass("Choosen branch: " + branch);
    }

    public void chooseEnvironment(String environment){
        report.logInfo("Try to click on environment.");
        clickOn(lstEnvironment);
        WebElement environmentToClick = driver.findElement(By.xpath("//*[@id='service-bot-environment']/option[contains(text(),'" + environment + "')]"));
        clickOn(environmentToClick);
        report.logPass("Choosen environment: " + environment);
    }

    public void clickOrder(){
        report.logInfo("Try to click 'Order'");
        clickOn(btnOrder);
        report.logPass("'Order' clicked.");
    }

    public void clickConfirmOrder(){
        report.logInfo("Try to confirm order");
        clickOn(btnConfirmOrder);
        report.logPass("Confirmed.");
    }

    public void orderScenarioPart1(String version, String branch, String environment) {
        if ((version.equalsIgnoreCase("development"))|| (version.equalsIgnoreCase("deweloperska"))) {
            chooseVersion(version);
            chooseBranch(branch);
            chooseEnvironment(environment);
        } else {
            chooseVersion(version);
            chooseEnvironment(environment);

        }
    }

    public void orderScenario(String version, String branch, String environment){
        orderScenarioPart1(version, branch, environment);
        clickOrder();
        clickConfirmOrder();
        mainPageComponent.isCreatedCorrectly();
    }

    public void setStartDate(String startDate){
        Main.report.logInfo("Try to set start date.");
        sendText(inpStartDate, startDate);
        Main.report.logPass("Start date set.");
    }

    public void setStartTime(String startHour, String startMinute){
        Main.report.logInfo("Try to set start time.");
        sendText(inpStartHour, startHour);
        sendText(inpStartMinute, startMinute);
        Main.report.logPass("Start time set.");
    }

    public void setRun(String run){
        Main.report.logInfo("Try to set run.");
        clickOn(lstRun);
        WebElement howOftenRun = driver.findElement(By.xpath("//select[@id = 'run']/option[contains(text(),'" + run + "')]"));
        clickOn(howOftenRun);
        Main.report.logPass("Run set: " + run);
    }

    public void setFinalDate(String finalDate, String endHour, String endMinute){
        Main.report.logInfo("Try to set 'Final date'.");
        clickOn(radFinalDate);
        sendText(inpFinalDate, finalDate);
        sendText(inpEndHour, endHour);
        sendText(inpEndMinute, endMinute);
        Main.report.logPass("Final date sets: " + finalDate + " " + endHour + "HH:" + endMinute + "MM");
    }

    public void setNumberOfStarts(String numberOfStarts){
        Main.report.logInfo("Try to set 'Number of starts'.");
        clickOn(radNumberOfStarts);
        sendText(inpNumberOfStarts, numberOfStarts);
        Main.report.logPass("Number of start sets: " + numberOfStarts);
    }



    public void setRecurrenceHowOftenRun(String run, String month, String daysOfMonth, String days, String hours, String minutes) {
        switch (run) {
            case "Yearly":
                WebElement inMonths = driver.findElement(By.xpath("//select[@id = 'rrule-by-month']//option[contains(text(),'"+month+"')]"));
                clickOn(inMonths);
            case "Monthly":
                WebElement inDaysOfMonths = driver.findElement(By.xpath("//select[@id = 'rrule-by-month-day']//option[contains(text(),'"+daysOfMonth+"')]"));
                clickOn(inDaysOfMonths);
            case "Weekly":
                WebElement inDays = driver.findElement(By.xpath("//select[@id = 'rrule-by-day']//option[contains(text(),'" + days + "')]"));
                clickOn(inDays);
            case "Daily":
                WebElement inHours = driver.findElement(By.xpath("//select[@id = 'rrule-by-hour']//option[contains(text(),'" + hours + "')]"));
                clickOn(inHours);
            case "Hourly":
                WebElement inMinutes = driver.findElement(By.xpath("//select[@id = 'rrule-by-minute']//option[contains(text(),'" + minutes + "')]"));
                clickOn(inMinutes);
            case "Once":
                break;
        }
    }

    public void setReccurenceWhenEnd(String run, String whenEnd, String finalDate, String endHour, String endMinute, String numberOfStarts){
        if (run != "Once") {
            switch (whenEnd) {
                case "None":
                    break;
                case "Final date":
                    setFinalDate(finalDate, endHour, endMinute);
                    break;
                case "Number of starts":
                    setNumberOfStarts(numberOfStarts);
                    break;
            }
        }
    }

    public void orderCyclicScenario(String version, String branch, String environment, String run, String month, String daysOfMonth, String days, String hours, String minutes, String whenEnd, String finalDate, String endHour, String endMinute, String numberOfStarts, String startDate, String startHour, String startMinute){
        orderScenarioPart1(version, branch, environment);
        setStartDate(startDate);
        setStartTime(startHour, startMinute);
        setRun(run);
        setRecurrenceHowOftenRun(run, month, daysOfMonth, days, hours, minutes);
        setReccurenceWhenEnd(run, whenEnd, finalDate, endHour, endMinute, numberOfStarts);
        clickOrder();
        clickConfirmOrder();
        mainPageComponent.isCreatedCorrectly();


    }
}
