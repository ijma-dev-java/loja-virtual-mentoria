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

		// Setar os proprietáros do endereços
		for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {

			pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);
			pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);

		}

		// Salvar no banco de dados
		pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);

		// Buscar usuario pelo id ou pelo login
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

		// Verificando se o usuário esta nulo
		if (usuarioPj == null) {

			// Consulta a constranint no banco de dados
			String constraint = usuarioRepository.consultaConstraintAcesso();

			// Se encontrou a constraint
			if (constraint != null) {

				// Deleta do bando de dados com jdbcTemplate
				jdbcTemplate.execute("begin; alter table usuario_acesso drop constraint " + constraint + "; commit;");

			}

			// Instanciando o usuario
			usuarioPj = new Usuario();

			// Setando os atributos
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(pessoaJuridica);
			usuarioPj.setPessoa(pessoaJuridica);
			usuarioPj.setLogin(pessoaJuridica.getEmail());

			// Atribuir a data atual em milisegundos para a senha
			String senha = "" + Calendar.getInstance().getTimeInMillis();

			// Criptografar a senha
			String senhaCript = new BCryptPasswordEncoder().encode(senha);

			// Setar a senha criptografa para o atributo da senha
			usuarioPj.setSenha(senhaCript);

			// Salvando no banco de dados
			usuarioPj = usuarioRepository.save(usuarioPj);

			// Adicionar o acesso de usuario padrão (ROLE_USER)
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId());

			// Fazer o envio de e-mail do login e da senha

		}

		return pessoaJuridica;

	}

}
