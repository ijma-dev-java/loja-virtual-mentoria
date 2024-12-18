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

	@Autowired
	private ServiceSendEmail serviceSendEmail;

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
			
			// Adicionar o acesso de usuario padrão (ROLE_ADMIN)
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");

			// Instanciando o StringBuilder para escrever o e-mail em HTML
			StringBuilder menssagemHtml = new StringBuilder();

			// Escreveendo o e-mail em HTML
			menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>" + "<br/><br/>");
			menssagemHtml.append("<b>Login: </b>" + pessoaJuridica.getEmail() + "<br/><br/>");
			menssagemHtml.append("<b>Senha: </b>" + senha + "<br/><br/>");
			menssagemHtml.append("<hr>");
			menssagemHtml
					.append("<b>Para maiores informações: </b>" + "Ítalo Araújo - (81) 9 9624-5416");

			try {

				// Enviando e-mail
				serviceSendEmail.enviarEmailHtml("Acesso gerado para acesso a loja virtual", menssagemHtml.toString(),
						pessoaJuridica.getEmail());

				// Se tiver exceção de Exception
			} catch (Exception e) {

				// Mostra mensgem no console
				e.printStackTrace();

			}

		}

		// Retorna o objeto salvo
		return pessoaJuridica;

	}

}
