package pageObject;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Methods;
import main.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import utils.SyncMethods;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;



import static utils.Methods.isVisible1;

public class MainPage_Component extends Driver_Component {

    public MainPage_Component(WebDriver driver) throws InterruptedException{
        super(driver);
    }

    Actions action = new Actions(driver);

    @FindBy(xpath = "//button[contains(.,'New Process')]")
    public WebElement btnNewScenario ;

    private By whichFillter(String fillter) {
        return new By.ByXPath("//app-list-filters/div/div/div/span[contains(text(),'" + fillter + "')]");
    }

    private By whichOption(String option) {
        return new By.ByXPath("//app-order-list/section/div/div/app-list-filters/div/div/div/div/div/label[contains(.,'" + option + "')]");
    }

    private By whichButton(String option) {
        return new By.ByXPath("//app-list-filters/div/div/div/button[contains(text(),'" + option + "')]");
    }

    @FindBy(xpath ="//app-order-list//app-pagination-controls[@id='pagination-controls']")
    public WebElement pageNumberUnderOrders;

    private By orderOnPage(int x) {
        return new By.ByXPath("//tbody/tr[" + x + "]");
    }

    @FindBy(xpath = "//tbody/tr")
    public WebElement oneOrder;

    @FindBy(xpath = "//div/h3")
    public WebElement emptyField;

    @FindBy(xpath = "//ng-component/div/div[2]//app-order-list/section//app-pagination-controls/div/div[1]//span[@class='pagination-next']/a")
    public WebElement txtNextButtonUnderOrders;

    @FindBy(xpath = "//app-date-filters/div")
    public WebElement btnDate;

    @FindBy (xpath = "//app-alert/section[@class = 'alert']")
    public WebElement alert;

    @FindBy (xpath = "//a[@class='nav-link text-truncate dropdown-toggle']")
    WebElement btnProfile;

    @FindBy (xpath = "//app-directories[1]/section/div/div/app-catalog/div")
    WebElement btnAllCatalog;

    @FindBy(xpath = "//div[@class='input-group input-group-sm']/input")
    public WebElement inpSearchCatalog;

    @FindBy(xpath = "//div[@class='input-group input-group-sm']/span/button")
    public WebElement btnLoupeImage_SearchCatalog;
    
    @FindBy(xpath = "//*[@id=\"1\"]")
    public WebElement btnSheduleList;



    public boolean isNewScenarioVisible(){
        Main.report.logInfo("Log correct");
        boolean visible = isVisible1(btnNewScenario);
        Main.report.logPass("Log correct");
        return visible;
    }
    public void newScenario() {
        Main.report.logInfo("Click new scenario.");
        Methods.clickOn(btnNewScenario);
        Main.report.logPass("Click new scenario.");
    }
    public void chooseFillter(String filter) {
        Main.report.logInfo("Try to orderScenario filter.");
        WebElement element = driver.findElement(whichFillter(filter));
        Methods.clickOn(element);
        Main.report.logPass("Fillter chosen. " + filter);
    }

    public void clickOnOption(String option) {
        Main.report.logInfo("Try to orderScenario filter's option. ");
        WebElement element = driver.findElement(whichOption(option));
        Methods.clickOn(element);
        Main.report.logPass("Filter's option chosen. " + option);
    }

    public void clickOnButton(String option) {
        Main.report.logInfo("Try to orderScenario filter's option.");
        WebElement element = driver.findElement(whichButton(option));
        Methods.clickOn(element);
        Main.report.logPass("Filter's option chosen. " + option);
    }

    public void chooseOption(String filter, String option) {
        if (filter.equalsIgnoreCase("Created by") || filter.equalsIgnoreCase("Status") || filter.equalsIgnoreCase("Application") || filter.equalsIgnoreCase("Environment")) {
            this.clickOnOption(option);
        } else {
            this.clickOnButton(option);
        }
    }

    public void chooseFilter(String filter, String option) {
        this.chooseFillter(filter);
        this.chooseOption(filter, option);
        Methods.clickOn(emptyField);
    }

    public void returnGoUp(int j) {
        if(j == 10) {
            clickNextUnderOrders();
            goUpPage();
        }
    }

    public String whoLogged() {
        Main.report.logInfo("Try to get name of logged user.");
        WebElement logged = driver.findElement(By.xpath("//div[@id='navbarSupportedContent']/ul[2]/li/a"));
        String who = logged.getText();
        Main.report.logPass("Name got correctly. " + who);
        return who;
    }

    public void clickNextUnderOrders() {
        try {
            Methods.clickOn(txtNextButtonUnderOrders);
        } catch (Exception e) {
            //ToDO
        }
        Main.report.logPass("'Next' clicked.");
    }

    public int getNumberOfPagesUnderOrders() {
        Main.report.logInfo("Check number of pages correct.");
        SyncMethods.manualProcessSlowdownDefaultVal();
        int currentPageNum = Integer.parseInt(pageNumberUnderOrders.getAttribute("total-pages"));
        return currentPageNum;
    }

    public int howManyOrdersOnPage() {
        List<WebElement> orders = driver.findElements(By.xpath("//tbody/tr"));
        int numberOrdersOnPage = orders.size();
        Main.report.logInfo("Number of orders on page = " + numberOrdersOnPage);
        return numberOrdersOnPage;
    }

    public void goUpPage() {
        WebElement bodyPage = driver.findElement(By.id("app"));
        action.moveToElement(bodyPage);
        action.perform();
        SyncMethods.manualProcessSlowdownDefaultVal();
    }

    public String blabla(String option){
        String optionEde;
        if(option.equalsIgnoreCase("Created by me")) {
            optionEde = whoLogged();
        } else {
            optionEde = option;
        }
        return optionEde;
    }


    public boolean checkFilter(String filter, String option) {
        Main.report.logInfo("Try to filter by [" + filter + "] and [" + option + "].");
        String optionEd = blabla(option);
        if(isVisible1(oneOrder)) {
            int pages = getNumberOfPagesUnderOrders();
            for (int i = 1; i <= pages; i++) {
                int orders = howManyOrdersOnPage();
                for (int j = 1; j <= orders; j++) {
                    WebElement order = driver.findElement(orderOnPage(j));
                    switch (filter) {
                        case "Created by":
//                                String optionEd;
//                                if (option.equals("Created by me")) {
//                                    optionEd = whoLogged();
//                                } else {
//                                    optionEd = option;
//                                }
                            Methods.clickOn(order);
                            String whoOrder = driver.findElement(By.xpath("//tbody/tr[" + j + "]/..//tr[" + (j + 1) + "]/td/div/div/section[1]/div/aside[3]/span[3]")).getText();
                            if (whoOrder.contains(optionEd) || optionEd.contains(whoOrder)) {
                                Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                            } else {
                                Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected " + option + " but found " + whoOrder);
                                return false;
                            }
                            Methods.clickOn(order);
                            returnGoUp(j);
                            break;
                        //-----------------------------------------------------------------------------------------------------------
                        case "Status":
                            String status = driver.findElement(By.xpath("//tbody/tr[" + j + "]/td[4]/ngb-progressbar/div/div[@role='progressbar']")).getText();
                            if (status.contains(status)) {
                                Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                            } else {
                                Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected " + option + " but found " + status);
                                return false;
                            }
                            returnGoUp(j);
                            break;
                        //-----------------------------------------------------------------------------------------------------------
                        case "Scenario Tags":
                            Main.report.logFail("Filter doesn't work.");
                            break;
                        //-----------------------------------------------------------------------------------------------------------
                        case "Type":
                            switch (option) {
                                case "Standard Scenario":
                                    WebElement standard = driver.findElement(By.xpath("//tbody/tr[" + j + "]//i[@class='fa fa-file-o']"));
                                    String title = standard.getAttribute("title");
                                    if (title.contains(option)) {
                                        Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                                    } else {
                                        Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected " + option + " but found " + title);
                                        return false;
                                    }
                                    returnGoUp(j);
                                    break;
                                case "Batch Scenario":
                                    WebElement batch = driver.findElement(By.xpath("//table//tbody/tr[" + j + "]//i[@class='fa fa-shopping-cart']"));
                                    String titleBat = batch.getAttribute("title");
                                    if (titleBat.contains(option)) {
                                        Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                                    } else {
                                        Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected " + option + " but found " + titleBat);
                                        return false;
                                    }
                                    returnGoUp(j);
                                    break;
                                case "Part of Batch Scenario":
                                    WebElement part = driver.findElement(By.xpath("//table//tbody/tr[" + j + "]/td//i[@class='fa fa-shopping-basket']"));
                                    String titlePart = part.getAttribute("title");
                                    if (titlePart.contains(option)) {
                                        Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                                    } else {
                                        Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected " + option + " but found " + titlePart);
                                        return false;
                                    }
                                    returnGoUp(j);
                                    break;
                                case "Complex Scenario":
                                    WebElement complex = driver.findElement(By.xpath("//table//tbody/tr[" + j + "]/td//i[@class='fa fa-object-group']"));
                                    String titleCom = complex.getAttribute("title");
                                    if (titleCom.contains(option)) {
                                        Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                                    } else {
                                        Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected " + option + " but found " + titleCom);
                                        return false;
                                    }
                                    returnGoUp(j);
                                    break;
                                case "Part of Complex Scenario":
                                    WebElement partCom = driver.findElement(By.xpath("//table//tbody/tr[" + j + "]/td//i[@title='Part of Complex Scenario']"));
                                    String titleComPart = partCom.getAttribute("title");
                                    if (titleComPart.contains(option)) {
                                        Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                                    } else {
                                        Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected " + option + " but found " + titleComPart);
                                        return false;
                                    }
                                    returnGoUp(j);
                                    break;
                            } break;
                        //-----------------------------------------------------------------------------------------------------------
                        case "Scheduled":
                            boolean visible = isVisible1(driver.findElement(By.xpath("//tbody/tr[" + j + "]/td/i[@title='Recurring Job']")));
                            if (option.equalsIgnoreCase("Scheduled")) {
                                if (visible) {
                                    Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                                } else {
                                    Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected 'Recurring Job' but found 'Unscheduled Job'.");
                                    return false;
                                }
                            } else {
                                if (!visible) {
                                    Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                                } else {
                                    Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected 'Unscheduled' but found 'Recurring Job'.");
                                    return false;
                                }
                            }
                            returnGoUp(j);
                            break;
                        //-----------------------------------------------------------------------------------------------------------
                        case "Bot version":
                            Methods.clickOn(order);
                            String botVersion = driver.findElement(By.xpath("//tbody/tr[" + j + "]/..//tr[" + (j + 1) + "]/td/div/div/section[1]/div/aside[3]/span[1]")).getText().toLowerCase();
                            if (option.contains(botVersion)) {
                                Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                            } else {
                                Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected " + option + " but found " + botVersion);
                            }
                            Methods.clickOn(order);
                            returnGoUp(j);
                            break;
                        //-----------------------------------------------------------------------------------------------------------
                        case "Application":
                            Methods.clickOn(order);
                            List<WebElement> applications = driver.findElements(By.xpath("//div[@class='card-body']//label[contains(.,'Applications')]/..//span"));
                            for (WebElement a : applications) {
                                String apl = a.getText();
                                if (apl.contains(option)) {
                                    Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                                } else {
                                    Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected value: " + option + " but found " + apl);
                                    return false;
                                }
                            }
                            Methods.clickOn(order);
                            returnGoUp(j);
                            break;
                        //-----------------------------------------------------------------------------------------------------------
                        case "Bots":
                            String bot = driver.findElement(By.xpath("//tbody/tr[" + j + "]/td[7]/span")).getText();
                            if (option.contains(bot)) {
                                Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                            } else {
                                Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected value: " + option + " but found " + bot);
                                return false;
                            }
                            returnGoUp(j);
                            break;
                        //-----------------------------------------------------------------------------------------------------------
                        case "Environment":
                            String environment = driver.findElement(By.xpath("//tr[" + j + "]/td[6]/span")).getText();
                            if (option.contains(environment)) {
                                Main.report.logPass("Element[" + j + "] page[" + i + "] filtered correct.");
                            } else {
                                Main.report.logFail("Element[" + j + "] page[" + i + "] filtered wrong. Expected value: " + option + " but found " + environment);
                                return false;
                            }
                            returnGoUp(j);
                            break;
                    }
                }
            }
        } else {
            Main.report.logFail("Empty list. Nothing for filter.");
            return false;
        }
        return true;
    }

    public boolean fillteredByDate(String dateFrom, String dateTo, String timeHoursFrom, String timeMinutesFrom, String timeHoursTo, String timeMinutesTo) throws ParseException {
        Methods.clickOn(btnDate);
        WebElement fromDate = driver.findElement(By.xpath("//app-date-filters/div/div/div[2]/div[1]/input[1]"));
        fromDate.sendKeys(dateFrom);
        WebElement toDate = driver.findElement(By.xpath("//app-date-filters/div/div/div[2]/div[1]/input[2]"));
        toDate.sendKeys(dateTo);
        WebElement fromTimeHours = driver.findElement(By.xpath("//ngb-timepicker[1]//input[@aria-label='Hours']"));
        fromTimeHours.clear();
        fromTimeHours.sendKeys(timeHoursFrom);
        WebElement fromTimeMinutes = driver.findElement(By.xpath("//ngb-timepicker[1]//input[@aria-label='Minutes']"));
        fromTimeMinutes.clear();
        fromTimeMinutes.sendKeys(timeMinutesFrom);
        WebElement toTimeHours = driver.findElement(By.xpath("//ngb-timepicker[2]//input[@aria-label='Hours']"));
        toTimeHours.clear();
        toTimeHours.sendKeys(timeHoursTo);
        WebElement toTimeMinutes = driver.findElement(By.xpath("//ngb-timepicker[2]//input[@aria-label='Minutes']"));
        toTimeMinutes.clear();
        toTimeMinutes.sendKeys(timeMinutesTo);
        Methods.clickOn(btnDate);
        String dateFrom1 = driver.findElement(By.xpath("//app-date-filters/div/div/div[1]/span[1]")).getText().substring(5);
        String dateTo1 = driver.findElement(By.xpath("//app-date-filters/div/div/div[1]/span[3]")).getText().substring(3);

        DateFormat format1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        java.util.Date dateFromFilltered = format1.parse(dateFrom1);
        java.util.Date dateToFilltered = format1.parse(dateTo1);

        if(isVisible1(oneOrder)) {
            int pagesAfterFilltered = getNumberOfPagesUnderOrders();
            for (int i = 1; i <= pagesAfterFilltered; i++) {
                int ordersOnPage = howManyOrdersOnPage();
                for (int j = 1; j <= ordersOnPage; j++) {
                    String dateOrder1 = driver.findElement(By.xpath("//tbody/tr[" + j + "]/td[3]")).getText();
                    java.util.Date dateOrder = format1.parse(dateOrder1);
                    if (dateOrder.after(dateFromFilltered) && dateOrder.before(dateToFilltered)) {
                        Main.report.logPass("Element[" + j + "] page[" + i + "] filltered correct.");
                    } else {
                        Main.report.logFail("Element[" + j + "] page[" + i + "] filltered wrong. Expected: date from " + dateFrom + " date to " + dateTo + " time from " + timeHoursFrom + ":" + timeMinutesFrom + " time to " + timeHoursTo + ":" + timeMinutesTo);
                        return false;
                    }
                    returnGoUp(j);
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean isAlertVisible(){
        Main.report.logInfo("Checking alert's visibility after ordered the scenario.");
        if (Methods.isVisible1(alert)){
            Main.report.logPass("Alert visible.");
            return true;
        } else {
            Main.report.logPass("Alert not visible");
            return false;
        }
    }

    public boolean isCreatedCorrectly() {
        Main.report.logInfo("Checking if create correctly");
        //WebElement success = Methods.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-alert//div[@class = 'alert-content row success']//h3[contains(.,'Success')]")));
        WebElement success = driver.findElement(By.xpath("//app-alert//div[@class = 'alert-content row success']//h3[contains(.,'Success')]"));
        Main.report.logPass("Checking if create correctly");
        String message = driver.findElement(By.xpath("//app-alert//div[@class = 'alert-content row success']//div[@class = 'col-9']//p[@class = 'message']")).getText();
        if (Methods.isVisible1(success)) {
            Main.report.logPass(message);
            return true;
        } else {
            Main.report.logFail("isn't create");
            return false;
        }

    }
    public void profile(){
        Main.report.logInfo("click on profile");
        Methods.clickOn(btnProfile);
        Main.report.logPass("click on profile");
    }

    public void setCatalogName_SearchCatalog(String catalogName) {
        Main.report.logInfo("Enter catalog name: "+catalogName+"' to element 'Search catalog'");
        Methods.sendText(inpSearchCatalog, catalogName);
        Main.report.logPass("Catalog named: '"+catalogName+"' was entered to element 'Search catalog'");
    }

    public void clickButtonLoupeImage_SearchCatalog() {
        Main.report.logInfo("Click button 'Loupe Image' for search catalog");
        Methods.clickOn(btnLoupeImage_SearchCatalog);
        Main.report.logPass("Button 'Loupe Image' for search catalog was clicked");
    }


    //-----MATEUSZ------------

    public void clickOnCatalog(String catalogName){
        Main.report.logInfo("try to click on All catalog");
        WebElement catalogToClick = driver.findElement(By.xpath("//app-catalog//a[contains(text(),'"+catalogName+"')]"));
        Methods.clickOn(catalogToClick);
        Main.report.logPass(catalogName + " clicked.");
    }

    public void orderScenario(String catalogName){
        setCatalogName_SearchCatalog(catalogName);
        clickButtonLoupeImage_SearchCatalog();
        clickOnCatalog(catalogName);
    }

    public void clickNavOption(String option){
        WebElement chooseOpition = driver.findElement(By.xpath("//span[text()='"+option+"']"));
        chooseOpition.click();
    }
    
    //-------Kamil--------------
    
    public void clickOnScheduleList() {
    
    	Main.report.logInfo("try to click on Schedule List");
    	Methods.clickOn(btnSheduleList);
    	Main.report.logPass("Schedule List clicked.");
    	
    }
    
    public boolean resumeOrderOnScheaduleList (String orderId) throws InterruptedException {
		
    	Main.report.logInfo("search for " + orderId + " on Scheadule List");
    	
    	selectNumberOfElementsInPage(100);
    	
    	WebElement list = null;
    	
    	try {
     	
    	list = driver.findElement(By.xpath("//section[@id='"+ orderId +"']"));
    	
    	}
    	catch (NoSuchElementException e)
    	{
    		Main.report.logError("Order is not visable on list");
    		return false;
    	}
    	String status = driver.findElement(By.xpath("//section[@id='"+ orderId +"']/app-periodic-order/div/div/div[6]/ngb-progressbar/div/div")).getText();
    	
    	if(status.equals("Paused"))
    	{
    		Methods.clickOn(list);
 
    		System.out.println(list.getText());
    		WebElement btnPause = driver.findElement(By.xpath("//section[@id='" + orderId + "']/app-periodic-order/div[2]/div[2]/button"));
    		Methods.ClickOnWithJSExecutor(btnPause);

    		Thread.sleep(3000);

    		status = driver.findElement(By.xpath("//section[@id='"+ orderId +"']/app-periodic-order/div/div/div[6]/ngb-progressbar/div/div")).getText();
        	if(!status.equals("Paused"))
    		{
    			Main.report.logPass("Status changed");
    			return true;
    		}
    		else
    		{
    			Main.report.logError("Status is not Paused!");
    			return false;
    		}
    	}
    	else
    	{
    		Main.report.logError("Order is in status: " + status + "");
    		return false;
    	}
    	
    	
    }
    
public boolean pauseOrderOnScheaduleList (String orderId) throws InterruptedException {
		
    	Main.report.logInfo("search for " + orderId + " on Scheadule List");
    	
    	selectNumberOfElementsInPage(100);
    	
    	WebElement list = null;
    	
    	try {
     	
    	list = driver.findElement(By.xpath("//section[@id='"+ orderId +"']"));
    	
    	}
    	catch (NoSuchElementException e)
    	{
    		Main.report.logError("Order is not visable on list");
    		return false;
    	}
    	
    	String status = driver.findElement(By.xpath("//section[@id='"+ orderId +"']/app-periodic-order/div/div/div[6]/ngb-progressbar/div/div")).getText();
    	
    	
    	selectNumberOfElementsInPage(100);
    	
    	if(!status.equals("Paused"))
    	{
    		
			Methods.clickOn(list);
 
    		System.out.println(list.getText());
    		WebElement btnPause = driver.findElement(By.xpath("//section[@id='" + orderId + "']/app-periodic-order/div[2]/div[2]/button"));
    		Methods.ClickOnWithJSExecutor(btnPause);

    		Thread.sleep(3000);

    		status = driver.findElement(By.xpath("//section[@id='"+ orderId +"']/app-periodic-order/div/div/div[6]/ngb-progressbar/div/div")).getText();
        	if(status.equals("Paused"))
    		{
    			Main.report.logPass("Status changed");
    			return true;
    		}
    		else
    		{
    			Main.report.logError("Status is Paused!");
    			return false;
    		}
    	}
    	else
    	{
    		Main.report.logError("Order is in status: " + status + "");
    		return false;
    	}
    	
    	
    }

public boolean selectNumberOfElementsInPage(int Number) {
	
	Select eleNumber = new Select(driver.findElement(By.xpath("/html/body/app-root/main/ng-component/div/div[2]/div[2]/ngb-tabset/div/div[2]/app-periodic-list/section/app-pagination-controls/div/div[1]/select")));
	
	try {
	eleNumber.selectByValue(Integer.toString(Number));
	}
	catch (Exception e)
	{
		return false;
	}
	return true;
	
}
    
    


}
