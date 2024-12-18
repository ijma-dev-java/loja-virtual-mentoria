package br.com.loja.virtual.mentoria.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.model.Usuario;
import br.com.loja.virtual.mentoria.repository.UsuarioRepository;

@Service
@Component
public class TarefaAutomatizadaService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ServiceSendEmail serviceSendEmail;

	// @Scheduled(initialDelay = 2000, fixedDelay = 86400000) Roda a cada 24 horas
	// Vai rodar todo dia as 11 horas da manhã horario de Sao paulo
	@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo")
	public void notificarUserTrocaSenha()
			throws UnsupportedEncodingException, MessagingException, InterruptedException {

		// Buscar no banco de dados
		List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();

		// Varrendo a lista de usuários
		for (Usuario usuario : usuarios) {

			// Instanciando o StringBuilder para construção do HTML
			StringBuilder msg = new StringBuilder();

			// Escrevendo o HTML
			msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/>");
			msg.append("Está na hora de trocar sua senha, já passou 90 dias de validade.").append("<br/>");
			msg.append("Troque sua senha de acesso a loja virtual do Ítalo Araújo do Java Web");

			// Enviado e-mail
			serviceSendEmail.enviarEmailHtml("Troca de senha", msg.toString(), usuario.getLogin());

			// Executando a cada 3 segundos
			Thread.sleep(3000);

		}

	}

}
