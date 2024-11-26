package br.com.loja.virtual.mentoria;

import br.com.loja.virtual.mentoria.util.ValidaCNPJ;

public class ValidaCNPJTest {

	public static void main(String[] args) {

		// Número de retornando em boolean
		boolean isCnpj = ValidaCNPJ.isCNPJ("39.192.998/0001-09");

		// Mostra o resultado no console
		System.out.println("CNPJ válidado: " + isCnpj);

	}

}
