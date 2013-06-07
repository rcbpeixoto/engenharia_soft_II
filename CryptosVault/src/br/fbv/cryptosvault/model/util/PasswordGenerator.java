package br.fbv.cryptosvault.model.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Project: Cryptos Vault
 * Class: PasswordGenerator
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * ##/##/####    "Erivan Nogueira" <erivan.spe@gmail.com>   	  Initial draft
 * 30/10/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>   	  Code Revision
 * ----------    -------------------------------------------- ----------------------------------------------
 */
public class PasswordGenerator {

	private Random random = new Random();

	private boolean lowCase = true;
	
	private final int LOW_CASE = 1;
	
	private String[] lowCaseLettersArray = { "a", "b", "c", "d", "e", "f", "g",
			"h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
			"u", "v", "w", "x", "y", "z" };

	private boolean high_case = false;
	
	private final int HIGH_CASE = 2;
	
	private String[] highCaseLettersArray = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	private boolean numbers = false;
	
	private final int NUMBERS = 3;
	
	private String[] numbersArray = { "0", "1", "2", "3", "4", "5", "6", "7", "8","9", "0" };
	
	private boolean special = false;
	private final int SPECIAL = 4;
	private String[] specialCharsArray = { "!", "@", "#", "$", "�", "%", "�","&", "*", "�" };

	private boolean others = false;
	private final int OTHERS = 5;
	private String[] othersCharsArray = { ",", ".", ";", ":", "{", "}", "[", "]", "�","�", 
			"/", "�", "?", "\\", "|", "'", "\"", "=", "+", "-", "_", "(",")" };

	/**
	 * Construtor vazio
	 * 
	 * @author Erivan
	 */
	public PasswordGenerator() {

	}
	

	/**
	 * Construtor
	 * 
	 * @author Erivan
	 * 
	 * @param lowCaseLetters
	 * @param highCaseLetters
	 * @param numbers
	 * @param special
	 * @param others
	 */
	public PasswordGenerator(boolean lowCaseLetters, boolean highCaseLetters,
			boolean numbers, boolean special, boolean others) {

		this.setConfig(lowCaseLetters, highCaseLetters, numbers, special,
				others);

	}

	/**
	 * Gera uma sequencia de caracteres de acordo com a configuracao passada
	 * @author Erivan
	 * @param size
	 * @return Senha
	 * */
	public String generatePassword(int size) {
		if(!(lowCase || high_case || numbers || special || others)){
			this.lowCase = true;
		}
		StringBuilder builder = new StringBuilder();

		ArrayList<Integer> list = new ArrayList<Integer>();
		if (this.lowCase) {
			list.add(new Integer(this.LOW_CASE));
		}
		if (this.high_case) {
			list.add(new Integer(this.HIGH_CASE));
		}
		if (this.special) {
			list.add(new Integer(this.SPECIAL));
		}
		if (this.numbers) {
			list.add(new Integer(this.NUMBERS));
		}
		if (this.others) {
			list.add(new Integer(this.OTHERS));
		}

		int cont = 0;
		while (cont != size) {
			int i = this.generateIntervals(list);

			switch (i) {
			case 1:// indentificador letras menusculas
				builder.append(this.lowCaseLettersArray[random
						.nextInt(this.lowCaseLettersArray.length - 1)]);
				cont++;
				break;
			case 2:// indentificador letras maiusculas
				builder.append(this.highCaseLettersArray[random
						.nextInt(this.highCaseLettersArray.length - 1)]);
				cont++;
				break;
			case 3:// indentificador numerais
				builder.append(this.numbersArray[random
						.nextInt(this.numbersArray.length - 1)]);
				cont++;
				break;
			case 4:// indentificador caracteres especiais
				builder.append(this.specialCharsArray[random
						.nextInt(this.specialCharsArray.length - 1)]);
				cont++;
				break;
			case 5:// indentificador outros
				builder.append(this.othersCharsArray[random
						.nextInt(this.othersCharsArray.length - 1)]);
				cont++;
				break;
			}

		}

		return builder.toString();
	}

	private int generateIntervals(ArrayList<Integer> interval) {
		return interval.get(random.nextInt(interval.size())).intValue();
	}

	/**
	 * @author Erivan Seta os parametros para os tipos de caracteres envolvidos
	 *         na chave.
	 * 
	 * @param lowCaseLetters
	 * @param highCaseLetters
	 * @param numbers
	 * @param special
	 * @param others
	 */
	private void setConfig(boolean lowCaseLetters, boolean highCaseLetters,
			boolean numbers, boolean special, boolean others) {

		this.lowCase = lowCaseLetters;
		this.high_case = highCaseLetters;
		this.numbers = numbers;
		this.special = special;
		this.others = others;
	}

}
