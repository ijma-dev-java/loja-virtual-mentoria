package br.com.loja.virtual.mentoria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendaCompraLojaVirtualService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void exclusaoTotalVendaBanco(Long idVenda) {
		
		// Criando SQL
		String sql =
				
                " begin;"
    			+ " UPDATE nota_fiscal_venda set venda_compra_loja_virtual_id = null where venda_compra_loja_virtual_id = "+idVenda+"; "
    			+ " delete from nota_fiscal_venda where venda_compra_loja_virtual_id = "+idVenda+"; "
    			+ " delete from item_venda_loja where venda_compra_loja_virtual_id = "+idVenda+"; "
    			+ " delete from status_rastreio where venda_compra_loja_virtual_id = "+idVenda+"; "
    			+ " delete from venda_compra_loja_virtual where id = "+idVenda+"; "
    			+ " commit; ";
		
		// Executando SQL
		jdbcTemplate.execute(sql);

	}
	
	public void exclusaoLogicaTotalVendaBanco(Long idVenda) {
		
		// Criando SQL
		String sql = "begin; update venda_compra_loja_virtual set excluido = true where id = " + idVenda +"; commit;";
		
		// Executando o SQL
		jdbcTemplate.execute(sql);;
		
	}
	
	public void ativaRegistroVendaBanco(Long idVenda) {
		
		// Criando SQL
		String sql = "begin; update venda_compra_loja_virtual set excluido = false where id = " + idVenda +"; commit;";
		
		// Executando o SQL
		jdbcTemplate.execute(sql);;
		
	}

}
