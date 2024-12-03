package br.com.loja.virtual.mentoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	// Consultado pelo item excluido
	@Query(value = "select vclv from VendaCompraLojaVirtual vclv where vclv.id = ?1 and vclv.excluido = false")
	VendaCompraLojaVirtual findByIdExclusao(Long id);

}
