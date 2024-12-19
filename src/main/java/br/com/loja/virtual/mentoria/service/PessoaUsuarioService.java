package br.com.loja.virtual.mentoria.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.loja.virtual.mentoria.model.PessoaFisica;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.model.Usuario;
import br.com.loja.virtual.mentoria.model.dto.CepDTO;
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
	private PessoaFisicaRepository pessoaFisicaRepository;

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
			usuarioRepository.insereAcessoUser(usuarioPj.getId());

			// Adicionar o acesso de usuario padrão (ROLE_ADMIN)
			usuarioRepository.insereAcessoAdmin(usuarioPj.getId(), "ROLE_ADMIN");

			// Instanciando o StringBuilder para escrever o e-mail em HTML
			StringBuilder menssagemHtml = new StringBuilder();

			// Escreveendo o e-mail em HTML
			menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>" + "<br/><br/>");
			menssagemHtml.append("<b>Login: </b>" + pessoaJuridica.getEmail() + "<br/><br/>");
			menssagemHtml.append("<b>Senha: </b>" + senha + "<br/><br/>");
			menssagemHtml.append("<hr>");
			menssagemHtml.append("<b>Para maiores informações: </b>" + "Ítalo Araújo - (81) 9 9624-5416");

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

	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {

		// Setar os proprietáros do endereços
		for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {

			pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
			// pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);

		}

		// Salvar no banco de dados
		pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

		// Buscar usuario pelo id ou pelo login
		Usuario usuarioPf = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

		// Verificando se o usuário esta nulo
		if (usuarioPf == null) {

			// Consulta a constranint no banco de dados
			String constraint = usuarioRepository.consultaConstraintAcesso();

			// Se encontrou a constraint
			if (constraint != null) {

				// Deleta do bando de dados com jdbcTemplate
				jdbcTemplate.execute("begin; alter table usuario_acesso drop constraint " + constraint + "; commit;");

			}

			// Instanciando o usuario
			usuarioPf = new Usuario();

			// Setando os atributos
			usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPf.setEmpresa(pessoaFisica.getEmpresa());
			usuarioPf.setPessoa(pessoaFisica);
			usuarioPf.setLogin(pessoaFisica.getEmail());

			// Atribuir a data atual em milisegundos para a senha
			String senha = "" + Calendar.getInstance().getTimeInMillis();

			// Criptografar a senha
			String senhaCript = new BCryptPasswordEncoder().encode(senha);

			// Setar a senha criptografa para o atributo da senha
			usuarioPf.setSenha(senhaCript);

			// Salvando no banco de dados
			usuarioPf = usuarioRepository.save(usuarioPf);

			// Adicionar o acesso de usuario padrão (ROLE_USER)
			usuarioRepository.insereAcessoUser(usuarioPf.getId());

			// Instanciando o StringBuilder para escrever o e-mail em HTML
			StringBuilder menssagemHtml = new StringBuilder();

			// Escreveendo o e-mail em HTML
			menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>" + "<br/><br/>");
			menssagemHtml.append("<b>Login: </b>" + pessoaFisica.getEmail() + "<br/><br/>");
			menssagemHtml.append("<b>Senha: </b>" + senha + "<br/><br/>");
			menssagemHtml.append("<hr>");
			menssagemHtml.append("<b>Para maiores informações: </b>" + "Ítalo Araújo - (81) 9 9624-5416");

			try {

				// Enviando e-mail
				serviceSendEmail.enviarEmailHtml("Acesso gerado para acesso a loja virtual", menssagemHtml.toString(),
						pessoaFisica.getEmail());

				// Se tiver exceção de Exception
			} catch (Exception e) {

				// Mostra mensgem no console
				e.printStackTrace();

			}

		}

		// Retorna o objeto salvo
		return pessoaFisica;

	}
	
	public CepDTO consultaCep(String cep) {
		
		// Retorna os dados consultados
		return new RestTemplate().getForEntity("https://viacep.com.br/ws/" + cep + "/json/", CepDTO.class).getBody();
		
	}

}
