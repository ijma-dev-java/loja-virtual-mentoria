package br.com.loja.virtual.mentoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.Endereco;

@Repository
@Transactional
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
