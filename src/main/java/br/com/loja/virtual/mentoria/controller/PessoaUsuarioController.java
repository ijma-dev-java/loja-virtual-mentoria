package br.com.loja.virtual.mentoria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.repository.PessoaJuridicaRepository;
import br.com.loja.virtual.mentoria.service.PessoaUsuarioService;

@RestController
@Controller
public class PessoaUsuarioController {

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private PessoaUsuarioService pessoaUsuarioService;

	@ResponseBody
	@PostMapping(value = "**/salvarPessoaJuridica")
	public ResponseEntity<PessoaJuridica> salvarPessoaJuridica(@RequestBody PessoaJuridica pessoaJuridica)
			throws LojaVirtualMentoriaException {

		// Validando se a pessoaJuridica está null
		if (pessoaJuridica == null) {
			// Mostrando a mensagem que a pessoaJuridica não pode ser NULL
			throw new LojaVirtualMentoriaException("Pessoa Jurídica não pode ser NULL");
		}

		// Validando se a pessoaJuridica está com o id null
		// e se existe o CNPJ cadastrado
		if (pessoaJuridica.getId() == null
				&& pessoaJuridicaRepository.buscarCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			// Mostrando a mensagem que já existe CNPJ cadastrado
			throw new LojaVirtualMentoriaException(
					"Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
		}

		// Validando se a pessoaJuridica está com o id null
		// e se existe o IE cadastrado
		if (pessoaJuridica.getId() == null && pessoaJuridicaRepository
				.buscarInscricaoEstadualCadastrado(pessoaJuridica.getInscricaoEstadual()) != null) {
			throw new LojaVirtualMentoriaException(
					"Já existe Inscrição estadual cadastrado com o número: " + pessoaJuridica.getInscricaoEstadual());
		}

		// Chamando o PessoaUsuarioService
		pessoaJuridica = pessoaUsuarioService.salvarPessoaJuridica(pessoaJuridica);

		// Retorna pessoaJuridica salvo
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);

	}

}
