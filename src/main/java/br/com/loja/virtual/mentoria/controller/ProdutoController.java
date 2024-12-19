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
	@PostMapping(value = "salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto)
			throws LojaVirtualMentoriaException {

		// Verificando se empresa está nulo ou se o id da empresa é menor que 0
		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {

			// Mostra mensgem
			throw new LojaVirtualMentoriaException("Empresa responsável deve ser informada");

		}

		// Verifica se o id da empresa está nulo
		if (produto.getId() == null) {

			// Buscar no banco de dados produto pelo nome e id da empresa
			List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(),
					produto.getEmpresa().getId());

			// Verifica se encontrou produto
			if (!produtos.isEmpty()) {

				// Mostra mensagem
				throw new LojaVirtualMentoriaException("Já existe Produto com a descrição: " + produto.getNome());

			}

		}

		// Verifica se a categoria do produto está ou se o id é menor que 0
		if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("Categoria deve ser informada");

		}

		// Verifica se a marca de produto está nulo ou se o id esta menor que 0
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("Marca deve ser informada");

		}

		// Salva no banco de dados
		Produto produtoSalvo = produtoRepository.save(produto);

		// Retorna o objeto salbo
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "deleteProduto")
	public ResponseEntity<String> deleteProduto(@RequestBody Produto produto) {

		// Deletanda do banco de dados por id
		produtoRepository.deleteById(produto.getId());

		// Retorna mensagem
		return new ResponseEntity<String>("Produto removido com sucesso", HttpStatus.OK);

	}

	// @Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })
	@ResponseBody
	@DeleteMapping(value = "deleteProdutoPorId/{id}")
	public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id) {

		// Deleta do banco de dados
		produtoRepository.deleteById(id);

		// Retorna mensagem
		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "obterProduto/{id}")
	public ResponseEntity<Produto> obterAcesso(@PathVariable("id") Long id) throws LojaVirtualMentoriaException {

		// Deleta do banco de dados
		Produto produto = produtoRepository.findById(id).orElse(null);

		// Vereifica se o produto está nulo
		if (produto == null) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("Não encontrou produto com código: " + id);

		}

		// Retorna objeto
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "buscarProdNome/{desc}")
	public ResponseEntity<List<Produto>> buscarProdNome(@PathVariable("desc") String desc) {

		// Busar no banco de dados
		List<Produto> acesso = produtoRepository.buscarProdutoNome(desc.toUpperCase());

		// Retorna objeto
		return new ResponseEntity<List<Produto>>(acesso, HttpStatus.OK);

	}

}
