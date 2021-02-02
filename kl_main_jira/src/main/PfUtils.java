package main;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class PfUtils {
	
	
	/*=============================================================================================
		GENREAL UTILS
	=============================================================================================*/	 
	
		/**
		 * Get timestamp according to some formats.
		 * @param format
		 * 		the format timestamp is generated with : [filename|timestamp|timestamps|nobreak|nobreaks]
		 * @return
		 * 		formatted timestamp string
		 */
		public static String getCurrentDateTime(String format) {
			switch (format.toLowerCase()) {
				case "filename" :
					format = "yyyy-MM-dd_HH-mm-ss";
					break;
				case "timestamp" :
					format = "yyyy-MM-dd HH:mm";
					break;
				case "timestamps" :
					format = "yyyy-MM-dd HH:mm:ss";
					break;
				case "nobreak":
					format = "yyyyMMddHHmm";
					break;
				case "nobreaks":
					format = "yyyyMMddHHmmss";
					break;
				default :
					return "";
			}
			return new SimpleDateFormat(format).format(new Date());
		}
		
		
		/**
		 * Get time between two points of time.<br>
		 * The returned time depends on whether the difference is greater than 1 hour (HH:mm:ss) and greater than 1 minute (mm:ss).
		 * @param startTime
		 * 		the time to start count the difference from
		 * @param endTime
		 * 		the time to start count the difference to
		 * @return
		 * 		formatted string
		 */
		public static String getTimeDifference(long startTime, long endTime) {
			long diff = endTime - startTime;
//			Duration amountOfTime = Duration.ofMillis(diff);	
//		    return String.format("%02d:%02d:%02d",amountOfTime.toHours(),
//		            amountOfTime.toMinutes(), amountOfTime.getSeconds());
			
//			if (diff < 60*1000)		// only seconds
//				return new SimpleDateFormat("ss").format(diff);
//			if (diff < 60*60*1000)	// minutes and seconds
//				return new SimpleDateFormat("mm:ss").format(diff);
			return new SimpleDateFormat("HH:mm:ss").format(diff - 60*60*1000);	// SimpleDateFormat error (adds one hour)
		}
		
	
	/*=============================================================================================
		SCREENSHOTS / ZRZUTY EKRANOWE
	=============================================================================================*/	 
		
		/**
		 * Wykonanie zrzutu ekranu z przeglądarki do pliku PNG.
		 * @param driver
		 * 		driver przeglądarki
		 * @return
		 * 		nazwa pliku zrzutu
		 */	
		public static String getWebScreenShot(WebDriver driver) throws IOException {
			return getWebScreenShot(driver, "screenshot" + getCurrentDateTime("fileName"));
		}
		
		
		/**
		 * Wykonanie zrzutu ekranu z przeglądarki do pliku PNG.
		 * @param driver
		 * 		driver przeglądarki
		 * @param strFilename
		 * 		nazwa pliku zrzutu ekranu (bez rozszerzenia!)
		 * @return
		 * 		nazwa pliku zrzutu
		 */
		public static String getWebScreenShot(WebDriver driver, String strFilename) throws IOException {
			if (driver == null)
				return "";
			WebDriver augmentedDriver = new Augmenter().augment(driver);
			File srcFile = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
			String filePath = Main.workspacePath + "result" + System.getProperty("file.separator") + "attachments"
					+ System.getProperty("file.separator");
			String fileName = strFilename + ".png";
			FileUtils.copyFile(srcFile, new File(filePath + fileName));
			return fileName;
		}
		
		
		/**
		 * Wykonanie zrzutu ekranu z przeglądarki do pliku PNG - wykorzystana jest biblioteka <i>Ashot</i>.
		 * @param driver
		 * 		driver przeglądarki
		 */	
		public static void getAShot(WebDriver driver) {
			Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(3000)).takeScreenshot(driver);
			String fileName = Main.workspacePath + "result" + System.getProperty("file.separator") + "attachments"
					+ System.getProperty("file.separator") + "screenshot_" + getCurrentDateTime("fileName") + ".png";
			try {
				ImageIO.write(fpScreenshot.getImage(), "PNG", new File(fileName));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
		/**
		 * Wykonanie zrzutu ekranu z przeglądarki do pliku PNG przy wykorzystaniu biblioteki <i>Augmenter</i>.
		 * @param driver
		 * 		driver przeglądarki
		 * @return
		 * 		obrazek w formacie Base64
		 */	
		public static String getScreenShotAsBase64(WebDriver driver) throws IOException {
			WebDriver augmentedDriver = new Augmenter().augment(driver);
			String image = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.BASE64);
			return image;
		}
		
		
	/*=============================================================================================
		OPERACJE NA PLIKACH
	=============================================================================================*/	 
		
		
		/**
		 * Walidacja istnienia pliku.
		 * @param path
		 * 		bezwględna ścieżka wraz z nazwą pliku i rozszerzeniem
		 * @return
		 * 		<code>true</code> = plik istnieje
		 */
		public static boolean fileExist(String path) {
			Path oPath = Paths.get(path);
			return Files.exists(oPath);
		}
		
		
		/**
		 * Wyszukanie nazwy pliku w folderze. DO DOKOŃCZENIA. TODO
		 * @param name
		 * 		nazwa szukanego pliku
		 * @param folder
		 * 		obiekt folderu
		 */
		public static void findFile(String name, File folder) {
			System.out.println(folder.getAbsolutePath());
			File[] list = folder.listFiles();
			if (list != null)
				for (File fil : list)
					if (fil.isDirectory()) {
						findFile(name, folder);
					} else if (name.equalsIgnoreCase(fil.getName())) {
						System.out.println(fil.getParentFile());
					}
		}
		
		
		/**
		 * Wyszukanie nazwy pliku w folderze.
		 * @param root
		 * 		obiekt folderu
		 * @param name
		 * 		nazwa szukanego pliku
		 * @return
		 * 		lista znalezionych obiektów plików
		 */
		public static List<File> search(File root, String name) {
			List<File> found = new ArrayList<File>();
			if (root.isFile() && root.getName().equalsIgnoreCase(name)) {
				found.add(root);
			} else if (root.isDirectory()) {
				for (File file : root.listFiles())
					found.addAll(search(file, name));
			}
			return found;
		}
		
		
		/**
		 * Wyszukanie plików o podanym rozszerzeniu w folderze i jego subfolderach.
		 * @param path
		 * 		ścieżka w której mają być wyszukiwane pliki
		 * @param extension
		 * 		rozszerzenie plików do wyszukania
		 * @return
		 * 		lista plików
		 */
		public static File[] findFilesExtension(String path, String extension) {
			if (!extension.startsWith("."))
				extension = "." + extension;
			final String ext = extension;
			return new File(path).listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(ext);
				}
			});
		}
		
		
		
	/*=============================================================================================
		OPERACJE NA PLIKACH CSV
	=============================================================================================*/	 
		
		
		public static void SaveToCSV(String FileName, Object[] line/*, boolean append*/) throws IOException {
			String root = (new File(".").getAbsoluteFile()) + "\\result\\attachments\\";
			BufferedWriter writer = new BufferedWriter(new FileWriter(root+FileName + ".csv", true));
			for (Object el: line)
				writer.write(el.toString() + ";");
			writer.write("\n");
			writer.close();
		}
		
		
		public static void SaveToCSV2(String FileName, Object[] line/*, boolean append*/) throws IOException {
			String root = (new File(".").getAbsoluteFile()) + "\\result\\attachments\\";
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(root + FileName + ".csv", true), StandardCharsets.UTF_8));
			writer.write('\uFEFF');
			for (Object el: line)
				writer.write(el.toString() + ";");
			writer.write("\n");
			writer.close();
		}
		
		
		public static void SaveToCSV3(String FileName, Object[] line, String message/*, boolean append*/) throws IOException {
			String root = (new File(".").getAbsoluteFile()) + "\\result\\attachments\\";
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(root + FileName + ".csv", true), StandardCharsets.UTF_8));
			writer.write('\uFEFF');
			for (Object el: line)
				writer.write(el.toString() + ";");
			writer.write(message + ";");
			writer.write("\n");
			writer.close();
		}
		
		
		public static void cleanAndAddHeader(String FileName, Object[] line/*, boolean append*/) throws IOException {
			String root = (new File(".").getAbsoluteFile()) + "\\result\\attachments\\";
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(root + FileName + ".csv", false), StandardCharsets.UTF_8));
			writer.write('\uFEFF');
			for (Object el: line)
				writer.write(el.toString() + ";");
			writer.write("\n");
			writer.close();
		}
		
		
		
	/*=============================================================================================
		CAPABILITIES
	=============================================================================================*/	 
		
		public DesiredCapabilities setAdditionalMobileCapabilities(String os, DesiredCapabilities cap) throws IOException {
			Map<String,String> capMap = new HashMap<String,String>();
			os = os.toLowerCase();
			switch (os) {
				case "ios":
					capMap = readProperties("c-ios.cap",capMap);
					capMap = readProperties("t-ios.cap",capMap);
					cap = addCapabilities(cap, capMap);
					break;
				case "android":
					capMap = readProperties("c-android.cap",capMap);
					capMap = readProperties("t-android.cap",capMap);
					cap = addCapabilities(cap, capMap);
					break;
				default:
					break;
			}
			return cap;
		}
		
		
		public DesiredCapabilities setAdditionalWebCapabilitiesOptions(String browser, DesiredCapabilities cap) throws IOException {
			Map<String,String> capMap = new HashMap<String,String>();
			browser = browser.toLowerCase();
			switch (browser) {
				case "chrome":
					capMap = readProperties("c-chrome.cap",capMap);
					capMap = readProperties("t-chrome.cap",capMap);
					capMap = readProperties("c-chrome.opt",capMap);
					capMap = readProperties("t-chrome.opt",capMap);
					cap = addCapabilities(cap, capMap);
					break;
				case "firefox":
					capMap = readProperties("c-firefox.cap",capMap);
					capMap = readProperties("t-firefox.cap",capMap);
					capMap = readProperties("c-firefox.opt",capMap);
					capMap = readProperties("t-firefox.opt",capMap);
					cap = addCapabilities(cap, capMap);
					break;	
				case "ie":
					capMap = readProperties("c-ie.cap",capMap);
					capMap = readProperties("t-ie.cap",capMap);
					capMap = readProperties("c-ie.opt",capMap);
					capMap = readProperties("t-ie.opt",capMap);
					cap = addCapabilities(cap, capMap);
					break;
				default:
					break;
			}
			return cap;
		}
		
		
		public DesiredCapabilities addCapabilities(DesiredCapabilities cap, Map<String,String> capMap) {
			for (Map.Entry<String, String> entry : capMap.entrySet())
				cap.setCapability(entry.getKey(), entry.getValue());
			return cap;
		}
		
		
		public Map<String, String> readProperties(String propertiesFile, Map<String, String> capMap) {
			try {
				File file = new File(propertiesFile);
				FileInputStream fileInput = new FileInputStream(file);
				Properties properties = new Properties();
				properties.load(fileInput);
				fileInput.close();
				Enumeration<?> enuKeys = properties.keys();
				while (enuKeys.hasMoreElements()) {
					String key = (String) enuKeys.nextElement();
					String value = properties.getProperty(key);
					System.out.println(key + ": " + value);
					capMap.put(key, value);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return capMap;
		}
		

/*		public static String getMobileScreenShot(AppiumDriver driver) throws IOException {
			File srcFiler = driver.getScreenshotAs(OutputType.FILE);
			String filePath = Main.workspacePath + "result" +
					System.getProperty("file.separator") + "attachments" + System.getProperty("file.separator");
			String fileName = "Screenshot.png";
			FileUtils.copyFile(srcFiler, new File(filePath + fileName));
			return fileName;
		}
*/
	
}
