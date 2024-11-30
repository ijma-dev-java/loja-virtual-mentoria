package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.ContaPagar;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

	// Buscar contar a pagar por descrição - Lista
	@Query("select cp from ContaPagar cp where upper(trim(cp.descricao)) like %?1%")
	List<ContaPagar> buscarContaPagarByDescricao(String descricao);

	// Buscar contar a pagar por id da pessoa da fisica - Lista
	@Query("select cp from ContaPagar cp where cp.pessoaFisica.id = ?1")
	List<ContaPagar> buscarContaByIdPessoa(Long idPessoa);

	// Buscar contar a pagar por id da pessoa fornecedora juridica - Lista
	@Query("select cp from ContaPagar cp where cp.pessoaFornecedor.id = ?1")
	List<ContaPagar> buscarContaByIdPessoaFornecedor(Long idPessoaForncedor);

	// Buscar contar a pagar por id da empresa - Lista
	@Query("select cp from ContaPagar cp where cp.empresa.id = ?1")
	List<ContaPagar> buscarContaPorEmpresa(Long idEmpresa);

}
