package br.com.loja.virtual.mentoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.PessoaJuridica;

@Repository
@Transactional
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {
	
	// Buscar CNPJ cadastrado
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica buscarCnpjCadastrado(String cnpj);

}
