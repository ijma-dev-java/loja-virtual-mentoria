package br.com.loja.virtual.mentoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.virtual.mentoria.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// Consultar usuário por login
	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario buscarUsuarioPorLogin(String login);

	// Consultar usuário no banco de dados por id ou e-mail da pessoa
	@Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login = ?2")
	Usuario buscarUsuarioPorPessoa(Long id, String email);

	// Consultar constraint desnecessária
	@Query(nativeQuery = true, value = "select constraint_name from information_schema.constraint_column_usage \r\n"
			+ "where table_name = 'usuario_acesso' and column_name = 'acesso_id'\r\n"
			+ "and constraint_name <> 'usuario_acesso_unique';")
	String consultarConstraintAcesso();

	// Inserindo acesso para usuário pessoaJuridica por ID
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuario_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where descricao = 'ROLE_USER'))")
	void insertAcessoUsuarioPjPorId(Long idUser);

}
