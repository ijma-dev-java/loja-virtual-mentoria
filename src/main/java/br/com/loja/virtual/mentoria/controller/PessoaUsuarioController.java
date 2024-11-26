package br.com.loja.virtual.mentoria.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.PessoaFisica;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.repository.PessoaFisicaRepository;
import br.com.loja.virtual.mentoria.repository.PessoaJuridicaRepository;
import br.com.loja.virtual.mentoria.service.PessoaUsuarioService;
import br.com.loja.virtual.mentoria.util.ValidaCNPJ;
import br.com.loja.virtual.mentoria.util.ValidaCPF;

@RestController
@Controller
public class PessoaUsuarioController {

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private PessoaUsuarioService pessoaUsuarioService;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarPessoaJuridica")
	public ResponseEntity<PessoaJuridica> salvarPessoaJuridica(@RequestBody @Valid PessoaJuridica pessoaJuridica)
			throws LojaVirtualMentoriaException {

		// Validando se a pessoaJuridica está null ou vazio - FORMA ANTIGA
		/*
		 * if (pessoaJuridica.getNome() == null ||
		 * pessoaJuridica.getNome().trim().isEmpty()) { // Mostrando a mensagem que a
		 * pessoaJuridica não pode ser NULL nem vazio throw new
		 * LojaVirtualMentoriaException("Informe o nome da pessoa jurídica"); }
		 * 
		 */

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
			// Mostrando a mensagem que já existe IE cadastrado
			throw new LojaVirtualMentoriaException(
					"Já existe Inscrição estadual cadastrado com o número: " + pessoaJuridica.getInscricaoEstadual());
		}

		// Validando se o CNPJ é válido
		if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			// Mostrando a mensagem que CNPJ não é válido
			throw new LojaVirtualMentoriaException("CNPJ: " + pessoaJuridica.getCnpj() + " está inválido");
		}

		// Chamando o PessoaUsuarioService
		pessoaJuridica = pessoaUsuarioService.salvarPessoaJuridica(pessoaJuridica);

		// Retorna pessoaJuridica salvo
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "**/salvarPessoaFisica")
	public ResponseEntity<PessoaFisica> salvarPessoaFisica(@RequestBody PessoaFisica pessoaFisica)
			throws LojaVirtualMentoriaException {

		// Validando se a pessoaFisica está null
		if (pessoaFisica == null) {
			// Mostrando a mensagem que a pessoaFisica não pode ser NULL
			throw new LojaVirtualMentoriaException("Pessoa Física não pode ser NULL");
		}

		// Validando se a pessoaFisica está com o id null
		// e se existe o CPF cadastrado
		if (pessoaFisica.getId() == null && pessoaFisicaRepository.buscarCpfCadastrado(pessoaFisica.getCpf()) != null) {
			// Mostrando a mensagem que já existe CPF cadastrado
			throw new LojaVirtualMentoriaException("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());
		}

		// Validando se o CPF é válido
		if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			// Mostrando a mensagem que CPF não é válido
			throw new LojaVirtualMentoriaException("CPF: " + pessoaFisica.getCpf() + " está inválido");
		}

		// Chamando o PessoaUsuarioService
		pessoaFisica = pessoaUsuarioService.salvarPessoaFisica(pessoaFisica);

		// Retorna pessoaJuridica salvo
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);

	}

}
