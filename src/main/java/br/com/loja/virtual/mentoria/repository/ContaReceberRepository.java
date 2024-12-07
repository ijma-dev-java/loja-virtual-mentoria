package br.com.loja.virtual.mentoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.ContaReceber;

@Repository
@Transactional
public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long> {

}
