package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.AvaliacaoProduto;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

	// Buscar avaliação do produto pelo id do produto - Lista
	@Query(value = "select ap from AvaliacaoProduto ap where ap.produto.id = ?1")
	List<AvaliacaoProduto> buscarAvaliacaoProdutoByIdProduto(Long idProduto);

	// Buscar aviliação do produto pelo id do produto e do id da pessoa - Lista
	@Query(value = "select ap from AvaliacaoProduto ap where ap.produto.id = ?1 and ap.pessoa.id = ?2")
	List<AvaliacaoProduto> buscarAvaliacaoProdutoByIdProdutoByIdPessoa(Long idProduto, Long idPessoa);

	// Buscar aviliação do produto pelo id da pessoa - Lista
	@Query(value = "select ap from AvaliacaoProduto ap where ap.pessoa.id = ?1")
	List<AvaliacaoProduto> buscarAvaliacaoProditoByIdPessoa(Long idPessoa);

}
