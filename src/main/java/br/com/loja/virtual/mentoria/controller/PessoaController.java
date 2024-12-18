package br.com.loja.virtual.mentoria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.repository.PessoaJuridicaRepository;
import br.com.loja.virtual.mentoria.service.PessoaUsuarioService;

@RestController
public class PessoaController {

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private PessoaUsuarioService pessoaUsuarioService;

	@ResponseBody
	@PostMapping(value = "salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica)
			throws LojaVirtualMentoriaException {

		// Verificando se a pessoa jurifica está nulo
		if (pessoaJuridica == null) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("Pessoa juridica não pode ser NULL");

		}

		// Verificando se o id da pessoa jurifica está nulo
		// e se existe CNPJ cadastro com o mesmo número
		if (pessoaJuridica.getId() == null
				&& pessoaJuridicaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException(
					"Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());

		}

		// Verificando se o id da pessoa jurifica está nulo
		// e se existe IE cadastro com o mesmo número
		if (pessoaJuridica.getId() == null
				&& pessoaJuridicaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException(
					"Já existe IE cadastrado com o número: " + pessoaJuridica.getInscEstadual());

		}

		// Invoca o PessoaUsuarioService para salvar no banco de dados
		pessoaJuridica = pessoaUsuarioService.salvarPessoaJuridica(pessoaJuridica);

		// Retorna o pessoa juridica salvo
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);

	}

}
