package br.fbv.cryptosvault.model.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Cryptos Vault
 * Class: MorseCode
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * ##/##/####    "Erivan Nogueira" <erivan.spe@gmail.com>   	  Initial draft
 * 30/10/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>   	  Code Revision
 * ----------    -------------------------------------------- ----------------------------------------------
 */
public class MorseCode {

	/**
	 * Valores de tempo definidos em 'ms'
	 */
	public static final long TIME_GAP_WORDS = 700;

	public static final long TIME_GAP_LETTERS = 400;

	public static final long TIME_DASH = 300;

	public static final long TIME_DOT = 100;

	public static final long TIME_GAP_SYMBOLS = 170;

	/**
	 * Mensagens especiais
	 */
	/* Mensagem de socorro*/
	public static final String SOS = "...---...";

	/* Parar; fim da mensagem */
	public static final String AR = ".-.-.";

	/* Espere por 10 segundos */
	public static final String AS = ".-...";

	/* Separador dentro da Mensagem */
	public static final String BT = "-...-";

	/* Saindo do ar */
	public static final String CL = "-.-..-..";

	/**
	 * Atributos
	 */
	private Map<String, String> letters;

	private String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"0" };

	private String[] morseCode = { 
	".-", /* A */
	"-...", /* B */
	"-.-.", /* C */
	"-..", /* D */
	".", /* E */
	"..-.", /* F */
	"--.", /* G */
	"....", /* H */
	"..", /* I */
	".---", /* J */
	"-.-", /* K */
	".-..", /* L */
	"--", /* M */
	"-.", /* N */
	"---", /* O */
	".--.", /* P */
	"--.-", /* Q */
	".-.", /* R */
	"...", /* S */
	"-", /* T */
	"..-", /* U */
	"...-", /* V */
	".--", /* W */
	"-..-", /* X */
	"-.--", /* Y */
	"--..", /* Z */
	".----",/* 1 */
	"..---",/* 2 */
	"...--",/* 3 */
	"....-",/* 4 */
	".....",/* 5 */
	"-....",/* 6 */
	"--...",/* 7 */
	"---..",/* 8 */
	"----.",/* 9 */
	"-----"/* 0 */};

	/**
	 * Construtor vazio
	 */
	public MorseCode() {
		this.initLetters();
	}

	/**
	 * Inclui as letras no Map para busca
	 * 
	 * @author Erivan
	 */
	private void initLetters() {
		this.letters = new HashMap<String, String>();

		for (int i = 0; i < alphabet.length; i++) {
			this.letters.put(alphabet[i], morseCode[i]);
		}
	}

	/**
	 * Retorna a sequencia codificada equivalente ao caractere informado
	 * 
	 * @author Erivan
	 * @return String
	 * @param caractere
	 */
	public String getMorse(char caractere) {

		if (this.letters == null) {
			this.initLetters();
		}

		String carac = String.valueOf(caractere);
		return this.letters.get(carac);
	}

	/**
	 * Verifica se a string passada por parametro � uma sequencia de c�digo
	 * morse
	 * 
	 * @author Erivan
	 * @param codigoMorse
	 * @return boolean
	 */
	public static boolean validateMorseCode(String codigoMorse) {
		boolean rtn = true;
		char[] morse = codigoMorse.trim().toCharArray();

		for (int i = 0; i < morse.length; i++) {
			if (Character.isLetter(morse[i])) {
				rtn = false;
			}
		}
		return rtn;
	}

}
