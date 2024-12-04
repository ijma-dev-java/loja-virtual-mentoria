package br.com.loja.virtual.mentoria.repository;

import java.util.Date;
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
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualByProduto(Long idProduto);

	// Buscar venda de compra pela loja virtual por nome do produto
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.produto.nome)) like %?1%")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualByNomeProduto(String nomeProduto);

	// Buscar venda de compra pela loja virtual por nome do cliente
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.pessoa.nome)) like %?1%")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualByNomeCliente(String nomeClente);

	// Buscar venda de compra pela loja virtual por endereço de cobrança
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.enderecoCobranca.logradouro)) like %?1%")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualByEnderecoCobranca(String enderecoCobranca);

	// Buscar venda de compra pela loja virtual por endereço de entrega
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.logradouro)) like %?1%")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualByEnderecoEntrega(String enderecoEntrega);
	
	// Buscar venda de compra pela loja virtual por faixa de data
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and i.vendaCompraLojaVirtual.dataVenda >= ?1 and i.vendaCompraLojaVirtual.dataVenda <= ?2")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualByFaixaData(Date data1, Date data2);


}
