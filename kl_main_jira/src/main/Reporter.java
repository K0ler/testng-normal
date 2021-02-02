package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import utils.Methods;


/**
 * Raportowanie użytkownika przebiegu Usługi.
 */
public class Reporter {
	
	
	/*=============================================================================================
		POLA KLASY
	=============================================================================================*/
	
		protected ExtentReports extent = null;
		protected ExtentTest reporter = null, reporter0 = null;
		
		private final String fileName;
		private boolean loggingOn;											// flaga: czy wyłączyc logowanie - domyślnie true
		private boolean logFailAndErrorOnly;								// flaga: czy logowac tylko statusy FAIL i ERROR - domyślnie false
		private static List<String> listResult = new ArrayList<String>();	// struktura z czystym tekstem wspisów raportowych do eksportu
		
		
		
	/*=============================================================================================
		KONSTRUKTORY i KONFIGURACJA
	=============================================================================================*/
		
		
		/** 
		 * KONSTRUKTOR
		 * @param reportPathFileName 
		 * 		ścieżka z nazwą, gdzie zapisać plik raportu
		 * @param workspacePath
		 * 		ścieżka do folderu 'workspace'
		 * @param serviceFileName
		 * 		nazwa pliku raportowego technicznego
		 */
		public Reporter(String reportPathFileName, String workspacePath, String serviceFileName) {
			if (reportPathFileName != null && !reportPathFileName.isEmpty())
				this.extent = new ExtentReports(reportPathFileName, true);
			loggingOn = true;
			logFailAndErrorOnly = false;
			fileName = (new StringBuilder())
				.append(workspacePath)
				.append(System.getProperty("file.separator"))
				.append("result")
				.append(System.getProperty("file.separator"))
				.append("attachments")
				.append(System.getProperty("file.separator"))
				.append(serviceFileName).toString();
		}
		
		
		/** 
		 * KONSTRUKTOR
		 * @param reportPathFileName 
		 * 		ścieżka z nazwą, gdzie zapisać plik raportu
		 * @param workspacePath
		 * 		ścieżka do folderu 'workspace'
		 */
		public Reporter(String reportPathFileName, String workspacePath) {
			this(reportPathFileName, workspacePath, "service.log");
		}
		
		
		/** 
		 * KONSTRUKTOR (legacy, left for compatibility)
		 * @param reportPathFileName 
		 * 		ścieżka z nazwą, gdzie zapisać plik raportu
		 */
		public Reporter(String reportPathFileName) {
			this(reportPathFileName, Main.workspacePath);
		}
		
		
		/**
		 * Utworzenie nowego wpisu w strukturze raportowej.
		 * @param testName
		 * 		nazwa testu
		 */
		public ExtentTest startTest(String testName) {
			reporter = this.extent.startTest(testName);
			if (reporter0 == null)
				reporter0 = reporter;
			return reporter;
		}
		
		
		/**
		 * Przełączenie logowania na wcześniejszy test.
		 * @param report
		 * 		obiekt testu
		 */
		public void switchToTest(ExtentTest report) {
			this.reporter = report;
		}
		
		
		/**
		 * Przełączenie logowania na pierwszy test.
		 */
		public void switchTo0Test() {
			this.reporter = reporter0;
		}
		
		
		/**
		 * Sterowanie czy włączyć logowanie. Będą dodwane tylko statusy <i>INFO</i>.
		 */
		public void loggingOn() { this.loggingOn = true; }
		
		
		/**
		 * Sterowanie czy wyłączyć całe logowanie. Będą dodawane tylko statusy <i>INFO</i>.
		 */
		public void loggingOff() { this.loggingOn = false; }
		
		
		/**
		 * Sterowanie czy włączyć logowanie tylko dla statusów <i>INFO, FAIL, ERROR</i>.
		 */
		public void logFailsErrorsOn() { this.logFailAndErrorOnly = true; }
		
		
		/**
		 * Sterowanie czy wyłączyć logowanie tylko dla statusów <i>INFO, FAIL, ERROR</i>.
		 */
		public void logFailsErrorsOff() { this.logFailAndErrorOnly = false; }
		
		
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
				.append(!loggingOn ? "" : (logFailAndErrorOnly ? ", logowanie statusów INFO, FAIL i ERROR" : ", logowanie wszystkich statusów"))
				.toString();
		}
		
		
		
	/*=============================================================================================
		SETTERY
	=============================================================================================*/
		
		
		/**
		 * Dodanie wpisu informacyjnego (<i>INFO</i>).
		 * @param msg
		 * 		treść wpisu
		 * @return
		 * 		treść wpisu
		 */
		public String logInfo(String msg) {
			if (loggingOn && !logFailAndErrorOnly) {
				listResult.add(clearHtml(msg));
				if (reporter != null)
					reporter.log(LogStatus.INFO, msg);
				writeTechnicalLog("[INFO] :  " + msg);
			}
			return msg;
		}
		
		
		/**
		 * Dodanie statusu informacyjnego (<i>INFO</i>) tylko do loga technicznego.<br>
		 * Sterowanie czy dodać wpis za pomocą flag.
		 * @param msg
		 * 		treść wpisu
		 * @return
		 * 		treść wpisu
		 */
		public String logTechnicalInfo(String msg) {
			if (loggingOn) {
				listResult.add("[INFO]" + clearHtml(msg));
				writeTechnicalLog("[INFO] :  " + msg);
			}
			return msg;
		}
		
		
		/**
		 * Dodanie statusu pozytywnego (<i>PASS</i>).<br>
		 * Sterowanie czy dodać wpis za pomocą flag.
		 * @param msg
		 * 		treść wpisu
		 * @return
		 * 		treść wpisu
		 */
		public String logPass(String msg) {
			if (loggingOn && !logFailAndErrorOnly) {
				listResult.add("[PASS]" + clearHtml(msg));
				if (reporter != null)
					reporter.log(LogStatus.PASS, msg);
				writeTechnicalLog("[PASS] :  " + msg);
			}
			return msg;
		}
		
		
		/**
		 * Dodanie statusu negatywnego (<i>FAIL</i>).<br>
		 * Sterowanie czy dodać wpis za pomocą flag.
		 * @param msg
		 * 		treść wpisu
		 * @return
		 * 		treść wpisu
		 */
		public String logFail(String msg) {
			if (loggingOn) {
				listResult.add("[FAIL]" + clearHtml(msg));
				if (reporter != null)
						reporter.log(LogStatus.FAIL, msg);
				writeTechnicalLog("[FAIL] :  " + msg);
			}
			return msg;
		}
		
		
		/**
		 * Dodanie statusu negatywnego (<i>FAIL</i>) tylko do loga technicznego.<br>
		 * Sterowanie czy dodać wpis za pomocą flag.
		 * @param msg
		 * 		treść wpisu
		 * @return
		 * 		treść wpisu
		 */
		public String logTechnicalFail(String msg) {
			if (loggingOn) {
				listResult.add("[FAIL]" + clearHtml(msg));
				writeTechnicalLog("[FAIL] :  " + msg);
			}
			return msg;
		}
		
		
		/**
		 * Dodanie statusu błędu (<i>ERROR</i>).<br>
		 * Sterowanie czy dodać wpis za pomocą flag.
		 * @param msg
		 * 		treść wpisu
		 * @return
		 * 		treść wpisu
		 */
		public String logError(String msg) {
			if (loggingOn) {
				listResult.add("[ERROR]" + clearHtml(msg));
				if (reporter != null)
					reporter.log(LogStatus.ERROR, msg);
				writeTechnicalLog("[ERROR] :  " + msg);
			}
			return msg;
		}
		
		
		/**
		 * Dodanie informacji środowiskowej do raportu.
		 * @param param
		 * 		nazwa informacji
		 * @param info
		 * 		tekst informujący
		 * @return
		 * 		tekst informujący
		 */
		public String addSystemInfo(String param, String info) {
			if (param == null)
				return null;
			extent.addSystemInfo(param, info);
			return info;
		}
		
		
		/**
		 * Wpis do logu technicznego.
		 * @param msg
		 * 		treść wpisu
		 * @return
		 * 		treść wpisu
		 */
		public String writeTechnicalLog(String msg) {
			BufferedWriter writer ;
			try {
				writer = new BufferedWriter(new FileWriter(fileName, true));
				writer.append(Methods.getDateTime("yyyy-MM-dd HH:mm:ss  ") + clearHtml(msg));
				writer.newLine();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally { }
			return msg;
		}
		
		
		/**
		 * Dodanie zrzutu ekranowego do raportu.
		 * @param imagePath
		 * 		ścieżka z nazwą pliku do zapisania zrzutu ekranowego
		 */
		public void logScreenShot(String imagePath) {
			logScreenShot(imagePath, LogStatus.INFO);
		}
		
		
		/**
		 * Dodanie zrzutu ekranowego do raportu.
		 * @param imagePath
		 * 		ścieżka z nazwą pliku ze zrzutem ekranowym
		 * @param
		 * 		status w raporcie do zrzutu ekranowego
		 */
		public void logScreenShot(String imagePath, LogStatus status) {
			if (reporter != null)
				reporter.log(status, reporter.addScreenCapture(imagePath));
		}
		
		
		/**
		 * Dodanie zrzutu ekranu ze statusem <i>INFO</i>.
		 * @param imgDataB64
		 * 		obraz w formacie Base64 (z WebDriver'a)
		 */
		public void logInfoWithScreenCapture(String imgDataB64) {
			logScreenCapture(imgDataB64, LogStatus.INFO);
		}
		
		
		/**
		 * Dodanie zrzutu ekranu ze statusem <i>ERROR</i>.
		 * @param imgDataB64
		 * 		obraz w formacie Base64 (z WebDriver'a)
		 */
		public void logErrorWithScreenCapture(String imgDataB64) {
			logScreenCapture(imgDataB64, LogStatus.ERROR);
		}
		
		
		/**
		 * Dodanie zrzutu ekranu z podanym statusem.
		 * @param imgDataB64
		 * 		obraz w formacie Base64 (z WebDriver'a)
		 * @param status
		 * 		status do raportu
		 */
		public void logScreenCapture(String imgDataB64, LogStatus status) {
			if (reporter != null)
				reporter.log(status, reporter.addBase64ScreenShot("data:image/png;base64," + imgDataB64));
		}
		
		
		
	/*=============================================================================================
		GETTERY
	=============================================================================================*/
		
		
		/**
		 * Wygenerowanie pliku raportu ExtendReports w folderze 'results'.
		 */
		public void generateRaport() {
			extent.endTest(reporter);
			extent.flush();
		}
		
		
		/**
		 * Dodanie bieżącego raportu do pliku raportu.
		 */
		public void appendRaport() {
			extent.endTest(reporter);
			extent.flush();
		}
		
		
		/**
		 * Zapis pliku raportu.
		 */
		public void closeRaport() {
			extent.endTest(reporter);
			extent.flush();
//			extent.close();
		}
		
		
		/**
		 * Zwrócenie listy wpisów reportera.
		 * @return scalona treść wpisów
		 */
		public static String GetAllListResultRecords() {
			String result = "";
			if (listResult.size() == 0)
				return "";
			for (int i = 0; i < listResult.size(); i++)
				result = result + listResult.get(i) + "\n";
			return result;
		}
		
		
		
	/*=============================================================================================
		UTILS
	=============================================================================================*/
		
		
		/**
		 * Usunięcie tagów HTML i wcinki kolejnych wierszy do logu tekstowego.
		 * @param msg
		 * 		treść do konwersji
		 * @return wyfiltrowana treść
		 */
		public String clearHtml(String msg) {
			if (msg != null)
				return msg.replaceAll("<br>|<p (.*?)>|<div(.*?)", "\n").
						replaceAll("</p>|</div>|<font(.*?)>|</font>|<i>|</i>|<b>|</b>|<strong>|</strong>|<pre>|</pre>", "");
			return msg;
		}
		
		
		/**
		 * Globalna obsługa błędow stackTrace'a.<br>
		 * Wypisanie stackTrace'a na ekrania, wpis treści stackTrace'a do logu.
		 * @param e
		 * 		obiekt wyjątku <pre>Exception</pre>
		 * @param driver
		 * 		obiekt drivera przeglądarki
		 */
		public void exceptionHandler(Throwable e, WebDriver driver) {
			exceptionHandler(e);
			try {
				if (driver != null)
					logScreenCapture(PfUtils.getScreenShotAsBase64(driver), LogStatus.FAIL);
			} catch (IOException e1) { }
		}
		
		
		/**
		 * Globalna obsługa błędow stackTrace'a.<br>
		 * Wypisanie stackTrace'a na ekrania, wpis treści stackTrace'a do logu.
		 * @param e
		 * 		obiekt wyjątku <pre>Exception</pre>
		 */
		public String exceptionHandler(Throwable e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			writeTechnicalLog("[ERROR] :  " + sw.toString());
			logFail(e.getMessage());
			return sw.toString();
		}
		
}
