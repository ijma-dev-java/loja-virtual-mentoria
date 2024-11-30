package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.MarcaProduto;

@Repository
@Transactional
public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {

	// Buscar MarcaProduto por descrição - Lista
	@Query("select mp from MarcaProduto mp where upper(trim(mp.descricao)) like %?1%")
	List<MarcaProduto> buscarMarcaProdutoByDescricao(String descricao);

}
