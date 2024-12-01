package br.com.loja.virtual.mentoria.controller;

import java.util.ArrayList;
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
import br.com.loja.virtual.mentoria.model.ImagemProduto;
import br.com.loja.virtual.mentoria.model.Produto;
import br.com.loja.virtual.mentoria.model.dto.ImagemProdutoDTO;
import br.com.loja.virtual.mentoria.repository.ImagemProdutoRepository;
import br.com.loja.virtual.mentoria.repository.ProdutoRepository;

@RestController
public class ImagemProdutoController {

	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarImagemProduto")
	public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody @Valid ImagemProduto imagemProduto) {

		// Salva no bando de dados
		imagemProduto = imagemProdutoRepository.saveAndFlush(imagemProduto);

		// Instância do ImagemProdutoDTO
		ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();

		// Seta as informações do imagemProduto para imagemProdutoDTO
		imagemProdutoDTO.setId(imagemProduto.getId());
		imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
		imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
		imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
		imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());

		// Retorna o objeto imagemProdutoDTO
		return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteImagemProdutoByIdProduto/{idProduto}")
	public ResponseEntity<?> deleteImagemProdutoByIdProduto(@PathVariable("idProduto") Long idProduto) {

		// Deleta do bando de dados
		imagemProdutoRepository.deleteImagemProdutoByIdProduto(idProduto);

		// Retorna a mensagem de imagens deletada
		return new ResponseEntity<String>("Imagems do produto removida", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteImagemProdutoByExistsById")
	public ResponseEntity<?> deleteImagemProdutoByExistsById(@RequestBody ImagemProduto imagemProduto) {

		// Verifica se o id do imagemProduto foi encontrado
		if (!imagemProdutoRepository.existsById(imagemProduto.getId())) {

			// Se não foi encontrado retorna uma mensagem
			return new ResponseEntity<String>(
					"Imagem já foi removida ou não existe com esse id: " + imagemProduto.getId(), HttpStatus.OK);

		}

		// Deleta do banco de dados
		imagemProdutoRepository.deleteById(imagemProduto.getId());

		// Retorna uma mensagem que a imagem do produto foi removida
		return new ResponseEntity<String>("Imagem removida", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteImagemProdutoPorId/{id}")
	public ResponseEntity<?> deleteImagemProdutoPorId(@PathVariable("id") Long id) {

		// Verifica se o id do imagemProduto foi encontrado
		if (!imagemProdutoRepository.existsById(id)) {

			// Se não foi encontrado retorna uma mensagem
			return new ResponseEntity<String>("Imagem já foi removida ou não existe com esse id: " + id, HttpStatus.OK);

		}

		// Deleta do banco de dados
		imagemProdutoRepository.deleteById(id);

		// Retorna uma mensagemque a imagem foi removida
		return new ResponseEntity<String>("Imagem removida", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscaImagemProdutoByIdProduto/{idProduto}")
	public ResponseEntity<List<ImagemProdutoDTO>> buscaImagemProdutoByIdProduto(
			@PathVariable("idProduto") Long idProduto) throws LojaVirtualMentoriaException {

		Produto produto = produtoRepository.findById(idProduto).orElse(null);

		// Se o produto estiver null
		if (produto == null) {

			// Mostra a mensagem que o produto não foi encontrato com o id consultado
			throw new LojaVirtualMentoriaException("Produto não encontrato com o ID: " + idProduto);

		}

		// Instância da Lista do ImagemProdutoDTO
		List<ImagemProdutoDTO> imagemProdutoDTOs = new ArrayList<ImagemProdutoDTO>();

		// Consulta no banco de dados
		List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProdutoByIdProduto(idProduto);

		// Varrendo a lista imagemProdutos
		for (ImagemProduto imagemProduto : imagemProdutos) {

			// Instância do ImagemProdutoDTO
			ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();

			// Seta as informações do imagemProduto para imagemProdutoDTO
			imagemProdutoDTO.setId(imagemProduto.getId());
			imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
			imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
			imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
			imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());

			// Adiciona o objeto imagemProdutoDTO a lista imagemProdutoDTOs
			imagemProdutoDTOs.add(imagemProdutoDTO);

		}

		// Retorna a lista imagemProdutoDTOs
		return new ResponseEntity<List<ImagemProdutoDTO>>(imagemProdutoDTOs, HttpStatus.OK);

	}

}
