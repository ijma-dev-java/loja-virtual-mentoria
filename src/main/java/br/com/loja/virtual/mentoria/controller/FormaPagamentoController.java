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
import br.com.loja.virtual.mentoria.model.FormaPagamento;
import br.com.loja.virtual.mentoria.repository.FormaPagamentoRepository;

@RestController
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarFormaPagamento")
	public ResponseEntity<FormaPagamento> salvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento)
			throws LojaVirtualMentoriaException {

		// Salva no banco de dados
		formaPagamento = formaPagamentoRepository.save(formaPagamento);

		// Retorna o objeto salvo do banco de dados
		return new ResponseEntity<FormaPagamento>(formaPagamento, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarFormaPagamentoByEmpresaLista/{idEmpresa}")
	public ResponseEntity<List<FormaPagamento>> buscarFormaPagamentoByEmpresaLista(
			@PathVariable(value = "idEmpresa") Long idEmpresa) {

		// Consulta no banco de dados e retorna a lista
		return new ResponseEntity<List<FormaPagamento>>(
				formaPagamentoRepository.buscarFormaPagamentoByEmpresaLista(idEmpresa), HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarFormaPagamentoByIdLista")
	public ResponseEntity<List<FormaPagamento>> listaFormaPagamento() {

		// Consulta no banco de dados e retorna a lista
		return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(), HttpStatus.OK);

	}

}
