package br.com.loja.virtual.mentoria.enums;

public enum TipoPessoa {

	JURIDICA("Jurídica"), JURIDICA_FORNECEDOR("Jurídica e Fornecedor"), FISICA("Física");

	// Atributo necessário para mostrar na tela
	private String descricao;

	// Construtor
	private TipoPessoa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	// Mostrar na tela
	@Override
	public String toString() {
		return this.descricao;
	}

}
