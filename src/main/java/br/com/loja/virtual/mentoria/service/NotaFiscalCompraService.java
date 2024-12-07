package br.com.loja.virtual.mentoria.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.model.dto.NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO;
import br.com.loja.virtual.mentoria.model.dto.NotaFiscalCompraRelatorioProdutoDTO;
import br.com.loja.virtual.mentoria.model.dto.StatusVendaCompraLojaVirtualDTO;

@Service
public class NotaFiscalCompraService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Title: Histórico de compras de produtor para a loja
	// Este relatório permite saber os produtos comprados para serem vendidos
	// pela loja virtual, todos os produtos tem relação com a nota fiscal de
	// compra/venda
	// @param NotaFiscalCompraRelatorioProdutoDTO
	// @param dataInicio e dataFinal são parametros obrigatórios
	// @return List<NotaFiscalCompraRelatorioProdutoDTO>
	public List<NotaFiscalCompraRelatorioProdutoDTO> geradorRelatorioProdutoNotaFiscalCompra(
			NotaFiscalCompraRelatorioProdutoDTO notaFiscalCompraRelatorioProdutoDTO) {

		// Instanciando o NotaFiscalCompraRelatorioDTO
		List<NotaFiscalCompraRelatorioProdutoDTO> retorno = new ArrayList<NotaFiscalCompraRelatorioProdutoDTO>();

		// Escrevendo SQL
		String sql = "select p.id as codigoProduto, p.nome as nomeProduto, p.valor_venda as valorVendaProduto, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor, " + " nip.qtd as qtdCompra, "
				+ " nfc.id as codigoNota, nfc.data_compra as dataCompra " + " from nota_fiscal_compra as nfc "
				+ " inner join nota_item_produto as nip on nfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = nip.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = nfc.pessoa_id where ";

		// Continuação do SQL
		sql += " nfc.data_compra >= '" + notaFiscalCompraRelatorioProdutoDTO.getDataInicial() + "' and ";
		sql += " nfc.data_compra <= '" + notaFiscalCompraRelatorioProdutoDTO.getDataFinal() + "' ";

		// Verificando o código da nota
		if (!notaFiscalCompraRelatorioProdutoDTO.getCodigoNota().isEmpty()) {

			// Mostrando para o cliente
			sql += " and nfc.id = " + notaFiscalCompraRelatorioProdutoDTO.getCodigoNota() + " ";

		}

		// Verificando o código produto
		if (!notaFiscalCompraRelatorioProdutoDTO.getCodigoProduto().isEmpty()) {

			// Mostrando para o cliente
			sql += " and p.id = " + notaFiscalCompraRelatorioProdutoDTO.getCodigoProduto() + " ";

		}

		// Verificando o nome do produto
		if (!notaFiscalCompraRelatorioProdutoDTO.getNomeProduto().isEmpty()) {

			// Mostrando para o cliente
			sql += " upper(p.nome) like upper('%" + notaFiscalCompraRelatorioProdutoDTO.getNomeProduto() + "')";

		}

		// Verificando o nome do fornecedor
		if (!notaFiscalCompraRelatorioProdutoDTO.getNomeFornecedor().isEmpty()) {

			// Mostrando para o cliente
			sql += " upper(pj.nome) like upper('%" + notaFiscalCompraRelatorioProdutoDTO.getNomeFornecedor() + "')";

		}

		// Verificando a quantidade de itens do produto
		if (!notaFiscalCompraRelatorioProdutoDTO.getQtdCompra().isEmpty()) {

			// Mostrando para o cliente
			sql += " and nip.qtd = " + notaFiscalCompraRelatorioProdutoDTO.getQtdCompra() + " ";

		}

		// Aprontando no relatório
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NotaFiscalCompraRelatorioProdutoDTO.class));

		// Retorna o relatório em uma lista
		return retorno;

	}

	// Este relatório retorna os produtos que estão com estoque menor ou igual a
	// quantidade definida no campo de qtd_alerta_estoque.
	// @param alertaEstoque NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO
	// @return List<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO>
	// Lista de objetos NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO
	public List<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO> geradorRelatorioProdutoAlertaEstoqueNotaFiscalCompra(
			NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO) {

		// Instanciando o NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO
		List<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO> retorno = new ArrayList<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO>();

		// Escrevendo SQL
		String sql = "select pj.id as codigoFornecedor, pj.nome as nomeFornecedor, "
				+ " nfc.id as codigoNota, nfc.data_compra as dataCompra, "
				+ " p.id as codigoProduto, p.nome as nomeProduto, " + " p.valor_venda as valorVendaProduto, "
				+ " p.qtd_estoque as qtdEstoque, p.qtd_alerta_estoque as qtdAlertaEstoque, " + " nip.qtd as qtdCompra "
				+ " from nota_fiscal_compra as nfc "
				+ " inner join nota_item_produto as nip on nfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = nip.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = nfc.pessoa_id where";

		// Continuação do SQL
		sql += " nfc.data_compra >= '" + notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getDataInicial() + "' and ";
		sql += " nfc.data_compra <= '" + notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getDataFinal() + "' ";
		sql += " and p.alerta_qtd_estoque = true and p.qtd_estoque <= p.qtd_alerta_estoque ";

		// Verificando o codido da nota fiscal d compra do produto
		if (!notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getCodigoNota().isEmpty()) {

			// Mostrando para o cliente
			sql += " and nfc.id = " + notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getCodigoNota() + " ";

		}

		// Verificadndo o código do produto
		if (!notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getCodigoProduto().isEmpty()) {

			// Mostrando para o cliente
			sql += " and p.id = " + notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getCodigoProduto() + " ";

		}

		// Verificadndo o nome do produto
		if (!notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getNomeProduto().isEmpty()) {

			// Mostrando para o cliente
			sql += " upper(p.nome) like upper('%" + notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getNomeProduto()
					+ "')";

		}

		// Verificadndo o nome do fornecedor do produto
		if (!notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getNomeFornecedor().isEmpty()) {

			// Mostrando para o cliente
			sql += " upper(pj.nome) like upper('%"
					+ notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.getNomeFornecedor() + "')";

		}

		// Aprontando no relatório
		retorno = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO.class));

		// Retorna o relatório em uma lista
		return retorno;

	}

	public List<StatusVendaCompraLojaVirtualDTO> geradorRelatorioStatusVendaCompraLojaVirtual(
			StatusVendaCompraLojaVirtualDTO statusVendaCompraLojaVirtualDTO) {

		// Instanciando o StatusVendaCompraLojaVirtualDTO
		List<StatusVendaCompraLojaVirtualDTO> retorno = new ArrayList<StatusVendaCompraLojaVirtualDTO>();

		// Escrenvendo SQL
		String sql = "select " + " pf.id as codigoCliente, " + " pf.nome as nomeCliente, "
				+ " pf.email as emailCliente, " + " pf.telefone as telefoneCliente, " + " p.id as codigoProduto, "
				+ " p.nome as nomeProduto, " + " p.valor_venda as valorVendaProduto, "
				+ " p.qtd_estoque as qtdEstoque, " + " vclv.id as codigoVenda, "
				+ " vclv.status_venda_compra_loja_virtual as statusVendaCompraLojaVirtual "
				+ " from venda_compra_loja_virtual as vclv "
				+ " inner join item_venda_loja as ivl on ivl.venda_compra_loja_virtual_id = vclv.id "
				+ " inner join produto as p on p.id = ivl.produto_id "
				+ " inner join pessoa_fisica as pf on pf.id = vclv.pessoa_id where ";

		// Continuação do SQL
		sql += "vclv.data_venda >= '" + statusVendaCompraLojaVirtualDTO.getDataInicial() + "' and vclv.data_venda  <= '"
				+ statusVendaCompraLojaVirtualDTO.getDataFinal() + "' ";

		// Verificando o nome do produto
		if (!statusVendaCompraLojaVirtualDTO.getCodigoVenda().isEmpty()) {

			// Mostrando para o cliente
			sql += " and vclv.id = " + statusVendaCompraLojaVirtualDTO.getCodigoVenda();

		}

		// Verificando o nome do produto
		if (!statusVendaCompraLojaVirtualDTO.getNomeProduto().isEmpty()) {

			// Mostrando para o cliente
			sql += " and upper(p.nome) like upper('%" + statusVendaCompraLojaVirtualDTO.getNomeProduto() + "%') ";

		}

		// Verificando o status de venda de compra pela loja virtual
		if (!statusVendaCompraLojaVirtualDTO.getStatusVendaCompraLojaVirtual().isEmpty()) {

			// Mostrando para o cliente
			sql += " and vclv.status_venda_compra_loja_virtual in ('"
					+ statusVendaCompraLojaVirtualDTO.getStatusVendaCompraLojaVirtual() + "') ";

		}

		// Verificando o nome do cliente
		if (!statusVendaCompraLojaVirtualDTO.getNomeCliente().isEmpty()) {

			// Mostrando para o cliente
			sql += " and pf.nome like '%" + statusVendaCompraLojaVirtualDTO.getNomeCliente() + "%' ";

		}

		// Aprontando no relatório
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(StatusVendaCompraLojaVirtualDTO.class));

		// Retorna o relatório em uma lista
		return retorno;

	}

}
