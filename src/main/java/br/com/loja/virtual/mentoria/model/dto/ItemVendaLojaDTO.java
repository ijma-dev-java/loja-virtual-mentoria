package br.com.loja.virtual.mentoria.model.dto;

import java.io.Serializable;

import br.com.loja.virtual.mentoria.model.Produto;

public class ItemVendaLojaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double qtd;

	private Produto produto;

	public Double getQtd() {
		return qtd;
	}

	public void setQtd(Double qtd) {
		this.qtd = qtd;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

}
