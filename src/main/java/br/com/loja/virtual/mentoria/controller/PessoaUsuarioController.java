package br.com.loja.virtual.mentoria.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.enums.TipoPessoa;
import br.com.loja.virtual.mentoria.model.Endereco;
import br.com.loja.virtual.mentoria.model.PessoaFisica;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.model.dto.CepDTO;
import br.com.loja.virtual.mentoria.model.dto.ConsultaCnpjDTO;
import br.com.loja.virtual.mentoria.repository.EnderecoRepository;
import br.com.loja.virtual.mentoria.repository.PessoaFisicaRepository;
import br.com.loja.virtual.mentoria.repository.PessoaJuridicaRepository;
import br.com.loja.virtual.mentoria.service.PessoaUsuarioService;
import br.com.loja.virtual.mentoria.service.ServiceContagemAcessoApi;
import br.com.loja.virtual.mentoria.util.ValidaCNPJ;
import br.com.loja.virtual.mentoria.util.ValidaCPF;

@RestController
public class PessoaUsuarioController {

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private PessoaUsuarioService pessoaUsuarioService;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ServiceContagemAcessoApi serviceContagemAcessoApi;

	@ResponseBody
	@GetMapping(value = "**/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep) {

		// Retorna a consulta para meu CepDTO
		return new ResponseEntity<CepDTO>(pessoaUsuarioService.consultaCep(cep), HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarNomeCadastradoPf/{nome}")
	public ResponseEntity<List<PessoaFisica>> buscarNomeCadastradoPf(@PathVariable("nome") String nome) {

		// Buscar NOME
		List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.buscarNomeCadastrado(nome.trim().toUpperCase());

		// Atualizando a tabela de acesso ao End-Point
		serviceContagemAcessoApi.atualizaAcessoEndPointPF();

		// Retorno da consulta por NOME
		return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarCpfCadastradoList/{cpf}")
	public ResponseEntity<List<PessoaFisica>> buscarCpfCadastradoList(@PathVariable("cpf") String cpf) {

		// Buscar CPF
		List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.buscarCpfCadastradoList(cpf);

		// Retorno da consulta por CPF
		return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarNomeCadastradoPj/{nome}")
	public ResponseEntity<List<PessoaJuridica>> buscarNomeCadastradoPj(@PathVariable("nome") String nome) {

		// Buscar NOME
		List<PessoaJuridica> pessoaJuridicas = pessoaJuridicaRepository.buscarNomeCadastrado(nome.trim().toUpperCase());

		// Retorno da consulta por NOME
		return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarCnpjCadastradoList/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> buscarCnpjCadastradoList(@PathVariable("cnpj") String cnpj) {

		// Buscar CNPJ
		List<PessoaJuridica> pessoaJuridicas = pessoaJuridicaRepository.buscarCnpjCadastradoList(cnpj);

		// Retorno da consulta por CNPJ
		return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);

	}

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

		// Validando se o TipoPessoa da pessoaJuridica está null
		if (pessoaJuridica.getTipoPessoa() == null) {
			// Mostrando a mensagem que o TipoPessoa da pessoaJuridica não pode ser NULL
			throw new LojaVirtualMentoriaException("Informe o Tipo Jurídico ou Fornecedor da Loja");
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

		// Verificando se o ID é NULL ou se é menor ou igual a 0(zero)
		if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {

			// Varrendo lista de endereços
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				// Atribuir ao DTO a consulta de CEP de cada endereço
				CepDTO cepDTO = pessoaUsuarioService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

				// Setar os dados do endereço consutado pelo ViaCep
				pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

			}

		} else {

			// Varrendo lista de endereços
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				// Consultar por id a lista de endereço temporário e atribuir a uma variável
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

				// Verificando se enderecoTemp é diferente ao que está no banco de dados
				if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {

					// Atribuir ao DTO a consulta de CEP de cada endereço
					CepDTO cepDTO = pessoaUsuarioService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

					// Setar os dados do endereço consutado pelo ViaCep
					pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

				}

			}

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

		// Validando se o tipoPessoa da pessoaFisica está null
		if (pessoaFisica.getTipoPessoa() == null) {
			// Setar por padrão FISICA do ENUM
			pessoaFisica.setTipoPessoa(TipoPessoa.FÍSICA.name());
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

	@ResponseBody
	@GetMapping(value = "**/consultaCnpjReceitaWs/{cnpj}")
	public ResponseEntity<ConsultaCnpjDTO> consultaCnpjReceitaWs(@PathVariable("cnpj") String cnpj) {

		// Retorna consulta pelo CNPJ vindo da API do Receita Federal
		return new ResponseEntity<ConsultaCnpjDTO>(pessoaUsuarioService.consultaCnpjReceitaWS(cnpj), HttpStatus.OK);

	}

}
