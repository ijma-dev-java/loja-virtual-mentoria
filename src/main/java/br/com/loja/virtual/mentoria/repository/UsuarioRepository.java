package br.com.loja.virtual.mentoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// Buscar usuário pela login
	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);

}
