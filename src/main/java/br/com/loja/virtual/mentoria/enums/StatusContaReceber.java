package br.com.loja.virtual.mentoria.enums;

public enum StatusContaReceber {

	// Valores
	COBRANCA("Pagar"), VENCIDA("Vencida"), ABERTA("Aberta"), QUITADA("Quitada");

	// Obrigatório ter um campo descrição para que possa ser mostrado na tela
	private String descricao;

	// Criação do construtor - Obrigatório
	// Pega o valor do atributo que vem do parametro do construtor
	private StatusContaReceber(String descricao) {
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
