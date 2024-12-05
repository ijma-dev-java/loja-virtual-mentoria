package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.CupomDesconto;

@Repository
@Transactional
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {
	
	// Buscar cupom de desconto pelo id da empresa
	@Query(value = "select cd from CupomDesconto cd where cd.empresa.id = ?1")
	public List<CupomDesconto> buscarCupomDescontoByIdEmpresa(Long idEmpresa);

}
