package br.com.loja.virtual.mentoria.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.enums.StatusContaReceber;
import br.com.loja.virtual.mentoria.model.ContaReceber;
import br.com.loja.virtual.mentoria.model.Endereco;
import br.com.loja.virtual.mentoria.model.ItemVendaLoja;
import br.com.loja.virtual.mentoria.model.PessoaFisica;
import br.com.loja.virtual.mentoria.model.StatusRastreio;
import br.com.loja.virtual.mentoria.model.VendaCompraLojaVirtual;
import br.com.loja.virtual.mentoria.model.dto.ItemVendaLojaDTO;
import br.com.loja.virtual.mentoria.model.dto.VendaCompraLojaVirtualDTO;
import br.com.loja.virtual.mentoria.repository.ContaReceberRepository;
import br.com.loja.virtual.mentoria.repository.EnderecoRepository;
import br.com.loja.virtual.mentoria.repository.NotaFiscalVendaRepository;
import br.com.loja.virtual.mentoria.repository.StatusRastreioRepository;
import br.com.loja.virtual.mentoria.repository.VendaCompraLojaVirtualRepository;
import br.com.loja.virtual.mentoria.service.ServiceSendEmail;
import br.com.loja.virtual.mentoria.service.VendaCompraLojaVirtualService;

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

	@Autowired
	private StatusRastreioRepository statusRastreioRepository;

	@Autowired
	private VendaCompraLojaVirtualService vendaCompraLojaVirtualService;

	@Autowired
	private ContaReceberRepository contaReceberRepository;

	@Autowired
	private ServiceSendEmail serviceSendEmail;

	@ResponseBody
	@PostMapping(value = "**/salvarVendaCompraLojaVirtual")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaCompraLojaVirtual(
			@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual)
			throws LojaVirtualMentoriaException, UnsupportedEncodingException, MessagingException {

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

		// Varrendo a lista de itens de venda da loja
		for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {

			// Setando a empresa da loja virtual para a lista de itens
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());

			// // Setando a venda realizado pela loja virtual para a lista de itens
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		}

		// Salvando no banco de dados a venda com todos os dados
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);

		// Instânciando o StatusRastreio
		StatusRastreio statusRastreio = new StatusRastreio();

		// Setando os atributos
		statusRastreio.setCentroDistribuicao("Loja Local");
		statusRastreio.setCidade("Local");
		statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		statusRastreio.setEstado("Local");
		statusRastreio.setStatus("Inicio Compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		// Salva no banco de dados
		statusRastreioRepository.save(statusRastreio);

		// Associando a venda gravada no banco com a nota fiscal
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		// Salva a nota fiscal pra ficar amarrada na venda
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());

		// Instancia o VendaCompraLojaVirtualDTO
		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		// Setando os atributos
		vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
		vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

		// Varrendo a lista de itens de venda da loja e Adicionando o DTO
		for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItemVendaLojas()) {

			// Instancia o ItemVendaLojaDTO
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

			// Setando os atributos
			itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
			itemVendaLojaDTO.setQtd(itemVendaLoja.getQtd());

			// Adicionando o ItemVendaLojaDTO ao meu VendaCompraLojaVirtualDTO
			vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaLojaDTO);

		}

		// Instanciando o ContaReceber
		ContaReceber contaReceber = new ContaReceber();

		// Setando o atributos
		contaReceber.setDescricao("Código da venda: " + vendaCompraLojaVirtual.getId());
		contaReceber.setDataPagamento(Calendar.getInstance().getTime());
		contaReceber.setDataVencimento(Calendar.getInstance().getTime());
		contaReceber.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		contaReceber.setPessoa(vendaCompraLojaVirtual.getPessoa());
		contaReceber.setStatusContaReceber(StatusContaReceber.QUITADA);
		contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotal());

		// Salva no banco de dados
		contaReceberRepository.saveAndFlush(contaReceber);

		// Enviando E-mail para o comprador

		// Instanciando o StringBuilder
		StringBuilder msgemail = new StringBuilder();

		// Escrevendo a mensagem em HTML para envir por e-mail
		msgemail.append("Olá, ").append(pessoaFisica.getNome()).append("</br>");
		msgemail.append("Você realizou a compra de nº: ").append(vendaCompraLojaVirtual.getId()).append("</br>");
		msgemail.append("Na loja: ").append(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());

		// Enviando e-mail com assunto - msg e e-mail de destino do cliente
		serviceSendEmail.enviarEmailHtml("Compra realizada", msgemail.toString(), pessoaFisica.getEmail());

		// Enviando E-mail para o vendedor

		// Instanciando o StringBuilder
		msgemail = new StringBuilder();

		// Escrevendo a mensagem em HTML para envir por e-mail
		msgemail.append("Você realizou uma venda de nº: ").append(vendaCompraLojaVirtual.getId());

		// Enviando e-mail com assunto - msg e e-mail de destino do vendedor
		serviceSendEmail.enviarEmailHtml("Venda Realizada", msgemail.toString(),
				vendaCompraLojaVirtual.getEmpresa().getEmail());

		// Retornando o vendaCompraLojaVirtualDTO
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualById/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> buscarVendaCompraLojaVirtualById(@PathVariable("id") Long id)
			throws LojaVirtualMentoriaException {

		// Consulta no banco de dados
		// VendaCompraLojaVirtual vendaCompraLojaVirtual =
		// vendaCompraLojaVirtualRepository.findById(id).orElse(new
		// VendaCompraLojaVirtual());
		VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(id);

		// Verifica se o vendaCompraLojaVirtual está null
		if (vendaCompraLojaVirtual == null) {

			// Instancia um novo objeto
			// vendaCompraLojaVirtual = new VendaCompraLojaVirtual();

			// Mostrar mensagem para o cliente
			throw new LojaVirtualMentoriaException("Venda de compra pela loja virtual não encontrada");

		}

		// Instancia o VendaCompraLojaVirtualDTO
		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		// Setando o vendaCompraLojaVirtualDTO
		vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
		vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());

		// Varrendo a lista de itens de venda da loja e Adicionando o DTO
		for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItemVendaLojas()) {

			// Instancia o ItemVendaLojaDTO
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

			// Setando os atributos
			itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
			itemVendaLojaDTO.setQtd(itemVendaLoja.getQtd());

			// Adicionando o ItemVendaLojaDTO ao meu VendaCompraLojaVirtualDTO
			vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaLojaDTO);

		}

		// Retornando o vendaCompraLojaVirtualDTO
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/exclusaoTotalVendaBanco/{idVenda}")
	public ResponseEntity<String> exclusaoTotalVendaBanco(@PathVariable(value = "idVenda") Long idVenda) {

		// Deleta do banco de dados a venda completa
		vendaCompraLojaVirtualService.exclusaoTotalVendaBanco(idVenda);

		// Retorna a mensagem para o cliente
		return new ResponseEntity<String>("Venda excluida com sucesso.", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/exclusaoLogicaTotalVendaBanco/{idVenda}")
	public ResponseEntity<String> exclusaoLogicaTotalVendaBanco(@PathVariable(value = "idVenda") Long idVenda) {

		// Deleta do banco de dados a venda completa
		vendaCompraLojaVirtualService.exclusaoLogicaTotalVendaBanco(idVenda);

		// Retorna a mensagem para o cliente
		return new ResponseEntity<String>("Venda excluida com sucesso.", HttpStatus.OK);

	}

	@ResponseBody
	@PutMapping(value = "**/ativaRegistroVendaBanco/{idVenda}")
	public ResponseEntity<String> ativaRegistroVendaBanco(@PathVariable(value = "idVenda") Long idVenda) {

		// Deleta do banco de dados a venda completa
		vendaCompraLojaVirtualService.ativaRegistroVendaBanco(idVenda);

		// Retorna a mensagem para o cliente
		return new ResponseEntity<String>("Venda ativada com sucesso.", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualByproduto/{idProduto}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaCompraLojaVirtualByproduto(
			@PathVariable("idProduto") Long idProduto) throws LojaVirtualMentoriaException {

		// Consulta no banco de dados
		List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
				.buscarVendaCompraLojaVirtualByProduto(idProduto);

		// Verifica se o vendaCompraLojaVirtual está null
		if (vendaCompraLojaVirtual == null) {

			vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();

		}

		// Instanciando a lista de VendaCompraLojaVirtualDTO
		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOs = new ArrayList<VendaCompraLojaVirtualDTO>();

		// Varrendo o VendaCompraLojaVirtual
		for (VendaCompraLojaVirtual vclv : vendaCompraLojaVirtual) {

			// Instancia o VendaCompraLojaVirtualDTO
			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			// Setando o vendaCompraLojaVirtualDTO
			vendaCompraLojaVirtualDTO.setId(vclv.getId());
			vendaCompraLojaVirtualDTO.setValorTotal(vclv.getValorTotal());
			vendaCompraLojaVirtualDTO.setValorDesconto(vclv.getValorDesconto());
			vendaCompraLojaVirtualDTO.setPessoa(vclv.getPessoa());
			vendaCompraLojaVirtualDTO.setEnderecoCobranca(vclv.getEnderecoCobranca());
			vendaCompraLojaVirtualDTO.setEnderecoEntrega(vclv.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setValorFrete(vclv.getValorFrete());

			// Varrendo a lista de itens de venda da loja e Adicionando o DTO
			for (ItemVendaLoja itemVendaLoja : vclv.getItemVendaLojas()) {

				// Instancia o ItemVendaLojaDTO
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

				// Setando os atributos
				itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
				itemVendaLojaDTO.setQtd(itemVendaLoja.getQtd());

				// Adicionando o ItemVendaLojaDTO ao meu VendaCompraLojaVirtualDTO
				vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaLojaDTO);

			}

			// Adicionando a minha lista vendaCompraLojaVirtualDTOs
			// Ao vendaCompraLojaVirtualDTO de retorno
			vendaCompraLojaVirtualDTOs.add(vendaCompraLojaVirtualDTO);

		}

		// Retornando o vendaCompraLojaVirtualDTO
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOs, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualDinamica/{valorConsulta}/{tipoConsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaCompraLojaVirtualDinamica(
			@PathVariable("valorConsulta") String valorConsulta, @PathVariable("tipoConsulta") String tipoConsulta)
			throws LojaVirtualMentoriaException {

		// Instancia a VendaCompraLojaVirtual em uma lista
		List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = null;

		// Verifica consulta por id do produto
		if (tipoConsulta.equalsIgnoreCase("POR_ID_PRODUTO")) {

			// Buscar no banco de dados
			vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository
					.buscarVendaCompraLojaVirtualByProduto(Long.parseLong(valorConsulta));

			// Verifica consulta por nome do produto
		} else if (tipoConsulta.equalsIgnoreCase("POR_NOME_PRODUTO")) {

			// Buscar no banco de dados
			vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository
					.buscarVendaCompraLojaVirtualByNomeProduto(valorConsulta.toUpperCase().trim());

			// Verifica consulta por nome do cliente
		} else if (tipoConsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {

			// Buscar no banco de dados
			vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository
					.buscarVendaCompraLojaVirtualByNomeCliente(valorConsulta.toUpperCase().trim());

			// Verifica consulta por endereço de cobrança
		} else if (tipoConsulta.equalsIgnoreCase("POR_ENDERECO_COBRANCA")) {

			// Buscar no banco de dados
			vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository
					.buscarVendaCompraLojaVirtualByEnderecoCobranca(valorConsulta.toUpperCase().trim());

			// Verifica consulta por endereço de entrega
		} else if (tipoConsulta.equalsIgnoreCase("POR_ENDERECO_ENTREGA")) {

			// Buscar no banco de dados
			vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository
					.buscarVendaCompraLojaVirtualByEnderecoEntrega(valorConsulta.toUpperCase().trim());

		}

		// Verifica se o vendaCompraLojaVirtual está null
		if (vendaCompraLojaVirtuals == null) {

			// Instanciando uma nova lista pra evitar exeção
			vendaCompraLojaVirtuals = new ArrayList<VendaCompraLojaVirtual>();

		}

		// Instanciando a lista de VendaCompraLojaVirtualDTO
		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOs = new ArrayList<VendaCompraLojaVirtualDTO>();

		// Varrendo o VendaCompraLojaVirtual
		for (VendaCompraLojaVirtual vclv : vendaCompraLojaVirtuals) {

			// Instancia o VendaCompraLojaVirtualDTO
			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			// Setando o vendaCompraLojaVirtualDTO
			vendaCompraLojaVirtualDTO.setId(vclv.getId());
			vendaCompraLojaVirtualDTO.setValorTotal(vclv.getValorTotal());
			vendaCompraLojaVirtualDTO.setValorDesconto(vclv.getValorDesconto());
			vendaCompraLojaVirtualDTO.setPessoa(vclv.getPessoa());
			vendaCompraLojaVirtualDTO.setEnderecoCobranca(vclv.getEnderecoCobranca());
			vendaCompraLojaVirtualDTO.setEnderecoEntrega(vclv.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setValorFrete(vclv.getValorFrete());

			// Varrendo a lista de itens de venda da loja e Adicionando o DTO
			for (ItemVendaLoja itemVendaLoja : vclv.getItemVendaLojas()) {

				// Instancia o ItemVendaLojaDTO
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

				// Setando os atributos
				itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
				itemVendaLojaDTO.setQtd(itemVendaLoja.getQtd());

				// Adicionando o ItemVendaLojaDTO ao meu VendaCompraLojaVirtualDTO
				vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaLojaDTO);

			}

			// Adicionando a minha lista vendaCompraLojaVirtualDTOs
			// Ao vendaCompraLojaVirtualDTO de retorno
			vendaCompraLojaVirtualDTOs.add(vendaCompraLojaVirtualDTO);

		}

		// Retornando o vendaCompraLojaVirtualDTO
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOs, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualByFaixaData/{data1}/{data2}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaCompraLojaVirtualByFaixaData(
			@PathVariable("data1") String data1, @PathVariable("data2") String data2) throws ParseException {

		// Instancia a VendaCompraLojaVirtual em uma lista
		List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = null;

		// Consultando no banco de dados com o vendaCompraLojaVirtualService
		vendaCompraLojaVirtuals = vendaCompraLojaVirtualService.buscarVendaCompraLojaVirtualByFaixaData(data1, data2);

		// Verifica se o vendaCompraLojaVirtual está null
		if (vendaCompraLojaVirtuals == null) {

			// Instanciando uma nova lista pra evitar exeção
			vendaCompraLojaVirtuals = new ArrayList<VendaCompraLojaVirtual>();

		}

		// Instanciando a lista de VendaCompraLojaVirtualDTO
		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOs = new ArrayList<VendaCompraLojaVirtualDTO>();

		// Varrendo o VendaCompraLojaVirtual
		for (VendaCompraLojaVirtual vclv : vendaCompraLojaVirtuals) {

			// Instancia o VendaCompraLojaVirtualDTO
			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			// Setando o vendaCompraLojaVirtualDTO
			vendaCompraLojaVirtualDTO.setId(vclv.getId());
			vendaCompraLojaVirtualDTO.setValorTotal(vclv.getValorTotal());
			vendaCompraLojaVirtualDTO.setValorDesconto(vclv.getValorDesconto());
			vendaCompraLojaVirtualDTO.setPessoa(vclv.getPessoa());
			vendaCompraLojaVirtualDTO.setEnderecoCobranca(vclv.getEnderecoCobranca());
			vendaCompraLojaVirtualDTO.setEnderecoEntrega(vclv.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setValorFrete(vclv.getValorFrete());

			// Varrendo a lista de itens de venda da loja e Adicionando o DTO
			for (ItemVendaLoja itemVendaLoja : vclv.getItemVendaLojas()) {

				// Instancia o ItemVendaLojaDTO
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

				// Setando os atributos
				itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
				itemVendaLojaDTO.setQtd(itemVendaLoja.getQtd());

				// Adicionando o ItemVendaLojaDTO ao meu VendaCompraLojaVirtualDTO
				vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaLojaDTO);

			}

			// Adicionando a minha lista vendaCompraLojaVirtualDTOs
			// Ao vendaCompraLojaVirtualDTO de retorno
			vendaCompraLojaVirtualDTOs.add(vendaCompraLojaVirtualDTO);

		}

		// Retornando o vendaCompraLojaVirtualDTO
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOs, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualByIdCliente/{idCliente}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaCompraLojaVirtualByIdCliente(
			@PathVariable("idCliente") Long idCliente) {

		// Consultando no banco de dados com o vendaCompraLojaVirtualService
		List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository
				.buscarVendaCompraLojaVirtualByIdCliente(idCliente);

		// Verifica se o vendaCompraLojaVirtual está null
		if (vendaCompraLojaVirtuals == null) {

			// Instanciando uma nova lista pra evitar exeção
			vendaCompraLojaVirtuals = new ArrayList<VendaCompraLojaVirtual>();

		}

		// Instanciando a lista de VendaCompraLojaVirtualDTO
		List<VendaCompraLojaVirtualDTO> vendaCompraLojaVirtualDTOs = new ArrayList<VendaCompraLojaVirtualDTO>();

		// Varrendo o VendaCompraLojaVirtual
		for (VendaCompraLojaVirtual vclv : vendaCompraLojaVirtuals) {

			// Instancia o VendaCompraLojaVirtualDTO
			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			// Setando o vendaCompraLojaVirtualDTO
			vendaCompraLojaVirtualDTO.setId(vclv.getId());
			vendaCompraLojaVirtualDTO.setValorTotal(vclv.getValorTotal());
			vendaCompraLojaVirtualDTO.setValorDesconto(vclv.getValorDesconto());
			vendaCompraLojaVirtualDTO.setPessoa(vclv.getPessoa());
			vendaCompraLojaVirtualDTO.setEnderecoCobranca(vclv.getEnderecoCobranca());
			vendaCompraLojaVirtualDTO.setEnderecoEntrega(vclv.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setValorFrete(vclv.getValorFrete());

			// Varrendo a lista de itens de venda da loja e Adicionando o DTO
			for (ItemVendaLoja itemVendaLoja : vclv.getItemVendaLojas()) {

				// Instancia o ItemVendaLojaDTO
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();

				// Setando os atributos
				itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
				itemVendaLojaDTO.setQtd(itemVendaLoja.getQtd());

				// Adicionando o ItemVendaLojaDTO ao meu VendaCompraLojaVirtualDTO
				vendaCompraLojaVirtualDTO.getItemVendaLojas().add(itemVendaLojaDTO);

			}

			// Adicionando a minha lista vendaCompraLojaVirtualDTOs
			// Ao vendaCompraLojaVirtualDTO de retorno
			vendaCompraLojaVirtualDTOs.add(vendaCompraLojaVirtualDTO);

		}

		// Retornando o vendaCompraLojaVirtualDTO
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendaCompraLojaVirtualDTOs, HttpStatus.OK);

	}

}
