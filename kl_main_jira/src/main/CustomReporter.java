package main;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import utils.Methods;



/**
 * Raportowanie użytkownika przebiegu Usługi.
 */
public class CustomReporter {
	
	// pola klasy
		public ExtentReports extent;
		public ExtentTest reporter;
		
		private final String fileName;
		private boolean loggingOn;										// flaga: czy wyłączyć logowanie - domyślnie true
		private boolean logFailAndErrorOnly;							// flaga: czy logować tylko statusy FAIL i ERROR - domyślnie false
		public static List<String> listResult = new ArrayList<String>();
	

	/** 
	 * KONSTRUKTOR
	 * @param reportPathFileName 
	 * 		sciezka z nazwa, gdzie zapisac plik raportu
	 */
	public CustomReporter(String reportPathFileName) {
		this.extent = new ExtentReports(reportPathFileName,true);
//		this.extent = new ExtentReports(reportPathFileName, NetworkMode.OFFLINE);
		loggingOn = true;
		logFailAndErrorOnly = false;
		fileName = (new StringBuilder())
			.append(Main.workspacePath)
			.append("result")
			.append(System.getProperty("file.separator"))
			.append("attachments")
			.append(System.getProperty("file.separator"))
			.append("service.log").toString();
	}
	
	
	/**
	 * Załadowanie kofiguracji reportera z pliku.
	 * @param fileName nazwa pliku z konfiguracją
	 */
	public void loadConfig(String fileName) {
		if (PfUtils.fileExist(fileName))
			this.extent.loadConfig(new File(fileName));
	}
	
	
	@Override
	public String toString() {
		return (new StringBuilder())
			.append(loggingOn ? "logowanie ON" : "logowanie OFF")
			.append(!loggingOn ? "" : (logFailAndErrorOnly ? ", logowanie statusow INFO, FAIL i ERROR" : ", logowanie wszystkich statusow"))
			.toString();
	}
	
	
	/**
	 * Utworzenie nowego wpisu w strukturze raportowej.
	 * @param testName
	 * 		nazwa testu
	 */
	public void startTest(String testName) {
		reporter = this.extent.startTest(testName);
	}
	
	
	/**
	 * Dodanie wpisu informacyjnego (INFO).
	 * @param msg
	 * 		tresc wpisu
	 */
	public void logInfo(String msg) {

		if (loggingOn && !logFailAndErrorOnly) {
			listResult.add(msg);
		reporter.log(LogStatus.INFO, msg);
		writeTechnicalLog("[INFO] :  " + msg);	
		}
	}
	
	
	/**
	 * Dodanie statusu pozytywnego (PASS).<br/>
	 * Sterowanie czy dodac wpis za pomoca flag.
	 * @param msg
	 * 		tresc wpisu
	 */
	public void logPass(String msg) {
		
		if (loggingOn && !logFailAndErrorOnly) {
			listResult.add(msg + "[PASS]");
			reporter.log(LogStatus.PASS, msg);
			writeTechnicalLog("[PASS] :  " + msg);
		}
	}
	
	
	/**
	 * Dodanie statusu negatywnego (FAIL).<br/>
	 * Sterowanie czy dodac wpis za pomoca flag.
	 * @param msg
	 * 		tresc wpisu
	 */
	
	public void logFail(String msg) {
		if (loggingOn) {
			listResult.add(msg + "[FAIL]");
			reporter.log(LogStatus.FAIL, msg);
			writeTechnicalLog("[FAIL] :  " + msg);
		}
	}
	
	public void logTechnicalFail(String msg) {
		if (loggingOn) {
			listResult.add(msg + "[FAIL]");
			
			writeTechnicalLog("[FAIL] :  " + msg);
		}
	}
	
	
	
	/**
	 * Dodanie statusu bledu (FAIL).<br/>
	 * Sterowanie czy dodac wpis za pomoca flag.
	 * @param msg
	 * 		tresc wpisu
	 */
	public void logError(String msg) {
		if (loggingOn) {
			listResult.add("[ERROR]" + msg);
			reporter.log(LogStatus.ERROR, msg);
			writeTechnicalLog("[ERROR] :  " + msg);
		}
	}
	
	
	/**
	 * Sterowanie czy wlaczyc logowanie. Beda dodwane tylko statusy <pre>INFO</pre>.
	 */
	public void loggingOn() { this.loggingOn = true; }
	
	
	/**
	 * Sterowanie czy wylaczyc cale logowanie. Beda dodawane tylko statusy <pre>INFO</pre>.
	 */
	public void loggingOff() { this.loggingOn = false; }
	
	
	/**
	 * Sterowanie czy wlaczyc logowanie tylko dla statusow <pre>INFO, FAIL, ERROR</pre>.
	 */
	public void logFailsErrorsOn() { this.logFailAndErrorOnly = true; }
	
	
	/**
	 * Sterowanie czy wylaczyc logowanie tylko dla statusow <pre>INFO, FAIL, ERROR</pre>.
	 */
	public void logFailsErrorsOff() { this.logFailAndErrorOnly = false; }
	
	
	/**
	 * Dodanie zrzutu ekranowego do raportu.
	 * @param imagePath
	 * 		sciezka z nazwa pliku ze zrzutem ekranowym
	 */
	public void logScreenShot(String imagePath) {
		reporter.addScreenCapture(imagePath);
	}
	
	
	/**
	 * Wpis do logu technicznego
	 * @param str
	 * 		tresc wpisu
	 */
	public void writeTechnicalLog(String str) {
		BufferedWriter writer ;
		try {
			writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.append(Methods.getDateTime("yyyy-MM-dd HH:mm:ss  ") + str);
			writer.newLine();
			writer.close();
		} catch (Exception e) {}
		
		finally { }
	}
	
	
	/**
	 * Wygenerowanie pliku raportu.
	 */
	public void generateRaport() {
		extent.endTest(reporter);
		extent.flush();
		extent.close();
	}
	
	public void closeRaport() {
	
		extent.flush();
		extent.close();
	}
	
	/**
	 * Wygenerowanie pliku raportu.
	 */
	public void appendRaport() {
		extent.endTest(reporter);
		extent.flush();
		
	}
	
	
	
	public static String GetAllListResultRecords() {
		String tmp = "";
		if (listResult.size()==0) return "";
		for(int i= 0; i<listResult.size();i++) {
			tmp = tmp + listResult.get(i) + "\n";
		}
		return tmp;
	}
	
	public void logInfoWithScreenCapture(String Base64) {	
		reporter.log(LogStatus.INFO, reporter.addBase64ScreenShot("data:image/png;base64,"+Base64));
	}

	
}
