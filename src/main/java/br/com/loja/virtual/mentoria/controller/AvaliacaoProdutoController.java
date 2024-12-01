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
import br.com.loja.virtual.mentoria.model.AvaliacaoProduto;
import br.com.loja.virtual.mentoria.repository.AvaliacaoProdutoRepository;

@RestController
public class AvaliacaoProdutoController {

	@Autowired
	private AvaliacaoProdutoRepository avaliacaoProdutoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarAvaliacaoProduto")
	public ResponseEntity<AvaliacaoProduto> salvarAvaliacaoProduto(
			@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws LojaVirtualMentoriaException {

		// Verifica se a empresa está null
		// ou se id é diferente de vazio
		// e menor que 0(zero)
		if (avaliacaoProduto.getEmpresa() == null
				|| (avaliacaoProduto.getEmpresa() != null && avaliacaoProduto.getEmpresa().getId() <= 0)) {

			// Mostre mensgem para o cliente
			throw new LojaVirtualMentoriaException("Informa a empresa dona do registro");

		}

		// Verifica se o produto está null
		// ou se id é diferente de vazio
		// e menor que 0(zero)
		if (avaliacaoProduto.getProduto() == null
				|| (avaliacaoProduto.getProduto() != null && avaliacaoProduto.getProduto().getId() <= 0)) {

			// Mostre mensgem para o cliente
			throw new LojaVirtualMentoriaException("A avaliação de produto deve conter o produto associado");

		}

		// Verifica se a pessoa está null
		// ou se id é diferente de vazio
		// e menor que 0(zero)
		if (avaliacaoProduto.getPessoa() == null
				|| (avaliacaoProduto.getPessoa() != null && avaliacaoProduto.getPessoa().getId() <= 0)) {

			// Mostre mensgem para o cliente
			throw new LojaVirtualMentoriaException("A avaliação produto deve conter a pessoa ou cliente associado");

		}

		// Salva no banco de dados
		avaliacaoProduto = avaliacaoProdutoRepository.saveAndFlush(avaliacaoProduto);

		// Retorna o objeto salvo
		return new ResponseEntity<AvaliacaoProduto>(avaliacaoProduto, HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteAvaliacaoProdutoById/{id}")
	public ResponseEntity<?> deleteAvaliacaoProdutoById(@PathVariable("id") Long id)
			throws LojaVirtualMentoriaException {

		// Buscar do banco de dados por ID
		// Se não encontrar retorna null para evitar exceção
		AvaliacaoProduto avaliacaoProduto = avaliacaoProdutoRepository.findById(id).orElse(null);

		// Se o acesso estiver null
		if (avaliacaoProduto == null) {

			// Mostra a mensagem avaliação do produto não encontrato com o ID
			throw new LojaVirtualMentoriaException("Avalição de produto não encontrato com o ID: " + id);

		}

		// Deletando do banco de dados
		avaliacaoProdutoRepository.deleteById(id);

		// Retorna a mensagem que foi deletado
		return new ResponseEntity<String>("Avaliacao de produto removida", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarAvaliacaoProdutoByIdProduto/{idProduto}")
	public ResponseEntity<List<AvaliacaoProduto>> buscarAvaliacaoProdutoByIdProduto(
			@PathVariable("idProduto") Long idProduto) {

		// Consulta no banco de dados
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository
				.buscarAvaliacaoProdutoByIdProduto(idProduto);

		// Retorna a consulta do banco de dados
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarAvaliacaoProdutoByIdPessoa/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProduto>> buscarAvaliacaoProdutoByIdPessoa(
			@PathVariable("idPessoa") Long idPessoa) {

		// Consulta no banco de dados
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository
				.buscarAvaliacaoProditoByIdPessoa(idPessoa);

		// Retorna a consulta do banco de dados
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarAvaliacaoProdutoByIdProdutoByIdPessoa/{idProduto}/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProduto>> buscarAvaliacaoProdutoByIdProdutoByIdPessoa(
			@PathVariable("idProduto") Long idProduto, @PathVariable("idPessoa") Long idPessoa) {

		// Consulto no banco de dados
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository
				.buscarAvaliacaoProdutoByIdProdutoByIdPessoa(idProduto, idPessoa);

		// Retorna a consulta do banco de dados
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);

	}

}
