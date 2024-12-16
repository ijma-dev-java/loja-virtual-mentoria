package br.com.loja.virtual.mentoria.enums;

public enum StatusContaReceber {

	COBRANCA("Cobrança"), 
	VENCIDA("Vencida"),
	ABERTA("Aberta"),
	QUITADA("Quitada");

	// Atributo necessário para mostrar na tela
	private String descricao;

	// Construtor
	private StatusContaReceber(String descricao) {
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
