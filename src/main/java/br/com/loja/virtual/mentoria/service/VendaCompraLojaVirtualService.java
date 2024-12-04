package br.com.loja.virtual.mentoria.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.model.VendaCompraLojaVirtual;
import br.com.loja.virtual.mentoria.repository.VendaCompraLojaVirtualRepository;

@Service
public class VendaCompraLojaVirtualService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

	public void exclusaoTotalVendaBanco(Long idVenda) {

		// Criando SQL
		String sql =

				" begin;"
						+ " UPDATE nota_fiscal_venda set venda_compra_loja_virtual_id = null where venda_compra_loja_virtual_id = "
						+ idVenda + "; " + " delete from nota_fiscal_venda where venda_compra_loja_virtual_id = "
						+ idVenda + "; " + " delete from item_venda_loja where venda_compra_loja_virtual_id = "
						+ idVenda + "; " + " delete from status_rastreio where venda_compra_loja_virtual_id = "
						+ idVenda + "; " + " delete from venda_compra_loja_virtual where id = " + idVenda + "; "
						+ " commit; ";

		// Executando SQL
		jdbcTemplate.execute(sql);

	}

	public void exclusaoLogicaTotalVendaBanco(Long idVenda) {

		// Criando SQL
		String sql = "begin; update venda_compra_loja_virtual set excluido = true where id = " + idVenda + "; commit;";

		// Executando o SQL
		jdbcTemplate.execute(sql);
		;

	}

	public void ativaRegistroVendaBanco(Long idVenda) {

		// Criando SQL
		String sql = "begin; update venda_compra_loja_virtual set excluido = false where id = " + idVenda + "; commit;";

		// Executando o SQL
		jdbcTemplate.execute(sql);
		;

	}

	public List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualByFaixaData(String data1, String data2)
			throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date date1 = simpleDateFormat.parse(data1);
		Date date2 = simpleDateFormat.parse(data2);

		return vendaCompraLojaVirtualRepository.buscarVendaCompraLojaVirtualByFaixaData(date1, date2);

	}

}
