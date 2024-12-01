package br.com.loja.virtual.mentoria.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.NotaItemProduto;
import br.com.loja.virtual.mentoria.repository.NotaItemProdutoRepository;

@RestController
public class NotaItemProdutoController {

	@Autowired
	private NotaItemProdutoRepository notaItemProdutoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarNotaItemProduto")
	public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto)
			throws LojaVirtualMentoriaException {

		// Verifica se o id esta null
		if (notaItemProduto.getId() == null) {

			// Verifica se o produto null ou se o id do produto está menor do que 0(zero)
			if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0) {

				// Mostra mensagem para o cliente
				throw new LojaVirtualMentoriaException("O produto deve ser informado");

			}

			// Verifica se a nota de compra está null ou se o id da nota de compra está
			// menor do 0(zero)
			if (notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0) {

				// Mostra mensagem para o cliente
				throw new LojaVirtualMentoriaException("A nota fiscal deve ser informada");

			}

			// Verifica se a empresa está null ou se o id da empresa está menor que 0(zero)
			if (notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0) {

				// Mostra mensagem para o cliente
				throw new LojaVirtualMentoriaException("A empresa deve ser informada");

			}

			// Consulta no banco de dados
			List<NotaItemProduto> notaItemProdutos = notaItemProdutoRepository
					.buscaNotaItemProdutoByIdProdutoByIdNotaFiscaCompra(notaItemProduto.getProduto().getId(),
							notaItemProduto.getNotaFiscalCompra().getId());

			// Verifica no banco de dados se tem registro
			if (!notaItemProdutos.isEmpty()) {

				// Mostra mensagem para o cliente
				throw new LojaVirtualMentoriaException("Já existe este produto cadastrado para esta nota");

			}

		}

		// Verifica se quantidade de produto está menor ou igual a 0(zero)
		if (notaItemProduto.getQtd() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("A quantidade do produto deve ser informada");
		}

		// Salva no banco de dados
		NotaItemProduto notaItemProdutoSalva = notaItemProdutoRepository.save(notaItemProduto);

		// Consulta no banco de dados
		//notaItemProdutoSalva = notaItemProdutoRepository.findById(notaItemProduto.getId()).get();

		// Retorna o objeto
		return new ResponseEntity<NotaItemProduto>(notaItemProdutoSalva, HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteNotaItemProdutoById/{id}")
	public ResponseEntity<?> deleteNotaItemProdutoById(@PathVariable("id") Long id) {

		// Delete do banco de dados
		notaItemProdutoRepository.deleteNotaItemProdutoById(id);

		// Retorna a mensagem que o objeto foi removido
		return new ResponseEntity<String>("Nota Item Produto Removido", HttpStatus.OK);

	}

}
