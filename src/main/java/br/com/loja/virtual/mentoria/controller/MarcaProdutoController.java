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
import br.com.loja.virtual.mentoria.model.MarcaProduto;
import br.com.loja.virtual.mentoria.repository.MarcaProdutoRepository;

@RestController
public class MarcaProdutoController {

	@Autowired
	private MarcaProdutoRepository marcaProdutoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarMarcaProduto")
	public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody @Valid MarcaProduto marcaProduto)
			throws LojaVirtualMentoriaException {

		// Verifica se o ID está null
		if (marcaProduto.getId() == null) {

			// Consultar no banco de dados
			List<MarcaProduto> marcaProdutos = marcaProdutoRepository
					.buscarMarcaProdutoByDescricao(marcaProduto.getDescricao().toUpperCase().trim());

			// Verifica se tem registro
			if (!marcaProdutos.isEmpty()) {

				// Mostra a mensagem que para o cliente
				throw new LojaVirtualMentoriaException(
						"Já existe marca de produyto com a descrição: " + marcaProduto.getDescricao());
			}

		}

		// Salvo no banco de dados
		MarcaProduto marcaProdutoSalvo = marcaProdutoRepository.save(marcaProduto);

		// Retorna o objeto salvo
		return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "**/deleteMarcaProduto")
	public ResponseEntity<String> deleteMarcaProduto(@RequestBody MarcaProduto marcaProduto) {

		// Deletar do banco de dados
		marcaProdutoRepository.deleteById(marcaProduto.getId());

		// Retorna a mensagem que o objeto foi removido
		return new ResponseEntity<String>("Marca de produto foi removida", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteMarcaPorId/{id}")
	public ResponseEntity<String> deleteMarcaPorId(@PathVariable("id") Long id) {

		// Deletar do banco de dados
		marcaProdutoRepository.deleteById(id);

		// Retorna a mensagem que o objeto foi removido
		return new ResponseEntity<String>("Marca de produto foi removida", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarMarcaProdutoPorId/{id}")
	public ResponseEntity<MarcaProduto> obterMarca(@PathVariable("id") Long id) throws LojaVirtualMentoriaException {

		// Consultar no banco de dados
		MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

		// Verifica se o ID está null
		if (marcaProduto == null) {

			// Mostra a mensagem para o cliente
			throw new LojaVirtualMentoriaException("Não encontrou a marca de produto com código: " + id);

		}

		// Retorna o objeto encontrado ou a mensagem
		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarMarcaProdutoPorDescricao/{descricao}")
	public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoPorDescricao(
			@PathVariable("descricao") String descricao) {

		// Consulta no banco de dados
		List<MarcaProduto> marcaProdutos = marcaProdutoRepository
				.buscarMarcaProdutoByDescricao(descricao.toUpperCase().trim());

		// Retorna o objeto consutado no banco de dados
		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);

	}

}
