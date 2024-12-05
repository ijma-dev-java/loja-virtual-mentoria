package br.com.loja.virtual.mentoria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.model.StatusRastreio;
import br.com.loja.virtual.mentoria.repository.StatusRastreioRepository;

@RestController
public class StatusRastreioController {

	@Autowired
	StatusRastreioRepository statusRastreioRepository;

	@ResponseBody
	@GetMapping(value = "**/buscarStatusRastreioByIdVenda/{idVenda}")
	public ResponseEntity<List<StatusRastreio>> buscarStatusRastreioByIdVenda(@PathVariable("idVenda") Long idVenda) {

		// Consulta no banco de dados
		List<StatusRastreio> statusRastreios = statusRastreioRepository.buscarStatusRastreioByIdVenda(idVenda);

		// Retorna a consulta
		return new ResponseEntity<List<StatusRastreio>>(statusRastreios, HttpStatus.OK);

	}

}
