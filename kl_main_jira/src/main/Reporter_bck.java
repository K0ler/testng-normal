package main;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


import utils.Methods;

/**
 * Raportowanie customowe przebiegu Uslugi.
 */
public class Reporter_bck {

	public  ExtentReports extent;
	public  ExtentTest reporter;
	


	/** 
	 * KONSTRUKTOR
	 * @param reportPathFileName sciezka z nazwa, gdzie zapisany ma byc plik raportu
	 */	
	public Reporter_bck(String reportPathFileName) {
		
		this.extent = new ExtentReports(reportPathFileName);		
	//	this.extent = new ExtentReports(reportPathFileName, NetworkMode.OFFLINE);			
		
	}
	
	/**
	 * Utworzenie nowego wpisu w strukturze raportowej.
	 * @param testName nazwa testu
	 */	
	public void startTest(String testName) {
		
		reporter = this.extent.startTest(testName);
				
	}
	
	
	
	/**
	 * Dodanie wpisu informacyjnego.
	 * @param msg tresc wpisu
	 */
	public void logInfo(String msg) {
		 
		reporter.log(LogStatus.INFO, msg);
		writeTechnicalLog("[INFO] :"+msg);	
	}
	
	/**
	 * Dodanie statusu pozytywnego.
	 * @param msg tresc wpisu
	 */
	public void logPass(String msg) {
		
		reporter.log(LogStatus.PASS, msg);
		writeTechnicalLog("[PASS] :"+msg);
	}
	
	/**
	 * Dodanie statusu negatywnego.
	 * @param msg tresc wpisu
	 */
	public void logFail(String msg) {
		
		writeTechnicalLog("[FAIL] :"+msg);
		reporter.log(LogStatus.FAIL, msg);
		
	}
	
	
	/**
	 * Dodanie zrzutu ekranowego do raportu.
	 * @param imagePath Sciezka z nazwa pliku z zrzutem ekranowym
	 */
	public void logScreenShot(String imagePath) {
		
		reporter.addScreenCapture(imagePath);
		
	}
	
	
	/**
	 * Wpis do logu technicznego
	 * @param str tresc wpisu
	 */
	public void writeTechnicalLog(String str) {
		  
		   String fileName = System.getProperty("user.dir")+ System.getProperty("file.separator")+"result"+ System.getProperty("file.separator")+"attachments"+ System.getProperty("file.separator")+"Service.log";
		   BufferedWriter writer;
		   
		   try {
							   
			   
		    writer = new BufferedWriter(new FileWriter(fileName, true));
		    writer.append(Methods.getDateTime("yyyy-MM-dd HH:mm:ss  ")+str);
		    writer.newLine(); 
		    writer.close();
		    
		}
		   
		   
catch (Exception e) { 
	
	//if (writer!=null) writer.close();
	
} 
finally {} }
	
	
	/**
	 * Wygenerowanie pliku raportu.
	 */
	public void generateRaport() {
		
		extent.endTest(reporter);
		extent.flush();
		extent.close();
	}
	
	
}
