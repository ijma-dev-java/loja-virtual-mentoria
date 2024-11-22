package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.Acesso;

@Repository
@Transactional
public interface AcessoRepository extends JpaRepository<Acesso, Long> {
	
	// Buscar acesso por descricao
	@Query(value = "select a from Acesso a where upper(trim(a.descricao)) like %?1%")
	List<Acesso> buscarAcessoByDescricao(String descricao);

}
