package br.com.loja.virtual.mentoria.controller;

import java.util.List;

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
import br.com.loja.virtual.mentoria.model.CategoriaProduto;
import br.com.loja.virtual.mentoria.model.dto.CategoriaProdutoDTO;
import br.com.loja.virtual.mentoria.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;

	@ResponseBody
	@GetMapping(value = "buscarPorDescricaoCategoriaProduto/{nomeDescricao}")
	public ResponseEntity<List<CategoriaProduto>> buscarPorDescricaoCategoriaProduto(
			@PathVariable("nomeDescricao") String nomeDescricao) {

		// Buscar no banco de dados
		List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository
				.buscarCategoriaDes(nomeDescricao.toUpperCase());

		// Retorno do objeto
		return new ResponseEntity<List<CategoriaProduto>>(categoriaProdutos, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "deleteCategoriaProduto")
	public ResponseEntity<?> deleteCategoriaProduto(@RequestBody CategoriaProduto categoriaProduto) {

		// Verificando se o id existe
		if (categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {

			// Mostra mensagem se não for encontrado
			return new ResponseEntity("Categoria já foi removida", HttpStatus.OK);

		}

		// Deleta do banco de dados
		categoriaProdutoRepository.deleteById(categoriaProduto.getId());

		// Mostra mensagem na tela
		return new ResponseEntity("Categoria Removida", HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "salvarCategoriaProduto")
	public ResponseEntity<CategoriaProdutoDTO> salvarCategoriaProduto(@RequestBody CategoriaProduto categoriaProduto)
			throws LojaVirtualMentoriaException {

		// Verificar se empresa está nulo ou se o id da empresa está nulo
		if (categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("A empresa deve ser informada");

		}

		// Verifica se o id da empresa ainda está nulo e se já existe no banco de dados
		if (categoriaProduto.getId() == null
				&& categoriaProdutoRepository.existeCatehoria(categoriaProduto.getNomeDesc())) {

			// Mostra mensagem
			throw new LojaVirtualMentoriaException("Não pode cadastar categoria com mesmo nome");

		}

		// Salva no banco de dados
		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);

		// Isntanciando o DTO
		CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();

		// Setando os atributos
		categoriaProdutoDTO.setId(categoriaSalva.getId());
		categoriaProdutoDTO.setNomeDesc(categoriaSalva.getNomeDesc());
		categoriaProdutoDTO.setEmpresa(categoriaSalva.getEmpresa().getId().toString());

		// Retorna o objeto salvo
		return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);

	}

}
