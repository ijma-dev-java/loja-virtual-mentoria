package br.com.loja.virtual.mentoria.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.model.PessoaFisica;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.model.Usuario;
import br.com.loja.virtual.mentoria.repository.PessoaFisicaRepository;
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
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	// Salvando PessoaJuridica
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

			// Inserindo acesso para usuário de ROLE_USER para pessoaJuridica por ID
			usuarioRepository.insertAcessoUsuarioUserPorId(usuarioPj.getId());

			// Inserindo acesso para usuário de ROLE_ADMIN para pessoaJuridica por ID
			usuarioRepository.insertAcessoUsuarioAdminPjPorId(usuarioPj.getId(), "ROLE_ADMIN");

			// Instância do StringBuilder para escrever o e-mail
			StringBuilder menssagemHtml = new StringBuilder();

			// Escrevendo a mensagem
			menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br /><br />");
			menssagemHtml.append("<b>Login: </b>" + pessoaJuridica.getEmail() + "<br /><br />");
			menssagemHtml.append("<b>Senha: </b>" + (senha) + "<br /><br />");
			menssagemHtml.append("<hr />");
			menssagemHtml.append("<br />");
			menssagemHtml.append("<b>Atenciosamente, Ítalo Araújo (81) 9 9624-5416</b>");

			try {

				// Chama o ServiceSendEmail passando:
				// o assunto, a mensagem e o e-mail de destino
				serviceSendEmail.enviarEmailHtml("Acesso gerado para loja virtual", menssagemHtml.toString(),
						pessoaJuridica.getEmail());

			} catch (Exception e) {

				// Mostra erro no console
				e.printStackTrace();

			}

		}

		return pessoaJuridica;

	}
	
	// Salvando PessoaFisica
	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {

		// Varrendo a lista de endereços e associando
		for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {

			// Pega os endereços da pessoa cadastrada
			pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);

		}

		// Salva a pessoaFisica no banco de dados
		pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

		// Consultar usuário no banco de dados por id ou e-mail da pessoa
		Usuario usuarioPf = usuarioRepository.buscarUsuarioPorPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

		// Validando que usuário está null
		if (usuarioPf == null) {
			// Consultar constraint desnecessária
			String constraint = usuarioRepository.consultarConstraintAcesso();
			// Validando que a consultar da constraint desnecessária existe
			if (constraint != null) {
				// Remove a constraint desnecessária utilizando jdbcTemplate do spring
				jdbcTemplate.execute("begin; alter table usuario_acesso drop constraint " + constraint + "; commit");
			}

			// Instância do novo usuário
			usuarioPf = new Usuario();

			// Seta os atributos
			usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPf.setEmpresa(pessoaFisica.getEmpresa());
			usuarioPf.setPessoa(pessoaFisica);
			usuarioPf.setLogin(pessoaFisica.getEmail());

			// Setando a data e a hora atual em milisegundos
			String senha = "123";

			// Criptografando a senha
			String senhaCryptografada = new BCryptPasswordEncoder().encode(senha);

			// Seta a senha criptografada
			usuarioPf.setSenha(senhaCryptografada);

			// Salva usuário no banco de dados
			usuarioPf = usuarioRepository.save(usuarioPf);

			// Inserindo acesso para usuário de ROLE_USER para pessoaJuridica por ID
			usuarioRepository.insertAcessoUsuarioUserPorId(usuarioPf.getId());

			// Instância do StringBuilder para escrever o e-mail
			StringBuilder menssagemHtml = new StringBuilder();

			// Escrevendo a mensagem
			menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br /><br />");
			menssagemHtml.append("<b>Login: </b>" + pessoaFisica.getEmail() + "<br /><br />");
			menssagemHtml.append("<b>Senha: </b>" + (senha) + "<br /><br />");
			menssagemHtml.append("<hr />");
			menssagemHtml.append("<br />");
			menssagemHtml.append("<b>Atenciosamente, Ítalo Araújo (81) 9 9624-5416</b>");

			try {

				// Chama o ServiceSendEmail passando:
				// o assunto, a mensagem e o e-mail de destino
				serviceSendEmail.enviarEmailHtml("Acesso gerado para loja virtual", menssagemHtml.toString(),
						pessoaFisica.getEmail());

			} catch (Exception e) {

				// Mostra erro no console
				e.printStackTrace();

			}

		}

		return pessoaFisica;

	}

}
