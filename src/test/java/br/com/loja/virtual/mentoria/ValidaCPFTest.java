package br.com.loja.virtual.mentoria;

import br.com.loja.virtual.mentoria.util.ValidaCPF;

public class ValidaCPFTest {

	public static void main(String[] args) {

		// Número de retornando em boolean
		boolean isCpf = ValidaCPF.isCPF("346.145.320-04");

		// Mostra o resultado no console
		System.out.println("CPF válidado: " + isCpf);

	}

}
