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
	
	// @Scheduled(initialDelay = 2000, fixedDelay = 86400000) // Roda a cada 24 horas
	@Scheduled(cron = "0 0 12 * * *", zone = "America/Sao_Paulo") // Vai rodar todo dia ao meio dia horario de Sao paulo
	public void notificarUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
		
		// Consultar usuário por dataAtualSenha menor que alguns dias
		List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();
		
		// Varrendo lista de usuários
		for (Usuario usuario : usuarios) {
			
			// Instância do objeto para escrever em HTML
			StringBuilder msg = new StringBuilder();
			
			// Escrevendo mensagem em HTML
			msg.append("Olá: " + usuario.getPessoa().getNome() + "<br /><br />");
			msg.append("Está na hora de trocar sua senha, já passou 90 dias de validade.").append("<br/>");
			msg.append("Troque sua senha da loja virtual do Ítalo Araújo - Java Web");
			
			// Enviando e-mail com a mensagem escrita
			serviceSendEmail.enviarEmailHtml("Troca de senha é necessária", msg.toString(), usuario.getLogin());
			
			// Executar a cada 3 segundos
			Thread.sleep(3000);
			
		}
		
	}

}
