package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.NotaFiscalVenda;

@Repository
@Transactional
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

	// Buscar nota fiscal pelo id da venda de compra pela loja vistural - Lista
	@Query(value = "select nfv from NotaFiscalVenda nfv where nfv.vendaCompraLojaVirtual.id = ?1")
	List<NotaFiscalVenda> buscaNotaFiscalByVendaCompraLojaVirtualLista(Long idVenda);

	// Buscar nota fiscal pelo id da venda de compra pela loja vistural - Objeto
	@Query(value = "select nfv from NotaFiscalVenda nfv where nfv.vendaCompraLojaVirtual.id = ?1")
	NotaFiscalVenda buscaNotaFiscalByVendaCompraLojaVirtualObjeto(Long idVenda);

}
