package br.com.loja.virtual.mentoria.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.Endereco;
import br.com.loja.virtual.mentoria.model.PessoaFisica;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.model.dto.CepDTO;
import br.com.loja.virtual.mentoria.repository.EnderecoRepository;
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

	@Autowired
	private EnderecoRepository enderecoRepository;

	@ResponseBody
	@GetMapping(value = "consultaPfNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPfNome(@PathVariable("nome") String nome) {

		// Buscar no banco de dados
		List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());

		// Retorno do objeto
		return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "consultaPfCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPfCpf(@PathVariable("cpf") String cpf) {

		// Buscar no banco de dados
		List<PessoaFisica> fisicas = pessoaFisicaRepository.existeCpfCadastradoList(cpf);

		// Retorno do objeto
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "consultaNomePJ/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaNomePJ(@PathVariable("nome") String nome) {

		// Buscar no banco de dados
		List<PessoaJuridica> fisicas = pessoaJuridicaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());

		// Retorno do objeto
		return new ResponseEntity<List<PessoaJuridica>>(fisicas, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "consultaCnpjPJ/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaCnpjPJ(@PathVariable("cnpj") String cnpj) {

		// Buscar no banco de dados
		List<PessoaJuridica> fisicas = pessoaJuridicaRepository.existeCnpjCadastradoList(cnpj.trim().toUpperCase());

		// Retorno do objeto
		return new ResponseEntity<List<PessoaJuridica>>(fisicas, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep) {

		// Retorna os dados consultados
		return new ResponseEntity<CepDTO>(pessoaUsuarioService.consultaCep(cep), HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica)
			throws LojaVirtualMentoriaException {

		// Utilizado em sistemas antigos
		// Verificando se o nome está null ou vazio
		/*
		 * if (pessoaJuridica.getNome() == null ||
		 * pessoaJuridica.getNome().trim().isEmpty()) {
		 * 
		 * // Mostra mensagem throw new
		 * LojaVirtualMentoriaException("Nome da empresa deve ser informado");
		 * 
		 * }
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

		// Verificando se a pessoa está nulo ou com id menor que zero
		if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {

			// Varrendo a lista de endereço
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				// Consultando endereço completo com o CEP
				CepDTO cepDTO = pessoaUsuarioService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

				// Setando os atributos
				pessoaJuridica.getEnderecos().get(p).setRua(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

			}

			// Caso contrário
		} else {

			// Varrendo a lista de endereço
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				// Buscar no banco de dados os endereços da pessoa
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

				// Verificando se o endereço do banco é diferente do que está vindo da tela
				if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {

					// Consultando endereço completo com o CEP
					CepDTO cepDTO = pessoaUsuarioService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

					// Setando os atributos
					pessoaJuridica.getEnderecos().get(p).setRua(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

				}

			}

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
