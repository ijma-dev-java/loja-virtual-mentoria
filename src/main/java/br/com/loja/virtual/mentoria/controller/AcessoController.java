package br.com.loja.virtual.mentoria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.loja.virtual.mentoria.model.Acesso;
import br.com.loja.virtual.mentoria.service.AcessoService;

@Controller
public class AcessoController {

	@Autowired
	private AcessoService acessoService;

	@PostMapping(value = "**/salvarAcesso")
	public Acesso salvarAcesso(Acesso acesso) {
		
		// Chama a classe de serviço
		return acessoService.salvarAcesso(acesso);

	}

}
