package br.com.loja.virtual.mentoria.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class PessoaController {

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaUsuarioService pessoaUsuarioService;

	@ResponseBody
	@PostMapping(value = "salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica)
			throws LojaVirtualMentoriaException {

		// Utilizado em sistemas antigos
		// Verificando se o nome está null ou vazio
		/*
		if (pessoaJuridica.getNome() == null || pessoaJuridica.getNome().trim().isEmpty()) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("Nome da empresa deve ser informado");

		}
		*/

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

		// Verificando se o CPNJ é valido
		if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new LojaVirtualMentoriaException("CNPJ : " + pessoaJuridica.getCnpj() + " está inválido.");
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

	@ResponseBody
	@PostMapping(value = "salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica)
			throws LojaVirtualMentoriaException {

		// Verificando se a pessoa jurifica está nulo
		if (pessoaFisica == null) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("Pessoa física não pode ser NULL");

		}

		// Verificando se o id da pessoa fisica está nulo
		// e se existe CPF cadastro com o mesmo número
		if (pessoaFisica.getId() == null && pessoaFisicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());

		}

		// Verificando se o CPF é valido
		if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new LojaVirtualMentoriaException("CPF : " + pessoaFisica.getCpf() + " está inválido.");
		}

		// Invoca o PessoaUsuarioService para salvar no banco de dados
		pessoaFisica = pessoaUsuarioService.salvarPessoaFisica(pessoaFisica);

		// Retorna o pessoa juridica salvo
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);

	}

}
