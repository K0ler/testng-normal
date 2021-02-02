//Bank Zachodni WBK, Centrum Automatyzacji Testow, Mateusz Lazanowski, 03-09-2018
package utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class DeCrypt {
	
	// pola klasy
		List<Integer> zaszyfrowane_binarnie = new ArrayList<Integer>();
	
	
	/**
	 * Generuje tablicę do odhaszowania hasła.
	 * @param klucz
	 * @return
	 */
	private int[] generacja_klucza(String klucz) {
		int main[] = new int [256];
		int support[] = new int [256];
		char[] znaki = klucz.toCharArray();
		for (int i = 0; i <= 255; i++) {
			main[i] = i;
			support[i] = znaki[i%znaki.length];
		}
		int j = 0;
		int tmp;
		for (int i = 0; i <= 255; i++) {
			j = (j + main[i] + support[i]) % 256;
			tmp = main[j];
			main[j] = main[i];
			main[i] = tmp;
		}
		return main;
	}
	
	
	/**
	 * OdHexowuje hasło do postaci tablicy int'ów.
	 * @param haslo
	 * @return
	 */
	private List<Integer> hex2int(String haslo) {
		List<Integer> tablica_hasla = new ArrayList<Integer>();
		char[] literka = haslo.toCharArray();
		String hex;
		for (int i = 0; i < haslo.length(); i = i+2) {
			hex = literka[i] + "" + literka[i +1];
			tablica_hasla.add(Integer.parseInt(hex, 16));
		}
		return tablica_hasla;
	}
	
	
	/**
	 * Dekoder hasła.
	 * @param zaszyfrowane_haslo_hex
	 * @param klucz
	 * @return
	 */
	public String deCrypt(String zaszyfrowane_haslo_hex, String klucz) {
		int[] wyjscie = generacja_klucza(klucz);
		int i1 = 0;
		int j = 0;
		int tmp;
		String wynik = "";
		List<Integer> zaszyfrowane_binarnie;
		zaszyfrowane_binarnie = hex2int(zaszyfrowane_haslo_hex);
		for (int z = 0; z < zaszyfrowane_binarnie.size(); z++) {
			i1 = (i1 + 1) % 256;
			j = (j + wyjscie[i1]) % 256;
			tmp = wyjscie[j];
			wyjscie[j] = wyjscie[i1];
			wyjscie[i1] = tmp;
			int hasz = wyjscie[(wyjscie[i1] + wyjscie[j]) % 256];
			int odszyfrowane_do_unicode = (zaszyfrowane_binarnie.get(z) ^ hasz);
			char literka_hasla = (char) odszyfrowane_do_unicode;
			wynik = wynik + "" + literka_hasla;
		}
		return wynik;
	}
	
	
	public static String decryptXOR(String message, String key){
		try {
			if (message == null || key == null ) return null;
			char[] keys = key.toCharArray();
			char[] mesg = new String(Base64.getDecoder().decode(message)).toCharArray();
			int ml = mesg.length;
			int kl = keys.length;
			char[] newmsg = new char[ml];
			for (int i = 0; i < ml; i++)
				newmsg[i] = (char)(mesg[i] ^ keys[i % kl]);
			mesg = null;
			keys = null;
			return new String(newmsg);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	
}
