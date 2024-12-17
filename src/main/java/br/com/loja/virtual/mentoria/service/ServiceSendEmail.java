package br.com.loja.virtual.mentoria.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ServiceSendEmail {
	
	// E-mail do resposnável
	private String userName = "ijma.services.tech@gmail.com";
	
	// Senha do e-mail do responsável
	private String senha = "kedk ghbm xpwz xfkk";
	
	// Serviço responsável pelo envio de e-mail
	@Async
	public void enviarEmailHtml(String assunto, String menssagem, String emailDestino)
			throws UnsupportedEncodingException, MessagingException {
		
		// Configurações de SMTP do servidor de e-mail
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls", "false");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		// Sessão de autenticação
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(userName, senha);
				
			}

		});
		
		// Debugando o session
		session.setDebug(true);
		
		// E-mail de destino
		Address[] toUser = InternetAddress.parse(emailDestino);
		
		// Instanciando o message
		Message message = new MimeMessage(session);
		
		// Setando os atributos
		message.setFrom(new InternetAddress(userName, "Ítalo Araújo do Java Web", "UTF-8"));
		message.setRecipients(Message.RecipientType.TO, toUser);
		message.setSubject(assunto);
		// Enviando apenas texto
		// message.setText(menssagem);
		
		// Enviando em HTML com codificação UTF-8
		message.setContent(menssagem, "text/html; charset=utf-8");
		
		// Transportando a mensagem
		Transport.send(message);

	}

}
