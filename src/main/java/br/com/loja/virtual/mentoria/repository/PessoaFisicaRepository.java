package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.PessoaFisica;

@Repository
@Transactional
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

	// Buscar NOME cadastrado - Lista
	@Query(value = "select pf from PessoaFisica pf where upper(trim(pf.nome)) like %?1%")
	public List<PessoaFisica> buscarNomeCadastrado(String nome);

	// Buscar CPF cadastrado - Objeto
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public PessoaFisica buscarCpfCadastrado(String cpf);
	
	// Buscar CPF cadastrado - Lista
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public List<PessoaFisica> buscarCpfCadastradoList(String cpf);

}
