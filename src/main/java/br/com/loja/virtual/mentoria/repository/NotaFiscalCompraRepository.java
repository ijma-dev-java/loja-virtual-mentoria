package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.NotaFiscalCompra;

@Repository
@Transactional
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {

	// Buscar nota fiscal de compra por descrição em observação - Lista
	@Query("select nfc from NotaFiscalCompra nfc where upper(trim(nfc.descricaoObs)) like %?1%")
	List<NotaFiscalCompra> buscaNotaFiscalCompraByDescricaoObsLista(String descricaoObs);

	// Buscar nota fiscal de compra por descrição em observação - Boolean
	@Query(nativeQuery = true, value = "select count(1) > 0 from nota_fiscal_compra where upper(trim(descricao_obs)) like ?1 ")
	boolean buscaNotaFiscalCompraByDescricaoObsBoolean(String descricaoObs);
	
	// Buscar nota fiscal de compra por pessoa - Lista
	@Query("select nfc from NotaFiscalCompra nfc where nfc.pessoa.id = ?1")
	List<NotaFiscalCompra> buscaNotaFiscalCompraByPessoa(Long idPessoa);
	
	// Buscar nota fiscal de compra por id de conta a pagar - Lista
	@Query("select ncf from NotaFiscalCompra ncf where ncf.contaPagar.id = ?1")
	List<NotaFiscalCompra> buscaNotaFiscalCompraByIdContaPagar(Long idContaPagar);
	
	// Buscar nota fiscal de compra por id de empresa - Lista
	@Query("select ncf  from NotaFiscalCompra ncf where ncf.empresa.id = ?1")
	List<NotaFiscalCompra> buscaNotaFiscalCompraByIdEmpresa(Long idEmpresa);
	
	// Deletar item do produto pelo id da nota fiscal de compra
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, value = "delete from nota_item_produto where nota_fiscal_compra_id = ?1")
	void deleteItemByIdNotaFiscalCompra(Long idNotaFiscalCompra);

}
