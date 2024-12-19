package br.com.loja.virtual.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

	// Buscar a data atual do usuário e verificando se é menor que tantos dias
	@Query(value = "select u from Usuario u where u.dataAtualSenha <= current_date - 90")
	List<Usuario> usuarioSenhaVencida();

	// Buscar usuario pelo id ou pelo login
	@Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login =?2")
	Usuario findUserByPessoa(Long id, String email);

	// Buscando constraint
	@Query(value = "select constraint_name from information_schema.constraint_column_usage where table_name = 'usuarios_acesso' and column_name = 'acesso_id' and constraint_name <> 'unique_acesso_user';", nativeQuery = true)
	String consultaConstraintAcesso();

	// Inserindo acesso pelo id do usário
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuario_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where nome_desc = 'ROLE_USER'))")
	void insereAcessoUser(Long iduser);

	// Inserindo acesso pelo id do usário e pelo nome do acesso
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuario_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where nome_desc = ?2 limit 1))")
	void insereAcessoAdmin(Long iduser, String acesso);

}
