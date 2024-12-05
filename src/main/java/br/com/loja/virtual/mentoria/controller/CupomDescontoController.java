package br.com.loja.virtual.mentoria.controller;

import java.util.List;

import javax.validation.Valid;

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
import br.com.loja.virtual.mentoria.model.CupomDesconto;
import br.com.loja.virtual.mentoria.repository.CupomDescontoRepository;

@RestController
public class CupomDescontoController {

	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarCupomDesconto")
	public ResponseEntity<CupomDesconto> salvarCupomDesconto(@RequestBody @Valid CupomDesconto cupomDesconto)
			throws LojaVirtualMentoriaException {

		// Salva no banco de dados
		CupomDesconto cupomDescontoSalvo = cupomDescontoRepository.save(cupomDesconto);

		// Retorna o objeto salvo
		return new ResponseEntity<CupomDesconto>(cupomDescontoSalvo, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarCupomDescontoById/{id}")
	public ResponseEntity<CupomDesconto> buscarCupomDescontoById(@PathVariable("id") Long id)
			throws LojaVirtualMentoriaException {

		// Consulta no banco de dados
		CupomDesconto cupomDesconto = cupomDescontoRepository.findById(id).get();

		// Se o cupom de desconto é null
		if (cupomDesconto == null) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Não encontrou o cupom de desconto com o código: " + id);

		}

		// Buscar no banco de dados e retornar
		return new ResponseEntity<CupomDesconto>(cupomDesconto, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarCupomDescontoByIdEmpresa/{idEmpresa}")
	public ResponseEntity<List<CupomDesconto>> buscarCupomDescontoByIdEmpresa(
			@PathVariable("idEmpresa") Long idEmpresa) {

		// Buscar no banco de dados e retornar
		return new ResponseEntity<List<CupomDesconto>>(
				cupomDescontoRepository.buscarCupomDescontoByIdEmpresa(idEmpresa), HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarCupomDescontoLista")
	public ResponseEntity<List<CupomDesconto>> buscarCupomDescontoLista() {

		// Buscar no banco de dados e retornar
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.findAll(), HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "**/deleteCupomDesconto")
	public ResponseEntity<?> deleteCupomDesconto(@PathVariable("id") Long id) {

		// Deleta do banco de dados
		cupomDescontoRepository.deleteById(id);

		// Retorna o objeto salvo
		return new ResponseEntity<String>("Cupom de desconto deletado com sucesso", HttpStatus.OK);

	}

}
