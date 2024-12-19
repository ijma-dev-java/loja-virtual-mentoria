package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.Produto;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	// Buscar por produto pelo nome da categoria - Boolean
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1))")
	public boolean existeProduto(String nomeCategoria);
	
	// Buscar por produto pelo nome da categoria e id da empresa - Boolean
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1)) and empresa_id = ?2")
	public boolean existeProduto(String nomeCategoria, Long idEmpresa);
	
	// Buscar por produto pelo nome do prodto - Lista
	@Query("select p from Produto p where upper(trim(p.nome)) like %?1%")
	public List<Produto> buscarProdutoNome(String nome);
	
	// Buscar por produto pelo nome do produto e id da empresa - Lista
	@Query("select p from Produto p where upper(trim(p.nome)) like %?1% and p.empresa.id = ?2")
	public List<Produto> buscarProdutoNome(String nome, Long idEmpresa);

}
