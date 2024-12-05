package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.FormaPagamento;

@Repository
@Transactional
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

	// Buscar formas de pagamento por id da empresa - Lista
	@Query(value = "select fp from FormaPagamento fp where fp.empresa.id = ?1")
	List<FormaPagamento> buscarFormaPagamentoByEmpresaLista(Long idEmpresa);

}
