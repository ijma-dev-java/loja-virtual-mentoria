package br.com.loja.virtual.mentoria.enums;

public enum StatusVendaCompraLojaVirtual {

	// Valores
	FINALIZADA("Finalizada"), CANCELADA("Cancelada"), ABANDONOU_CARRINHO("Abandonou Carrinho");

	// Obrigatório ter um campo descrição para que possa ser mostrado na tela
	private String descricao;

	// Criação do construtor - Obrigatório
	// Pega o valor do atributo que vem do parametro do construtor
	private StatusVendaCompraLojaVirtual(String descricao) {
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
