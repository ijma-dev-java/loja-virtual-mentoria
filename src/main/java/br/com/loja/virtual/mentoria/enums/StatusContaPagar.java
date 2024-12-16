package br.com.loja.virtual.mentoria.enums;

public enum StatusContaPagar {

	COBRANCA("Cobrança"), 
	VENCIDA("Vencida"),
	ABERTA("Aberta"),
	QUITADA("Quitada"),
	ALUGUEL("Aluguel"),
	FUNCIONARIO("Funcionário"),
	NEGOCIADA("Renegociada");

	// Atributo necessário para mostrar na tela
	private String descricao;

	// Construtor
	private StatusContaPagar(String descricao) {
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
