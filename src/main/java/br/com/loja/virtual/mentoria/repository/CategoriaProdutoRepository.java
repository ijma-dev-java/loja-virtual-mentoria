package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.CategoriaProduto;

@Repository
@Transactional
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

	// Buscar CategoriaProduto por descrição - boolean
	@Query(nativeQuery = true, value = "select count(1) > 0 from categoria_produto where upper(trim(descricao)) = upper(trim(?1))")
	public boolean buscarCategoriaProdutoByDescricaoBoolean(String descricao);

	// Buscar CategoriaProduto por descrição - Lista
	@Query("select cp from CategoriaProduto cp where upper(trim(cp.descricao)) like %?1%")
	public List<CategoriaProduto> buscarCategoriaProdutoByDescricaoLista(String descricao);

}
