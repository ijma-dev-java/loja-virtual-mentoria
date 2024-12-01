package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.NotaFiscalCompra;
import br.com.loja.virtual.mentoria.model.NotaItemProduto;

@Repository
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {

	// Buscar nota item produto pelo id do produto e pelo id da nota fiscal de
	// compra - Lista
	@Query("select nip from NotaItemProduto nip where nip.produto.id = ?1 and nip.notaFiscalCompra.id = ?2")
	List<NotaItemProduto> buscaNotaItemProdutoByIdProdutoByIdNotaFiscaCompra(Long idProduto, Long idNotaFiscal);

	// Buscar nota item produto pelo id do produto - Lista
	@Query("select nip from NotaItemProduto nip where nip.produto.id = ?1")
	List<NotaItemProduto> buscaNotaItemProdutoByIdProduto(Long idProduto);

	// Buscar nota item produto pelo id da nota fiscal de compra - Lista
	@Query("select nip from NotaItemProduto nip where nip.notaFiscalCompra.id = ?1")
	List<NotaItemProduto> buscaNotaItemProdutoByIdNotaFiscalCompra(Long idNotaFiscal);

	// Buscar nota item produto pelo id da empresa - Lista
	@Query("select nip from NotaItemProduto nip where nip.empresa.id = ?1")
	List<NotaFiscalCompra> buscaNotaItemPorEmpresa(Long idEmpresa);

	// Deletar item produto pelo id
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "delete from nota_item_produto where id = ?1")
	void deleteNotaItemProdutoById(Long id);

}
