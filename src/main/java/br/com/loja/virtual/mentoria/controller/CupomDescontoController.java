package br.com.loja.virtual.mentoria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.model.CupomDesconto;
import br.com.loja.virtual.mentoria.repository.CupomDescontoRepository;

@RestController
public class CupomDescontoController {

	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;

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
	public ResponseEntity<List<CupomDesconto>> listaCupomDesc() {

		// Buscar no banco de dados e retornar
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.findAll(), HttpStatus.OK);

	}

}
