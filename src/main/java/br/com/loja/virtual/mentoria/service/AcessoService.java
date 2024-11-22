package br.com.loja.virtual.mentoria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.model.Acesso;
import br.com.loja.virtual.mentoria.repository.AcessoRepository;

@Service
public class AcessoService {

	@Autowired
	private AcessoRepository acessoRepository;

	public Acesso salvarAcesso(Acesso acesso) {

		// Chama a interface de repositório e salva no banco de dados
		return acessoRepository.save(acesso);

	}

}
