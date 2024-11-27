package br.com.loja.virtual.mentoria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServiceContagemAcessoApi {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void atualizaAcessoEndPointPF() {

		// Atualizando a tabela de acesso ao End-Point
		jdbcTemplate.execute(
				"begin; update acesso_end_point set qtd = qtd + 1 where nome = 'buscarNomeCadastradoPf'; commit;");

	}

}
