package br.com.loja.virtual.mentoria.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class VendaCompraLojaVirtualDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal valorTotal;

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

}
