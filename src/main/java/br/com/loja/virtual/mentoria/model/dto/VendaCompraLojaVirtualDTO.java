package br.com.loja.virtual.mentoria.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.loja.virtual.mentoria.model.Endereco;
import br.com.loja.virtual.mentoria.model.Pessoa;

public class VendaCompraLojaVirtualDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private BigDecimal valorTotal;

	private BigDecimal valorDesconto;

	private Pessoa pessoa;

	private Endereco enderecoCobranca;

	private Endereco enderecoEntrega;

	private BigDecimal valorFrete;

	private List<ItemVendaLojaDTO> itemVendaLojas = new ArrayList<ItemVendaLojaDTO>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Endereco getEnderecoCobranca() {
		return enderecoCobranca;
	}

	public void setEnderecoCobranca(Endereco enderecoCobranca) {
		this.enderecoCobranca = enderecoCobranca;
	}

	public Endereco getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(Endereco enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public List<ItemVendaLojaDTO> getItemVendaLojas() {
		return itemVendaLojas;
	}

	public void setItemVendaLojas(List<ItemVendaLojaDTO> itemVendaLojas) {
		this.itemVendaLojas = itemVendaLojas;
	}

}
