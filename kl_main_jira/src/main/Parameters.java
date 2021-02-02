package main;


import main.PfUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Obsługa parametrów przekazywanych z i do Platformy  PowerFarm.
 */
public class Parameters {

	
	/*=============================================================================================
		POLA KLASY
	=============================================================================================*/
		
		
	// pola globalne
		private String workspacePath;
		private String encoding;

	// parametry plików IN
	// UWAGA: ważna jest kolejność (!!!) - wartości powtórzonych parametrów zostaną nadpisane
		private static final String[] PARAMETERS_INPUT_STRUCTURE = new String[] {
			"options.json",
			"arguments.json"
		};
	
	// mapy zawierające parametry wejściowe z PF oraz zmieniane
		private Map<String, String> TCparametersIn;
		private Map<String, String> TCparametersOut;
		
		
		
	/*=============================================================================================
		KONSTRUKTORY
	=============================================================================================*/
		
		
		/**
		 * KONSTRUKTOR
		 * @param workspacePath
		 * 			ścieżka do plików 'options.json' i 'arguments.json'
		 * @param encoding
		 * 		kododwanie plików json
		 */
		public Parameters(String workspacePath, String encoding ) {
			// inicjalizacja pól klasy
				this.TCparametersIn = new HashMap<>();
				this.TCparametersOut = new HashMap<>();
			// nadanie wartości
				this.workspacePath =
						(workspacePath.endsWith(System.getProperty("file.separator"))) ? 
						workspacePath
						: workspacePath + System.getProperty("file.separator");
				this.encoding = encoding;
		}
		
		
		/**
		 * KONSTRUKTOR
		 * @param workspacePath
		 * 			ścieżka do plików 'options.json' i 'arguments.json'
		 */
		public Parameters(String workspacePath) {
			this(workspacePath, "UTF-8");
		}
		
		
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append("parameters IN: ").append(TCparametersIn.toString());
			if (!TCparametersOut.isEmpty())
				result.append("\r\n").append("parameters OUT: ").append(TCparametersOut.toString());
			return result.toString();
		}
		
		
		
	/*=============================================================================================
		GETTERY
	=============================================================================================*/
		
		
		/**
		 * Odczyt parametrów wejściowych z predefiniowanej tablicy plików JSON.
		 * @throws IOException
		 * 		błąd odczytu plików
		 * @throws JSONException
		 * 		błąd parsowania zawartości plików
		 */
		public void readParameters() throws IOException, JSONException {
			readParameters(PARAMETERS_INPUT_STRUCTURE);
		}
		

		/**
		 * Odczyt parametrów wejściowych ze zdefiniowanych w parametrze tablicy plików JSON (nazwa + rozszerzenie).<br>
		 * Istotna jest kolejność ładowania plików, ze względu na kolejność nadpisywania wartości poszczególnych parametrów.
		 * @param fileMatrix
		 * 		tablica nazw plików JSON
		 * @throws IOException
		 * 		błąd odczytu plików
		 * @throws JSONException
		 * 		błąd parsowania zawartości plików
		 */
		private void readParameters(String[] fileMatrix) throws IOException, JSONException {
			String fileContent, filePathName;
			JSONObject JSONfileContent;
		// odczyt i parsowanie plików wejściowych z parametrami
			for (String fileName : fileMatrix) {
				filePathName = workspacePath + fileName;
				fileContent = "";
				try {
				// walidacja czy plik istnieje
					if (!PfUtils.fileExist(filePathName))
						continue;
				// ładowanie pliku
					fileContent = FileUtils.readFileToString(new File(filePathName), encoding);
					if (fileContent.length() == 0)
						throw new IOException("Błąd odczytu pliku z danymi wejściowymi '" + filePathName + "' - pusty plik");
//					fileContent = fileContent.replace("\\", "\\\\");
				// parsowanie obiektu JSON z uwzględnieniem zamieszczenia tablicy na dowolnym poziomie zagnieżdżenia dowolnego obiektu JSON
					JSONfileContent = new JSONObject(fileContent);
					for (String keyL1 : JSONObject.getNames(JSONfileContent))
						parseJSON(keyL1.toLowerCase(), JSONfileContent.get(keyL1));
					JSONfileContent = null;
				} catch(IOException e) {
					throw new IOException("Błąd odczytu pliku z danymi wejściowym '" + filePathName + "' - brak pliku");
				} catch(JSONException e) {
					throw new JSONException("Nieprawidłowy format JSON w pliku wejściowym '" + filePathName + "'");
				}
			}
		// parsowanie struktury wejściowej w poszukiwaniu argumentów Aplikacji do podmiany na wartości nadpisane przez arguments.json
//			String paramName;
//			for (String appName : getApplicationNames())
//				for (Map.Entry<String, String> arg : TCparametersIn.entrySet()) {
//					appName = appName.toLowerCase();
//					if (!arg.getKey().startsWith("applications|" + appName.toLowerCase() + "|arguments|"))
//						continue;
//					paramName = arg.getKey().split("\\|")[3];
//					if (TCparametersIn.containsKey(paramName))
//						TCparametersIn.put("applications|" + appName + "|arguments|" + paramName, TCparametersIn.get(paramName));
//				}
		}
		
		
		/**
		 * Parsowanie obiektu/tablicy JSON lub bezpośredniego String'a.
		 * @param keyName
		 * 		nazwa klucza do zamieszczenia w tablicy parametrów
		 * @param obj
		 * 		obiekt/tablica JSON lub odczytany String z obiektu JSON
		 * @throws JSONException
		 * 		błąd parsowania zawartości obiektu
		 */
		private void parseJSON(String keyName, Object obj) throws JSONException {
			JSONObject JSONobj, JSONobj1; 
			if (obj instanceof JSONObject) {
				JSONobj = (JSONObject) obj;
				for (String key : JSONObject.getNames(JSONobj)) {
					if (JSONobj.get(key) instanceof JSONObject) {
						JSONobj1 = (JSONObject) JSONobj.get(key);
						if (JSONObject.getNames(JSONobj1) != null)
							for (String key1 : JSONObject.getNames(JSONobj1))
								parseJSON(keyName.toLowerCase() + "|" + key.toLowerCase() + "|arguments|" + key1.toLowerCase(), JSONobj1.get(key1));			// arguments.json
					} else {
						parseJSON(keyName.toLowerCase() + "|" + key.toLowerCase(), JSONobj.get(key));
					}
				}
			} else if (obj instanceof JSONArray) {
				JSONArray JSONarray = (JSONArray) obj;
				for (int i = JSONarray.length() -1; i >= 0; i--) {
					JSONobj = JSONarray.getJSONObject(i);
					String name = JSONobj.getString("name").toLowerCase();
					for (String key : JSONObject.getNames(JSONobj)) {
						if (JSONobj.get(key) instanceof JSONObject) {
							JSONobj1 = (JSONObject) JSONobj.get(key);
							if (JSONObject.getNames(JSONobj1) != null)
								for (String key1 : JSONObject.getNames(JSONobj1)) {
									parseJSON(keyName.toLowerCase() + "|" + name + "|" + key.toLowerCase() + "|" + key1.toLowerCase(), JSONobj1.get(key1));		// options.json
//									if (key.equalsIgnoreCase("arguments"))
//										parseJSON(keyName.toLowerCase() + "|" + name + "|" + key.toLowerCase() + "|" + key1.toLowerCase() + "|org", JSONobj1.get(key1));
								}
						} else {
							parseJSON(keyName.toLowerCase() + "|" + name + "|" + key.toLowerCase(), JSONobj.get(key));
						}
					}
				}
			} else if (obj instanceof Boolean) {
				TCparametersIn.put(keyName.toLowerCase(), obj.toString());
			} else {
				TCparametersIn.put(keyName.toLowerCase(), (String) obj);
			}
		}
			
		
		/**
		 * Odczyt wartości ze struktury parametrów w kolejności:
		 * <ol>
		 * <li>zmieniony</li>
		 * <li>wejściowy</li>
		 * </ol>
		 * @param parameterName
		 * 		nazwa parametru
		 * @return
		 * 		wartość parametru
		 */
		public String getParameter(String parameterName) {
			String paramName = parameterName.toLowerCase();
		// odczyt ze struktury parametrów zmodyfikowanych
			if (TCparametersOut.size() > 0 && TCparametersOut.containsKey(paramName)) {
				return TCparametersOut.get(paramName);
		// odczyt ze struktury parametrów wejściowych
			} else if (TCparametersIn.size() > 0 && TCparametersIn.containsKey(paramName)) {
				return this.TCparametersIn.get(paramName);
		// brak parametru
			} else {
				throw new IllegalArgumentException("No parameter found: " + parameterName);
			}
		}
		
		
		/**
		 * Walidacja czy struktura parametrów zmienionych lub wejściowych zawiera szukany klucz.
		 * @param parameterName
		 * 		nazwa parametru
		 * @return
		 * 		<code>true</code> = struktura zawiera podany parametr
		 */
		public boolean hasParameter(String parameterName) {
			parameterName = parameterName.toLowerCase();
			boolean result = true;
			result = result && TCparametersOut.containsKey(parameterName);
			if (!result)
				result = TCparametersIn.containsKey(parameterName);
			return result;
		}
		
		
		
	/*=============================================================================================
		SETTERY
	=============================================================================================*/
		
		
		/**
		 * Zapis parametru do struktury parametrów: użytkownika lub zmieniającego wartość parametru wejściowego.
		 * @param parameterName
		 * 		nazwa (nowego/istniejącego) parametru
		 * @param parameterValue
		 * 		nowa wartość parametru
		 */
		public void setParameter(String parameterName, String parameterValue) {
			if (parameterName != null)
				TCparametersOut.put(parameterName, parameterValue);
		}
		
		
		/**
		 * Zapis parametrów użytkownika powstałych podczas trwania TC do pliku JSON.<br>
		 * Gdy brak parametrów użytkownika plik nie jest zapisywany.
		 * @throws IOException
		 * @throws JSONException
		 */
		public void saveParameters() throws IOException, JSONException {
			if (TCparametersOut.isEmpty())
				return;
			String TCname = (TCparametersIn.containsKey("unique_id")) ? TCparametersIn.get("unique_id") : (TCparametersIn.containsKey("service")) ? TCparametersIn.get("service") : "service";
			JSONObject result = new JSONObject();
			result.append(TCname, new JSONObject(TCparametersOut));
			String fileName = "arguments.json";
			workspacePath = workspacePath + "result" + System.getProperty("file.separator");
		// zapis pliku
			try {
			// walidacja czy jest istniejący do usunięcia
				if (PfUtils.fileExist(workspacePath + fileName))
					Files.delete(Paths.get(workspacePath + fileName));
			// zapis nowego pliku
				FileUtils.writeStringToFile(new File(workspacePath + fileName), result.toString(), encoding.toString());
			} catch (IOException e) {
				throw new IOException("Błąd zapisu pliku z parametrami wyjściowymi '" + workspacePath + fileName + "' - brak dostępu do zasobu dyskowego");
			}
		}
		
		
		/**
		 * Zapis parametrów wyjściowych TC w ustrukturyzowany JSON wyjściowy do pliku o podanej nazwie (nazwa + rozszerzenie).
		 * @param status
		 * 		status TC
		 * @param resultMessage
		 * 		komunikat wyjściowy
		 * @param stackTrace
		 * 		zrzut stosu w przypadku błędu
		 * @param causedBy
		 * 		przyczyna błędu - komunikat użytkownika
		 * @param screenshot
		 * 		nazwa pliku ze zrzutem ekranowym
		 * @throws IOException
		 * 		błąd dostępu do zasobu dyskowego
		 * @throws JSONException
		 * 		błąd generowania struktury JSONa wyjściowego
		 */
		public void setResult(String status, String resultMessage, String stackTrace, String causedBy, String screenshot) throws IOException, JSONException {
			String fileName = "result.json";
			JSONObject res = new JSONObject();
			res.put("status", status);
			res.put("result", resultMessage);
			JSONObject errors = new JSONObject();
			errors.put("stack_trace", stackTrace);
			errors.put("caused_by", causedBy);
			errors.put("screenshot", screenshot);		
			res.put("errors", errors);
			if (!TCparametersOut.isEmpty())
				res.put("returns", new JSONObject(TCparametersOut));
			String resultPath = workspacePath + "result" + System.getProperty("file.separator");
		// zapis pliku
			try {
			// walidacja czy jest istniejący do usunięcia
				if (PfUtils.fileExist(resultPath + fileName))
					Files.delete(Paths.get(resultPath + fileName));
			// zapis nowego pliku
				FileUtils.writeStringToFile(new File(resultPath + fileName), res.toString(), encoding.toString());
			} catch (IOException e) {
				throw new IOException("Błąd zapisu pliku z wynikiem działania Procesu '" + resultPath + fileName + "' - brak dostępu do zasobu dyskowego");
			}
		}
		
		
		/**
		 * Zwrócenie mapy parametrów wejściowych.
		 * @return
		 * 		mapa
		 */
		public Map<String,String> getMapParams() {
			return new HashMap<String, String>(TCparametersIn);
		}
		
		
		
	/*=============================================================================================
		GETTERY KONKRETNYCH KLUCZY Z 'options.json' I 'arguments.json'
	=============================================================================================*/
		
		
		/**
		 * Wygenerowanie stringa z odczytanymi opcjami i argumentami Procesu wraz z nazwami kluczy.
		 * @return
		 * 		kompletna struktura danych wejściowych: opcje i argumenty
		 */
		public String getInputParametersString() {
			StringBuilder resultParams = new StringBuilder();
			Map<String,String> params = getMapParams();
			for (String key : params.keySet())
				resultParams
					.append("'")
					.append(key)
					.append(" : ")
					.append(params.get(key))
					.append("'\n");
			resultParams.setLength(resultParams.length() -1);
			return resultParams.toString();
		}
		
		
		/**
		 * Odczyt wartości pojedynczego parametru.
		 * @param keyName
		 * 		nazwa klucza
		 * @return
		 * 		wartość klucza lub <code>null</code> jeśli nie znaleziono klucza
		 */
		private String getOneParam(String keyName) {
			if (keyName == null)
				return null;
			keyName = keyName.toLowerCase();
			if (hasParameter(keyName))
				return getParameter(keyName);
			else
				return null;
		}
		
		
	/** options.json */
		
		/**
		 * Zwrócenie zasobu do uruchomienia np. przeglądarka Chrome, os mobilny iOS.
		 * @return
		 * 		wartość klucza <i>'resource'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getResourceName() { return getOneParam("resource"); }
		
		
		/**
		 * @deprecated
		 * Obsługiwanych jest wiele aplikacji. Należy użyć metody z parametrem.
		 * Zwrócenie nazwy testowanej Aplikacji - jeżeli jest dokładnie jedna. Dla większej ilości lub braku rzucany jest wyjątek.
		 * @return
		 * 		nazwa aplikacji (wartość klucza <i>'name'</i>) / komunikat błędu
		 */
		public String getApplicationName() { 
			if (getOneParam("name") == null)
				throw new IllegalArgumentException("Method 'getApplicationName()' supports only one application. Use 'getApplicationName(String appName)' method instead.");
			return getOneParam("name");
		}
		
		
		/**
		 * Zwrócenie nazw testowanych Aplikacji.
		 * @return
		 * 		wartości kluczy <i>'name'</i> Aplikacji
		 */
		public List<String> getApplicationNames() {
			List<String> applicationNames = new ArrayList<>();
			for (Map.Entry<String, String> app : TCparametersIn.entrySet()) {
				if (app.getKey().startsWith("applications|") && app.getKey().endsWith("|name"))
					applicationNames.add(app.getValue());
			}
			return applicationNames;
		}
		
		
		/**
		 * Zwrócenie flagi czy dane Aplikacji zostały przekazane.
		 * @param appName
		 * 		nazw Aplikacji
		 * @return
		 * 		flaga
		 */
		public boolean isApplication(String appName) {
			return TCparametersIn.containsKey("applications|" + appName.toLowerCase() + "|name");
		}
		
		
		/**
		 * @deprecated
		 * Obsługiwanych jest wiele aplikacji. Należy użyć metody z parametrem.
		 * Zwrócenie adresu Aplikacji - jeżeli jest dokładnie jedna. Dla większej ilości lub braku rzucany jest wyjątek.
		 * @return
		 * 		nazwa aplikacji (wartość klucza <i>'SCIEZKA_{nazwa_testowanej_aplikacji}'</i>) / komunikat błędu
		 */
		public String getApplicationAddress() {
			if (getOneParam("SCIEZKA_" + getApplicationName()) == null)
				throw new IllegalArgumentException("Method 'getApplicationAddress()' supports only one application. Use 'getApplicationAddress(String appName)' method instead.");
			return getOneParam("SCIEZKA_" + getApplicationName());
		}
		
		
		/**
		 * Zwrócenie adresu Aplikacji o podanej nazwie.
		 * @return
		 * 		adres Aplikacji lub null jeśli Aplikacja nie została odnaleziona
		 */
		public String getApplicationAddress(String appName) {
			return getOneParam("applications|" + appName.toLowerCase() + "|url");
		}
		
		
		/**
		 * Zwrócenie argumentu globalnego dla Aplikacji (poszukiwany jest najpierw argument nadpisany przez Proces).
		 * @param appName
		 * 		nazwa Aplikacji
		 * @param argName
		 * 		nazwa argumentu
		 * @return
		 * 		wartość argumentu, <code>null</code> jeśli brak Aplikacji / argumentu
		 */
		public String getApplicationArgument(String appName, String argName) {
			if (TCparametersIn.containsKey(argName))
				return TCparametersIn.get(argName);
			return getOneParam("applications|" + appName + "|arguments|" + argName);
		}
		
		
		/**
		 * Zwrócenie orginalnego (nienadpisanego) argumentu globalnego dla Aplikacji.
		 * @param appName
		 * 		nazwa Aplikacji
		 * @param argName
		 * 		nazwa argumentu
		 * @return
		 * 		wartość argumentu, <code>null</code> jeśli brak Aplikacji / argumentu
		 */
		public String getApplicationOrgArgument(String appName, String argName) {
			return getOneParam("applications|" + appName.toLowerCase() + "|arguments|" + argName.toLowerCase());
		}
		
		
		/**
		 * Zwrócenie statusu czy argument globalny dla Aplikacji został nadpisany.
		 * @param appName
		 * 		nazwa Aplikacji
		 * @param argName
		 * 		nazwa argumentu
		 * @return
		 * 		<code>true</code> - argument został nadpisany; <code>false</code> - w przeciwnym przypadku (również jeśli argument nie istnieje)
		 */
		public boolean isApplicationOrgArgument(String appName, String argName) {
			appName = appName.toLowerCase();
			argName = argName.toLowerCase();
			String orgArg = getOneParam("applications|" + appName + "|arguments|" + argName);
			if (orgArg == null)
				return false;
			String modArg = getOneParam(argName);
			if (modArg == null)
				return false;
			return true;
		}
		
		
		/**
		 * Zwrócenie nazwy repozytorium Procesu.
		 * @return
		 * 		wartość klucza <i>'service'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getServiceName() { return getOneParam("service"); }
		
		
		/**
		 * Zwrócenie ID Procesu.
		 * @return
		 * 		wartość klucza <i>'service_id'</i>
		 */
		public String getServiceId() { return getOneParam("service_id"); }
		
		
		/**
		 * Zwrócenie identyfikatora środowiska testowanych Aplikacji.
		 * @return
		 * 		wartość klucza <i>'environment_unique_id'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getEnvironmentUniqueId() { return getOneParam("environment_unique_id"); }
		
		
		/**
		 * Zwrócenie unikalnego numeru zamówienia.
		 * @return
		 * 		wartość klucza <i>'order_id'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getOrderId() { return getOneParam("order_id"); }
		
		
		/**
		 * Zwrócenie ID zamówienia = nazwy środowiska połączonego znakiem '|' z nazwą Procesu.
		 * @return
		 * 		wartość klucza <i>'unique_id'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getUniqueId() { return getOneParam("unique_id"); }
		
		
		/**
		 * @deprecated PowerFarm zwraca wielu użytkowników o różnych uprawnieniach
		 * Zwrócenie loginu użytkownika.
		 * @return
		 * 		login z klucza <i>'userLogin'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getUserLogin() { return getOneParam("userLogin|Login"); }
		
		
		/**
		 * Zwrócenie loginu konkretnego użytkownika.
		 * @return
		 * 		login z klucza <i>'{user}|login'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getUserLogin(String user) { return getOneParam(user + "|Login"); }
		
		
		/**
		 * @deprecated PowerFarm zwraca wielu użytkowników o różnych uprawnieniach
		 * Zwrócenie hasła użytkownika.
		 * @return
		 * 		hasło z klucza <i>'userLogin'</i> lub <code>null</code> jeśli nie znaleziono klucza, lub jest on niekompletny
		 */
		public String getUserPassword() { return getOneParam("userLogin|Password"); }
		
		
		/**
		 * Zwrócenie hasła konkretnego użytkownika.
		 * @return
		 * 		hasło z klucza <i>'{user}|Password'</i> lub <code>null</code> jeśli nie znaleziono klucza, lub jest on niekompletny
		 */
		public String getUserPassword(String user) { return getOneParam(user + "|Password"); }
		
		
		/**
		 * Zwrócenie loginu, hasła i uprawnienia konkretnego użytkownika.
		 * @return
		 * 		dane konkretnego użytkownika [0: login; 1: hasło; 2: uprawnienie(dla dedykowanego <code>null</code>)] <br>
		 * 		lub <code>null</code> jeśli nie znaleziono klucza, lub jest on niekompletny
		 */
		public String[] getUser(String user) {
			if (getOneParam(user + "|Login") == null)
				return null;
			return new String[] { getOneParam(user + "|Login"), getOneParam(user + "|Password"), getOneParam(user + "|Permission") };
		}
		
		
		/**
		 * Zwrócenie IP i portu instancji Appium dla urządzenia ze zbioru parametrów charakteryzujących urządzenie mobilne.
		 * @return
		 * 		wartość klucza <i>'mobile_info|mobile_address'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getMobileAddress() { return getOneParam("mobile_info|mobile_address"); }
		
		
		/**
		 * Zwrócenie nazwy systemu operacyjnego urządzenia ze zbioru parametrów charakteryzujących urządzenie mobilne.
		 * @return
		 * 		wartość klucza <i>'mobile_info|mobile_os'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getMobileOsName() { return getOneParam("mobile_info|mobile_os"); }
		
		
		/**
		 * Zwrócenie wersji systemu operacyjnego urządzenia ze zbioru parametrów charakteryzujących urządzenie mobilne.
		 * @return
		 * 		wartość klucza <i>'mobile_info|mobile_os_version'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getMobileOsVersion() { return getOneParam("mobile_info|mobile_os_version"); }
		
		
		/**
		 * Zwrócenie flagi czy zostały przekazane dane użytkownika dedykowanego.
		 * @return
		 * 		flaga
		 */
		public boolean isDedicatedUser() {
			return getOneParam("with_dedicated_user").equals("true");
		}
		
		
		/**
		 * Zwrócenie loginu użytkownika dedykowanego.
		 * @return
		 * 		login z klucza <i>'dedicated_user\login'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getDedicatedUserLogin() {
			return getOneParam("dedicated_user|login");
		}
		
		
		/**
		 * Zwrócenie hasła użytkownika dedykowanego.
		 * @return
		 * 		hasło z klucza <i>'dedicated_user\password'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getDedicatedUserPassword() {
			return getOneParam("dedicated_user|password");
		}
		
		
	/** arguments.json */
	
		/**
		 * Zwrócenie ścieżki do przekazywanego pliku datatable.
		 * @return
		 * 		wartość klucza <i>'SCIEZKA_PLIKU_Z_PARAMETRAMI'</i> lub <code>null</code> jeśli nie znaleziono klucza
		 */
		public String getParametersPath() { return getOneParam("SCIEZKA_PLIKU_Z_PARAMETRAMI"); }
		
		
}