package br.com.loja.virtual.mentoria.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.ContaPagar;
import br.com.loja.virtual.mentoria.repository.ContaPagarRepository;

@RestController
public class ContaPagarController {

	@Autowired
	private ContaPagarRepository contaPagarRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarContaPagar")
	public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar)
			throws LojaVirtualMentoriaException {

		// Verifica por id da empresa
		if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("A Empresa responsável pela conta deve ser informada");

		}

		// Verifica por id da pessoa fisica
		if (contaPagar.getPessoaFisica() == null || contaPagar.getPessoaFisica().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("A Pessoa responsável pela conta deve ser informada");

		}

		// Verifica por id da pessoa fisica
		if (contaPagar.getPessoaFornecedor() == null || contaPagar.getPessoaFornecedor().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("O Fornecedor responsável pela conta deve ser informado");

		}

		// Verifica se o id esta null
		if (contaPagar.getId() == null) {

			// Consulta no banco de dados por descrição
			List<ContaPagar> contaPagars = contaPagarRepository
					.buscarContaPagarByDescricao(contaPagar.getDescricao().toUpperCase().trim());

			// Verifica se existe registro com a mesma descrição
			if (!contaPagars.isEmpty()) {

				// Mostra mensgem para o cliente
				throw new LojaVirtualMentoriaException(
						"Já existe conta a pagar com a descrição: " + contaPagar.getDescricao());

			}

		}

		// Salva no banco de dados
		ContaPagar acessoSalvo = contaPagarRepository.save(contaPagar);

		// Retorna o objeto salvo
		return new ResponseEntity<ContaPagar>(acessoSalvo, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "**/deleteContaPagar")
	public ResponseEntity<String> deleteContaPagar(@RequestBody ContaPagar contaPagar) {

		// Deleta do banco de dados
		contaPagarRepository.deleteById(contaPagar.getId());

		// Retorna a mensagem para cliente
		return new ResponseEntity<String>("Conta Pagar Removida", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteContaPagarPorId/{id}")
	public ResponseEntity<String> deleteContaPagarPorId(@PathVariable("id") Long id) {

		// Deleta do banco de dados
		contaPagarRepository.deleteById(id);

		// Retorna a mensagem para cliente
		return new ResponseEntity<String>("Conta Pagar Removida", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarContaPagar/{id}")
	public ResponseEntity<ContaPagar> buscarContaPagar(@PathVariable("id") Long id)
			throws LojaVirtualMentoriaException {

		// Consutando no banco de dados
		ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

		// Verifica se o id está null
		if (contaPagar == null) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Não encontrou Conta Pagar com código: " + id);

		}

		return new ResponseEntity<ContaPagar>(contaPagar, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarPorContaPorDescricao/{descricao}")
	public ResponseEntity<List<ContaPagar>> buscarPorContaPorDescricao(@PathVariable("descricao") String descricao) {

		// Consuta no banco de dados
		List<ContaPagar> contaPagars = contaPagarRepository.buscarContaPagarByDescricao(descricao.toUpperCase().trim());

		// Retorna o objeto consultado
		return new ResponseEntity<List<ContaPagar>>(contaPagars, HttpStatus.OK);

	}

}
