package br.com.loja.virtual.mentoria.model.dto;

import java.io.Serializable;

public class CategoriaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String descricao;

	private String empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

}
