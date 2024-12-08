package br.com.loja.virtual.mentoria.controller;

import java.io.Serializable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PagamentoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@GetMapping(value = "**/pagamento/{idVendaCompra}")
	public ModelAndView pagamento(@PathVariable(value = "idVendaCompra", required = false) String idVendaCompra) {

		return new ModelAndView("pagamento");

	}

}
