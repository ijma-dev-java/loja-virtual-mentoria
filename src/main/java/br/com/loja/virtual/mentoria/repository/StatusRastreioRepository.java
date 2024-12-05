package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.StatusRastreio;

@Repository
@Transactional
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {

	// Buscar StatusRastreio por id da venda
	@Query(value = "select sr from StatusRastreio sr where sr.vendaCompraLojaVirtual.id = ?1 order by sr.id")
	public List<StatusRastreio> buscarStatusRastreioByIdVenda(Long idVenda);

}
