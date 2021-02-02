package main;


import java.io.IOException;

import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.LogStatus;



@Listeners ({ConfigurationListener.class})
public abstract class Main {
	
	// pola klasy
		public static WebDriver driver;
		public static WebDriverWait wait;
		public static int waitForElement = 60;
		public static Parameters parameters;
		public final static String reportName = "Report.html";
		public static Reporter report = new Reporter(System.getProperty("user.dir")+ System.getProperty("file.separator")+"result"+System.getProperty("file.separator")+"attachments"+System.getProperty("file.separator")+reportName);
		public static String driverPath;
		public static String workspacePath =
				(System.getProperty("user.dir").toLowerCase().contains("workspace")
				? System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("workspace") + "workspace".length())
				: System.getProperty("user.dir")
				) + System.getProperty("file.separator");
		public String finalMsg = "";
		public String screenShotFileName = "";
		
		
	//inicjalizacja suity
		@BeforeSuite
		public void tearUp() throws JSONException, IOException {
		// odczyt parametrów przekazanych z PFa
			parameters = new Parameters(workspacePath);
			parameters.readParameters();
		// tworzenie obiektu raportowego
			report = new Reporter(workspacePath + "result" + System.getProperty("file.separator") + "attachments" + System.getProperty("file.separator") + reportName);
			report.loadConfig("C:\\lib\\extent-config.xml");
			report.startTest(parameters.getServiceName());
		}
		
		
	// raportowanie po każdej metodzie z @Test
		@AfterMethod(alwaysRun = true)
		public void afterMethod(ITestResult result) throws IOException {
			if (result.getStatus() == ITestResult.FAILURE) {
				report.exceptionHandler(result.getThrowable());
				if (screenShotFileName != null && !screenShotFileName.isEmpty() && !screenShotFileName.isEmpty())
					report.logScreenShot(screenShotFileName, LogStatus.FAIL);
				PfUtils.getWebScreenShot(driver);
				parameters.setResult("failure", (finalMsg != null && !finalMsg.isEmpty()) ? report.clearHtml(finalMsg) :
						"Usługa zakończona ze statusem FAIL: szczegóły w zakładce 'Informacje techniczne'",		// "The service has been completed with status FAIL: more information in Technical information tab\n"
						result.getThrowable().toString(), null , null);
			}
		}
		
	
	// zakończenie suity
		@AfterSuite
		public void tearDown(ITestContext context) throws JSONException, IOException {
			String msg = (finalMsg == null || finalMsg.isEmpty()) ? "" : finalMsg.replaceAll("\r\n|\n", "<br>") + "<br>";
			if (context.getFailedTests().size() == 0 && context.getPassedTests().size() > 0)
				parameters.setResult("success", msg + "Usługa zakończona ze statusem PASS: szczegóły w bloku 'Informacje techniczne'\n" + Reporter.GetAllListResultRecords(),		// "The service has been completed with status PASS: more information in Technical information tab\n"
						null, null, null);
			report.generateRaport();
			if (driver !=null)
				driver.quit();
			report.closeRaport();
		}
		
}