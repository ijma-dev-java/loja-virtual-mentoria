package br.com.loja.virtual.mentoria.enums;

public enum TipoPessoa {

	// Valores
	FÍSICA("Física"), JURIDICA("Jurídica"), JURIDICA_FORNECEDOR("Jurídica e Fornecedor");

	// Obrigatório ter um campo descrição para que possa ser mostrado na tela
	private String descricao;

	// Criação do construtor - Obrigatório
	// Pega o valor do atributo que vem do parametro do construtor
	private TipoPessoa(String descricao) {
		this.descricao = descricao;
	}

	// Caso precise mostrar em algum local do projeto
	public String getDescricao() {
		return descricao;
	}

	// Valor padrão para ser mostrado na tela
	@Override
	public String toString() {
		return this.descricao;
	}

}
