package br.com.loja.virtual.mentoria.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.model.dto.NotaFiscalCompraRelatorioDTO;

@Service
public class NotaFiscalCompraService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<NotaFiscalCompraRelatorioDTO> geradorRelatorioNotaFiscalCompra(
			NotaFiscalCompraRelatorioDTO notaFiscalCompraRelatorioDTO) {

		// Instanciando o NotaFiscalCompraRelatorioDTO
		List<NotaFiscalCompraRelatorioDTO> retorno = new ArrayList<NotaFiscalCompraRelatorioDTO>();

		// Escrevendo SQL
		String sql = "select p.id as codigoProduto, p.nome as nomeProduto, p.valor_venda as valorVendaProduto, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor, " + " nip.qtd as qtdCompra, "
				+ " nfc.id as codigoNota, nfc.data_compra as dataCompra " + " from nota_fiscal_compra as nfc "
				+ " inner join nota_item_produto as nip on nfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = nip.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = nfc.pessoa_id where ";
		
		// Continuação do SQL
		sql += " nfc.data_compra >= '" + notaFiscalCompraRelatorioDTO.getDataInicial() + "' and ";
		sql += " nfc.data_compra <= '" + notaFiscalCompraRelatorioDTO.getDataFinal() + "' ";
		
		// Verificando o código da nota
		if (!notaFiscalCompraRelatorioDTO.getCodigoNota().isEmpty()) {
		
			// Mostrando para o cliente
			sql += " and nfc.id = " + notaFiscalCompraRelatorioDTO.getCodigoNota() + " ";
			
		}
		
		// Verificando o código produto
		if (!notaFiscalCompraRelatorioDTO.getCodigoProduto().isEmpty()) {
			
			// Mostrando para o cliente
			sql += " and p.id = " + notaFiscalCompraRelatorioDTO.getCodigoProduto() + " ";
			
		}

		// Verificando o nome do produto
		if (!notaFiscalCompraRelatorioDTO.getNomeProduto().isEmpty()) {
			
			// Mostrando para o cliente
			sql += " upper(p.nome) like upper('%" + notaFiscalCompraRelatorioDTO.getNomeProduto() + "')";
			
		}
		
		// Verificando o nome do fornecedor
		if (!notaFiscalCompraRelatorioDTO.getNomeFornecedor().isEmpty()) {
			
			// Mostrando para o cliente
			sql += " upper(pj.nome) like upper('%" + notaFiscalCompraRelatorioDTO.getNomeFornecedor() + "')";
			
		}
		
		// Verificando a quantidade de itens do produto
		if (!notaFiscalCompraRelatorioDTO.getQtdCompra().isEmpty()) {
			
			// Mostrando para o cliente
			sql += " and nip.qtd = " + notaFiscalCompraRelatorioDTO.getQtdCompra() + " ";
			
		}

		// Aprontando no relatório
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NotaFiscalCompraRelatorioDTO.class));

		// Retorna o relatório em uma lista
		return retorno;

	}

}
