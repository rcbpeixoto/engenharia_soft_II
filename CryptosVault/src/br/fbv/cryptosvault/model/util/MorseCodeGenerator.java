package br.fbv.cryptosvault.model.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Project: Cryptos Vault Class: MorseCodeGenerator REVISION HISTORY Date          Developer       Comment ----------    -------------------------------------------- ---------------------------------------------- ##/##/####    "Erivan Nogueira" <erivan.spe@gmail.com>   	  Initial draft 30/10/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>   	  Code Revision ----------    -------------------------------------------- ----------------------------------------------
 */
public class MorseCodeGenerator {

	private String text;

	/**
	 * @uml.property  name="morseCode"
	 * @uml.associationEnd  
	 */
	private MorseCode morseCode;

	/**
	 * Construtor vazio
	 * 
	 * @author Erivan
	 */
	public MorseCodeGenerator() {
		morseCode = new MorseCode();

	}

	/**
	 * Contrutor com um parametro
	 * 
	 * @author Erivan
	 */
	public MorseCodeGenerator(String text) {
		morseCode = new MorseCode();
		this.setTexto(text);
	}

	/**
	 * Fornece o texto usado nas operacoes de conversao
	 * 
	 * @author Erivan
	 * @param texto
	 */
	public void setTexto(String text) {
		this.text = text;
	}

	/**
	 * Retorna o texto usado nas operacoes de conversao
	 * 
	 * @author Erivan
	 * @return String
	 */
	public String getTexto() {
		return this.text;
	}

	/**
	 * Analiza os caracteres da mensagem e substitui pelas respectivas
	 * sequencias em codigo morse
	 * 
	 * @author Erivan
	 * @return String
	 */
	public String generateMorse() {
		StringBuilder retorno = new StringBuilder();

		if (this.text != null || !this.text.equals("")) {
			char[] brokenText = this.text.toUpperCase().toCharArray();

			for (int i = 0; i < brokenText.length; i++) {
				String letra = this.morseCode.getMorse(brokenText[i]);

				if (letra != null && !letra.equals("")) {
					retorno.append(letra + " ");
				} else {
					retorno.append(" ");
				}
			}
		}
		return retorno.toString().trim();
	}

	/**
	 * Converte a String passada por parametro em uma sequencia de numeros usada
	 * para fazer o celular vibrar conforme a sequencia
	 * 
	 * @author Erivan
	 * @param morseCode
	 * @return long[]
	 */
	public long[] vibrationConverter(String morseCode) {
		long[] rtn;
		if (MorseCode.validateMorseCode(morseCode)) {
			rtn = this.getLongArray(morseCode.trim());
		} else {
			this.setTexto(morseCode);
			String s = this.generateMorse();
			rtn = this.getLongArray(s);
		}
		return rtn;
	}

	/**
	 * Metodo privado que faz efetivamente quebra a String e gera o vetor com os
	 * numeros correspondentes e sequencia fornecida.
	 * 
	 * @author Erivan
	 * @param codigoMorse
	 * @return long[]
	 */
	private long[] getLongArray(String codigoMorse) {
		long[] rtn;
		char[] carac = codigoMorse.toCharArray();
		ArrayList<Long> array = new ArrayList<Long>();
		array.add(5L);

		for (int i = 0; i < carac.length; i++) {
			if (Character.toString(carac[i]).equals("-")) {
				array.add(new Long(MorseCode.TIME_DASH));
				array.add(new Long(MorseCode.TIME_GAP_SYMBOLS));

			} else if (Character.toString(carac[i]).equals(".")) {
				array.add(new Long(MorseCode.TIME_DOT));
				array.add(new Long(MorseCode.TIME_GAP_SYMBOLS));
				/*
				 * Se os caracteres anterior e posterior forem um ponto ou um
				 * traco entao o caractere encontrado e o final de uma
				 * palavra. Sendo assim, insere-se um espaco de tempo entre palavras;
				 */
			} else if (Character.toString(carac[i]).equals(" ")) {
				if ((Character.toString(carac[i - 1]).equals(".") || Character
						.toString(carac[i - 1]).equals("-"))
						&& (Character.toString(carac[i + 1]).equals(".") || Character
								.toString(carac[i + 1]).equals("-"))) {
					array.add(1L);
					array.add(new Long(MorseCode.TIME_GAP_LETTERS));

				} else if (Character.toString(carac[i + 1]).equals(" ")) {
					array.add(1L);
					array.add(new Long(MorseCode.TIME_GAP_WORDS));
				}
			}
		}

		rtn = new long[array.size()];
		int i = 0;
		for (Iterator<Long> iterator = array.iterator(); iterator.hasNext();) {
			Long long1 = (Long) iterator.next();
			rtn[i] = long1.longValue();
			i++;
		}

		return rtn;
	}
}
