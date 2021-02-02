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

import java.util.HashMap;
import java.util.Map;


/**
 * Obsluga parametrow przekazywanych z Platformy i do Platformy.
 */
public class Parameters_bck {

	
	/*=============================================================================================
		POLA KLASY
	=============================================================================================*/
		
		
		/* powielenie pol klasy Main */
			private String TC_PATH;
			private String TC_CHARSET;
	
		/* parametry plikow IN */
			private static final String[] PARAMETERS_INPUT_STRUCTURE = new String[] {		// ważna jest kolejność (!!!) - wartości powtórzonych parametrów zostaną nadpisane
				"options.json",
				"arguments.json"
			};
		
		/* mapy zawierajace parametry wejsciowe z PF oraz zmieniane */
			private Map<String, String> TCparametersIn;
			private Map<String, String> TCparametersOut;
		
		
		
	/*=============================================================================================
		KONSTRUKTORY
	=============================================================================================*/
		
		
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append("parameters IN: ").append(TCparametersIn.toString());
			if (!TCparametersOut.isEmpty())
				result.append("\r\n").append("parameters OUT: ").append(TCparametersOut.toString());
			return result.toString();
		}
		
		
		public Parameters_bck(String TC_PATH, String TC_CHARSET ) {
			// inicjalizacja pol klasy
				this.TCparametersIn = new HashMap<>();
				this.TCparametersOut = new HashMap<>();
			// nadanie wartosci
				this.TC_PATH = TC_PATH + ((TC_PATH.endsWith(System.getProperty("file.separator"))) ? "" : System.getProperty("file.separator"));
				this.TC_CHARSET = TC_CHARSET;
		}
		
		
		
	/*=============================================================================================
		GETTERY
	=============================================================================================*/
		
		
		/**
		 * Odczyt parametrow wejsciowych z predefiniowanej tablicy plikow JSON.
		 * @throws IOException
		 * 		blad odczytu plikow
		 * @throws JSONException
		 * 		blad parsowania zawartosci plikow
		 */
		public void readParameters() throws IOException, JSONException {
			readParameters(PARAMETERS_INPUT_STRUCTURE);
		}
		

		/**
		 * Odczyt parametrow wejsciowych ze zdefiniowanych w parametrze tablicy plikow JSON (nazwa + rozszerzenie).
		 * Istotna jest kolejnosc ladowania plikow, ze wzgledu na kolejnosc nadpisywania wartosci poszczegolnych parametrow.
		 * @param fileMatrix
		 * 		tablica nazw plikow JSON
		 * @throws IOException
		 * 		blad odczytu plikow
		 * @throws JSONException
		 * 		bladd parsowania zawartosci plikow
		 */
		private void readParameters(String[] fileMatrix) throws IOException, JSONException {
			String fileContent, filePathName;
			JSONObject JSONfileContent;
		// odczyt i parsowanie plikow wejsciowych z parametrami
			for (String fileName : fileMatrix) {
				filePathName = TC_PATH + fileName;
				fileContent = "";
				try {
				// walidacja czy plik istnieje
					if (!PfUtils.fileExist(filePathName))
						continue;
				// ladowanie pliku
					fileContent = FileUtils.readFileToString(new File(filePathName), TC_CHARSET);
					if (fileContent.length() == 0)
						throw new IOException("Blad odczytu pliku z danymi wejsciowymi '" + filePathName + "' - pusty plik");
					fileContent = fileContent.replace("\\", "\\\\");
				// parsowanie obiektu JSON z uwzglednieniem zamieszczenia tablicy na dowolnym poziomie zagniezdzenia dowolnego obiektu JSON
					JSONfileContent = new JSONObject(fileContent);
					for (String keyL1 : JSONObject.getNames(JSONfileContent)) {
						parseJSON(keyL1.toLowerCase(), JSONfileContent.get(keyL1));
						
/*//chcac ograniczyc i uproscic odczyt jedynie do jednego poziomu wglab pliku JSON usunac linie powyzej, usunac metode parseJSON() i odkomentowac kod ponizej:
						System.out.println(keyL1);
						if (JSONfileContent.get(keyL1) instanceof JSONObject) {
							JSONObject JSONobjL2 = JSONfileContent.getJSONObject(keyL1);
							for (String keyL2 : JSONObject.getNames(JSONobjL2)) {
								System.out.println("\t" + keyL1.toLowerCase() + "|" + keyL2.toLowerCase());
								TCparametersIn.put(keyL1.toLowerCase() + "|" + keyL2.toLowerCase(), JSONobjL2.getString(keyL2));
							}
						} else if (JSONfileContent.get(keyL1) instanceof JSONArray) {
							JSONArray JSONarrayL1 = JSONfileContent.getJSONArray(keyL1);
							int records = JSONarrayL1.length();
							for (int i = 0; i < records; i++) {
								JSONObject JSONobjL2 = JSONarrayL1.getJSONObject(i);
								for (String keyL2 : JSONObject.getNames(JSONobjL2)) {
									System.out.println("\t" + keyL1.toLowerCase() + "|" + i + "|" + keyL2.toLowerCase());
									TCparametersIn.put(keyL1.toLowerCase() + "|" + i + "|" + keyL2.toLowerCase(), (String) JSONobjL2.getString(keyL2));
							}	}
						} else {
							TCparametersIn.put(keyL1.toLowerCase(), (String) JSONfileContent.getString(keyL1));
						}
*/
					}
					JSONfileContent = null;
				} catch(IOException e) {
					throw new IOException("Blad odczytu pliku z danymi wejsciowym '" + filePathName + "' - brak pliku");
				} catch(JSONException e) {
					throw new JSONException("Nieprawidlowy format JSON w pliku wejsciowym '" + filePathName + "'");
				}
			}
		}
		
		
		/**
		 * Parsowanie obiektu/tablicy JSON lub bezposredniego String'a.
		 * @param keyName
		 * 		nazwa klucza do zamieszczenia w tablicy parametrow
		 * @param obj
		 * 		obiekt/tablica JSON lub odczytany String z obiektu JSON
		 * @throws JSONException
		 * 		blad parsowania zawartosci obiektu
		 */
		private void parseJSON(String keyName, Object obj) throws JSONException {
			JSONObject JSONobj; 
			System.out.println(keyName);
			if (obj instanceof JSONObject) {
				JSONobj = (JSONObject) obj;
				for (String key : JSONObject.getNames(JSONobj)) {
					System.out.println("\t" + keyName.toLowerCase() + "|" + key.toLowerCase());
					parseJSON(keyName.toLowerCase() + "|" + key.toLowerCase(), JSONobj.get(key));
				}
			} else if (obj instanceof JSONArray) {
				JSONArray JSONarray = (JSONArray) obj;
				int records = JSONarray.length();
				for (int i = 0; i < records; i++) {
					JSONobj = JSONarray.getJSONObject(i);
					for (String key : JSONObject.getNames(JSONobj)) {
						System.out.println("\t" + keyName.toLowerCase() + "|" + i + "|" + key.toLowerCase());
						parseJSON(keyName.toLowerCase() + "|" + key.toLowerCase(), JSONobj.get(key));
				}	}
			} else {
				TCparametersIn.put(keyName.toLowerCase(), (String) obj);
			}
		}
			
		
		/**
		 * Odczyt wartosci ze struktury parametrow w kolejnosci:
		 * <ol>
		 * <li>zmieniony</li>
		 * <li>wejsciowy</li>
		 * </ol>
		 * @param parameterName
		 * 		nazwa parametru
		 * @return
		 * 		wartosc parametru
		 */
		public String getParameter(String parameterName) {
			
		// odczyt ze struktury parametrow zmodyfikowanych
			if (TCparametersOut.size() > 0 && TCparametersOut.containsKey(parameterName)) {
				return TCparametersOut.get(parameterName);
		// odczyt ze struktury parametrow wejsciowych
			} else if (TCparametersIn.containsKey(parameterName)) {
				return this.TCparametersIn.get(parameterName);
		// brak parametru
			} else {
				throw new IllegalArgumentException("Brak parametru: " + parameterName);
			}
		}
		
		
		/**
		 * Walidacja czy struktura parametrow zmienionych lub wejsciowych zawiera szukany klucz.
		 * @param parameterName
		 * 		nazwa parametru
		 * @return
		 * 		<code>true</code> = struktura zawiera podany parametr
		 */
		public boolean hasParameter(String parameterName) {
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
		 * Zapis parametru do struktury parametrow: customowego lub zmieniajacego wartosci parametru wejsciowego.
		 * @param parameterName
		 * 		nazwa (nowego/istniejacego) parametru
		 * @param parameterValue
		 * 		nowa wartosc parametru
		 */
		public void setParameter(String parameterName, String parameterValue) {
			if (parameterName != null)
				TCparametersOut.put(parameterName, parameterValue);
		}
		
		
		/**
		 * Zapis parametrow customowych powstalych podczas trwania TC do pliku JSON.<br/>
		 * Gdy brak parametrow customowych plik nie jest zapisywany.
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
			TC_PATH = TC_PATH + "result" + System.getProperty("file.separator");
		// zapis pliku
			try {
			// walidacja czy jest istniejacy do usuniecia
				if (PfUtils.fileExist(TC_PATH + fileName))
					Files.delete(Paths.get(TC_PATH + fileName));
			// zapis nowego pliku
				FileUtils.writeStringToFile(new File(TC_PATH + fileName), result.toString(), TC_CHARSET.toString());
			} catch (IOException e) {
				throw new IOException("Blad zapisu pliku z parametrami wyjsciowymi '" + TC_PATH + fileName + "' - brak dostepu do zasobu dyskowego");
			}
		}
		
		
		/**
		 * Zapis parametrow wyjsciowych TC w ustrukturyzowany JSON wyjsciowy do pliku o podanej nazwie (nazwa + rozszerzenie).
		 * @param status
		 * 		status TC
		 * @param resultMessage
		 * 		komunikat wyjsciowy
		 * @param stackTrace
		 * 		zrzut stosu w przypadku bledu
		 * @param causedBy
		 * 		przyczyna bledu - komunikat uzytkownika
		 * @param screenshot
		 * 		nazwa pliku ze zrzutem ekranowym
		 * @throws IOException
		 * 		blad dostepu do zasobu dyskowego
		 * @throws JSONException
		 * 		blad generowania struktury JSONa wyjsciowego
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
			res.put("error", errors);
			if (!TCparametersOut.isEmpty())
				res.put("returns", new JSONObject(TCparametersOut));
			String resultPath = TC_PATH + "result" + System.getProperty("file.separator");
		// zapis pliku
			try {
			// walidacja czy jest istniejacy do usuniecia
				if (PfUtils.fileExist(resultPath + fileName))
					Files.delete(Paths.get(resultPath + fileName));
			// zapis nowego pliku
				FileUtils.writeStringToFile(new File(resultPath + fileName), res.toString(), TC_CHARSET.toString());
			} catch (IOException e) {
				throw new IOException("Blad zapisu pliku z wynikiem dzialania Uslugi '" + resultPath + fileName + "' - brak dostepu do zasobu dyskowego");
	    	}
		}
		
		
		/**
		 * Zwrocenie mapy parametrow wejsciowych.
		 * @return
		 * 		Mapa
		 */
		public Map<String,String> getMapParams() {
			return this.TCparametersIn;
		}
	

}