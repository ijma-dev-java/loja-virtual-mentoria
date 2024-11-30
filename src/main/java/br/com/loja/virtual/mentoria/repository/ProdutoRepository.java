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

	// Buscar por nome - boolean
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1))")
	public boolean buscarProdutoByNomeBoolean(String nome);

	// Buscar por nome e pelo id da empresa - boolean
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1)) and empresa_id = ?2")
	public boolean buscarProdutoByNomeByIdEmpresaBoolean(String nomeCategoria, Long idEmpresa);

	// Buscar por nome - lista
	@Query("select a from Produto a where upper(trim(a.nome)) like %?1%")
	public List<Produto> buscarProdutoByNomeLista(String nome);

	// Buscar por nome e pelo id da empresa - lista
	@Query("select a from Produto a where upper(trim(a.nome)) like %?1% and a.empresa.id = ?2")
	public List<Produto> buscarProdutoByNomeByIdEmpresaLista(String nome, Long idEmpresa);

}
