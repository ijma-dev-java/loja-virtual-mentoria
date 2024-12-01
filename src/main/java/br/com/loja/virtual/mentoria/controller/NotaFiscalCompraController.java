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
import br.com.loja.virtual.mentoria.model.NotaFiscalCompra;
import br.com.loja.virtual.mentoria.repository.NotaFiscalCompraRepository;

@RestController
public class NotaFiscalCompraController {

	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(
			@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws LojaVirtualMentoriaException {

		// Verifica se o id da nota fiscal de compra está null
		if (notaFiscalCompra.getId() == null) {

			// Verifica se a descrição em observação está diferente de null
			if (notaFiscalCompra.getDescricaoObs() != null) {

				// Consulta no banco de dados
				boolean existe = notaFiscalCompraRepository.buscaNotaFiscalCompraByDescricaoObsBoolean(
						notaFiscalCompra.getDescricaoObs().toUpperCase().trim());

				if (existe) {

					// Mostra mensagem para o cliente
					throw new LojaVirtualMentoriaException(
							"Já existe Nota de compra de compra com essa mesma descrição : "
									+ notaFiscalCompra.getDescricaoObs());

				}

			}

		}

		// Verifica se a pessoa esta null
		if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("A Pessoa Juridica da nota fiscal deve ser informada.");

		}

		// Verifica se a empresa esta null ou id é menor que 0(zero)
		if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("A empresa responsável deve ser infromada.");

		}

		// Verifica se a conta a pagar esta null ou id de conta a pagar da nota fiscal
		// de compra é menor que 0(zero)
		if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("A conta a pagar da nota deve ser informada.");

		}

		// Salva no banco de dados
		NotaFiscalCompra notaFiscalCompraSalva = notaFiscalCompraRepository.save(notaFiscalCompra);

		// Retorna o objeto salvo
		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalva, HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteItemByIdNotaFiscalCompra/{id}")
	public ResponseEntity<?> deleteItemByIdNotaFiscalCompra(@PathVariable("id") Long id) {

		// Deleta os filhos(itens)
		notaFiscalCompraRepository.deleteItemByIdNotaFiscalCompra(id);

		// Deleta o pai(nota fiscal de compra)
		notaFiscalCompraRepository.deleteById(id);

		// Retorna a mensagem para o cliente
		return new ResponseEntity("Nota Fiscal Compra Removida", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarNotaFiscalCompraById/{id}")
	public ResponseEntity<NotaFiscalCompra> buscarNotaFiscalCompraById(@PathVariable("id") Long id)
			throws LojaVirtualMentoriaException {

		// Consultar no banco de dados
		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

		// Verifica se o id está null
		if (notaFiscalCompra == null) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Não encontrou Nota Fiscal com código: " + id);

		}

		// Retorna a consulta
		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscaNotaFiscalCompraByDescricaoObsLista/{descricaoObs}")
	public ResponseEntity<List<NotaFiscalCompra>> buscaNotaFiscalCompraByDescricaoObsLista(
			@PathVariable("descricaoObs") String descricaoObs) {

		// Consulta no banco de dados
		List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository
				.buscaNotaFiscalCompraByDescricaoObsLista(descricaoObs.toUpperCase().trim());

		// Retorna a consulta
		return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompras, HttpStatus.OK);

	}

}
