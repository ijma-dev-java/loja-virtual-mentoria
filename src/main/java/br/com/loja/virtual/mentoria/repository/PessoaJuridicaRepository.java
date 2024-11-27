package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.PessoaJuridica;

@Repository
@Transactional
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {

	// Buscar por NOME - Lista
	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%")
	public List<PessoaJuridica> buscarNomeCadastrado(String nome);

	// Buscar por CNPJ - Objeto
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica buscarCnpjCadastrado(String cnpj);

	// Buscar por CNPJ - Lista
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public List<PessoaJuridica> buscarCnpjCadastradoList(String cnpj);

	// Buscar por IE - Objeto
	@Query(value = "select pj from PessoaJuridica pj where pj.inscricaoEstadual = ?1")
	public PessoaJuridica buscarInscricaoEstadualCadastrado(String inscricaoEstadual);

	// Buscar por IE - Lista
	@Query(value = "select pj from PessoaJuridica pj where pj.inscricaoEstadual = ?1")
	public List<PessoaJuridica> buscarInscricaoEstadualCadastradoList(String inscricaoEstadual);

}
