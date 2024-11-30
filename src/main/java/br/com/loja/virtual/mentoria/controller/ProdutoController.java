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
import br.com.loja.virtual.mentoria.model.Produto;
import br.com.loja.virtual.mentoria.repository.ProdutoRepository;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto)
			throws LojaVirtualMentoriaException {

		// Validando se o ID é null ou menor que 0(zero)
		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Empresa responsável deve ser informada");

		}

		// Validando se o ID é null
		if (produto.getId() == null) {

			// Consultar no banco de dados se já existe produto com o mesmo nome e empresa
			// por id
			List<Produto> produtos = produtoRepository.buscarProdutoByNomeByIdEmpresaLista(
					produto.getNome().toUpperCase().trim(), produto.getEmpresa().getId());

			// Se encontrar no banco de dados acesso com o mesmo nome
			if (!produtos.isEmpty()) {

				// Mostra mensagem para o cliente
				throw new LojaVirtualMentoriaException("Já existe produto com o nome: " + produto.getNome());

			}

		}

		// Validando se o ID é null ou se ID é menor que 0(zero)
		if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Categoria do produto deve ser informada");

		}

		// Validando se o ID é null ou se ID é menor que 0(zero)
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Marca do produto deve ser informada");

		}

		// Salvo no banco de dados
		Produto produtoSalvo = produtoRepository.save(produto);

		// Retorna o objeto salvo
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "**/deleteProduto")
	public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) {

		// Deleta do banco de dados
		produtoRepository.deleteById(produto.getId());

		// Retorna a mensagem do objeto deletado
		return new ResponseEntity<String>("Produto removido", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteProdutoPorId/{id}")
	public ResponseEntity<?> deleteProdutoPorId(@PathVariable("id") Long id) {

		// Deleta do banco de dados por ID
		produtoRepository.deleteById(id);

		// Retorna a mensagem do objeto deletado
		return new ResponseEntity<String>("Produto removido", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarProdutoPorId/{id}")
	public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable("id") Long id) throws LojaVirtualMentoriaException {

		// Buscar do banco de dados por ID
		// Se não encontrar retorna null para evitar exceção
		Produto produto = produtoRepository.findById(id).orElse(null);

		// Se o produto estiver null
		if (produto == null) {

			// Mostra a mensagem customizada de produto não encontrato com o ID
			// consultado
			throw new LojaVirtualMentoriaException("Produto não encontrato com o ID: " + id);

		}

		// Retorna a consulta do objeto
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarProdutoByNomeLista/{nome}")
	public ResponseEntity<List<Produto>> buscarProdutoByNomeLista(@PathVariable("nome") String nome) {

		// Buscar do banco de dados por descricao
		List<Produto> produtos = produtoRepository.buscarProdutoByNomeLista(nome.toUpperCase().trim());

		// Retorna a consulta do objeto
		return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);

	}

}
