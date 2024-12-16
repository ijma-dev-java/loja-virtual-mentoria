package br.com.loja.virtual.mentoria.enums;

public enum TipoEndereco {

	COBRANCA("Cobrança"), ENTREGA("Entrega");

	// Atributo necessário para mostrar na tela
	private String descricao;

	// Construtor
	private TipoEndereco(String descricao) {
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
