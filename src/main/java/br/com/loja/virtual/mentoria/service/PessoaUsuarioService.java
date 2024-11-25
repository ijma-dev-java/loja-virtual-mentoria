package br.com.loja.virtual.mentoria.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.model.Usuario;
import br.com.loja.virtual.mentoria.repository.PessoaJuridicaRepository;
import br.com.loja.virtual.mentoria.repository.UsuarioRepository;

@Service
public class PessoaUsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica) {

		// Varrendo a lista de endereços e associando
		for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {

			// Pega os endereços da pessoa cadastrada
			pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);

			// Pega os endereços da empresa cadastrada
			pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);

		}

		// Salva a pessoaJuridica no banco de dados
		pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);

		// Consultar usuário no banco de dados por id ou e-mail da pessoa
		Usuario usuarioPj = usuarioRepository.buscarUsuarioPorPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

		// Validando que usuário está null
		if (usuarioPj == null) {
			// Consultar constraint desnecessária
			String constraint = usuarioRepository.consultarConstraintAcesso();
			// Validando que a consultar da constraint desnecessária existe
			if (constraint != null) {
				// Remove a constraint desnecessária utilizando jdbcTemplate do spring
				jdbcTemplate.execute("begin; alter table usuario_acesso drop constraint " + constraint + "; commit");
			}

			// Instância do novo usuário
			usuarioPj = new Usuario();

			// Seta os atributos
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(pessoaJuridica);
			usuarioPj.setPessoa(pessoaJuridica);
			usuarioPj.setLogin(pessoaJuridica.getEmail());

			// Setando a data e a hora atual em milisegundos
			String senha = "123";

			// Criptografando a senha
			String senhaCryptografada = new BCryptPasswordEncoder().encode(senha);

			// Seta a senha criptografada
			usuarioPj.setSenha(senhaCryptografada);

			// Salva usuário no banco de dados
			usuarioPj = usuarioRepository.save(usuarioPj);

			// Inserindo acesso para usuário pessoaJuridica por ID
			usuarioRepository.insertAcessoUsuarioPjPorId(usuarioPj.getId());

			// Fazer o login de e-mail com o login e senha

		}

		return pessoaJuridica;

	}

}
