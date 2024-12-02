package br.com.loja.virtual.mentoria.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.Endereco;
import br.com.loja.virtual.mentoria.model.PessoaFisica;
import br.com.loja.virtual.mentoria.model.VendaCompraLojaVirtual;
import br.com.loja.virtual.mentoria.model.dto.VendaCompraLojaVirtualDTO;
import br.com.loja.virtual.mentoria.repository.EnderecoRepository;
import br.com.loja.virtual.mentoria.repository.NotaFiscalVendaRepository;
import br.com.loja.virtual.mentoria.repository.VendaCompraLojaVirtualRepository;

@RestController
public class VendaCompraLojaVirtualController {

	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaUsuarioController pessoaUsuarioController;

	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarVendaCompraLojaVirtual")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaCompraLojaVirtual(
			@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws LojaVirtualMentoriaException {

		// Setando a empresa
		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

		// Salvando no banco de dados a venda de pessoa física
		PessoaFisica pessoaFisica = pessoaUsuarioController.salvarPessoaFisica(vendaCompraLojaVirtual.getPessoa())
				.getBody();

		// Setando a pessoa física
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);

		// Setando a pessoa física no endereço de cobrança
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);

		// Setando a empresa no endereço de cobrança
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

		// Salvando o endereço de cobrança
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());

		// Setando o endereço de cobrança para o endereço de cobrança na venda
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

		// Setando a pessoa física no endereço de entrega
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);

		// Setando a empresa no endereço de entrega
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

		// Salvando o endereço de entrega
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());

		// Setando o endereço de cobrança para o endereço de entrega na venda
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

		// Setando e recuperando a empresa
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

		// Salvando no banco de dados a venda com todos os dados
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);

		// Associando a venda gravada no banco com a nota fiscal
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		// Salva a nota fiscal pra ficar amarrada na venda
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());

		// Instancia o VendaCompraLojaVirtualDTO
		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		// Setando o vendaCompraLojaVirtualDTO
		vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());

		// Retornando o vendaCompraLojaVirtualDTO
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);

	}

}
