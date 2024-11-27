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
import br.com.loja.virtual.mentoria.model.CategoriaProduto;
import br.com.loja.virtual.mentoria.model.dto.CategoriaProdutoDTO;
import br.com.loja.virtual.mentoria.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarCategoriaProduto")
	public ResponseEntity<CategoriaProdutoDTO> salvarCategoriaProduto(
			@RequestBody @Valid CategoriaProduto categoriaProduto) throws LojaVirtualMentoriaException {

		// Validando se a empresa está null ou sem ID
		if (categoriaProduto.getEmpresa() == null || categoriaProduto.getEmpresa().getId() == null) {
			// Mostrando a mensagem que a empresa deve ser informada
			throw new LojaVirtualMentoriaException("A empresa deve ser informada");
		}

		// Validando se categoria do produto no banco de dados
		if (categoriaProduto.getId() == null && categoriaProdutoRepository
				.buscarCategoriaProdutoByDescricaoBoolean(categoriaProduto.getDescricao())) {
			// Mostrando a mensagem que a categoria de produtro
			// não pode ser cadastrada com mesmo nome
			throw new LojaVirtualMentoriaException("Não pode ser cadastrada categoria de produto com o mesmo nome");
		}

		// Salvando no banco de dados
		CategoriaProduto categoriaProdutoSalvo = categoriaProdutoRepository.save(categoriaProduto);

		// Instância do CategoriaProdutoDTO
		CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();

		// Setando o dados do CategoriaProduto para o CategoriaProdutoDTO
		categoriaProdutoDTO.setId(categoriaProduto.getId());
		categoriaProdutoDTO.setDescricao(categoriaProduto.getDescricao());
		categoriaProdutoDTO.setEmpresa(categoriaProduto.getEmpresa().getId().toString());

		// Retorno do CategoriaProdutoDTO como objeto salvo
		return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarCategoriaProdutoByDescricao/{descricao}")
	public ResponseEntity<List<CategoriaProduto>> buscarCategoriaProdutoByDescricao(
			@PathVariable("descricao") String descricao) {

		// Buscar no banco de dados
		List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository
				.buscarCategoriaProdutoByDescricaoLista(descricao.toUpperCase().trim());

		// Retorno da busca no banco de dados
		return new ResponseEntity<List<CategoriaProduto>>(categoriaProdutos, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "**/deleteCategoriaProdutoByIdPost")
	public ResponseEntity<?> deleteCategoriaProdutoByIdPost(@RequestBody CategoriaProduto categoriaProduto) {

		// Verificando se a categoria do produto existe no banco de dados
		if (categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {

			// Mostre a mensagem se a categoria do produto não for encontrada
			return new ResponseEntity("Categoria do produto já foi removida", HttpStatus.OK);

		}

		// Removendo do banco de dados a categoria do produto por ID
		categoriaProdutoRepository.deleteById(categoriaProduto.getId());

		// Mostrando a mensagem de retorno que a
		// categoria do produto foi removida do banco de dados
		return new ResponseEntity("Categoria Removida", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteCategoriaProdutoByIdDelete/{id}")
	public ResponseEntity<CategoriaProduto> deleteCategoriaProdutoByIdPost(@PathVariable("id") Long id) {

		// Verificando se a categoria do produto existe no banco de dados
		if (categoriaProdutoRepository.findById(id).isPresent() == false) {

			// Mostre a mensagem se a categoria do produto não for encontrada
			return new ResponseEntity("Categoria do produto já foi removida", HttpStatus.OK);

		}

		// Removendo do banco de dados a categoria do produto por ID
		categoriaProdutoRepository.deleteById(id);

		// Mostrando a mensagem de retorno que a
		// categoria do produto foi removida do banco de dados
		return new ResponseEntity("Categoria Removida", HttpStatus.OK);

	}

}
