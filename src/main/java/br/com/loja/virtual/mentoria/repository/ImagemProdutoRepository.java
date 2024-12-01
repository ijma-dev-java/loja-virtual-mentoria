package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.ImagemProduto;

@Repository
@Transactional
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

	// Buscar imagem do prodto por id do produto
	@Query("select ipr from ImagemProduto ipr where ipr.produto.id = ?1")
	List<ImagemProduto> buscaImagemProdutoByIdProduto(Long idProduto);

	// Deleta pelo id do produto
	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = "delete from imagem_produto where produto_id = ?1")
	void deleteImagemProdutoByIdProduto(Long idProduto);

}
