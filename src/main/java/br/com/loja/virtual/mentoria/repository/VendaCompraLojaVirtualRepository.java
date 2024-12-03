package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	// Buscar pelo item excluido
	@Query(value = "select vclv from VendaCompraLojaVirtual vclv where vclv.id = ?1 and vclv.excluido = false")
	VendaCompraLojaVirtual findByIdExclusao(Long id);

	// Buscar venda de compra pela loja virtual por id do produto
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualByproduto(Long idProduto);

}
