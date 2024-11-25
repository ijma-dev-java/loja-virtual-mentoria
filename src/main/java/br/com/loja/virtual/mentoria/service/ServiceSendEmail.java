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

	// Nome do usuário
	private String userName = "ijma.services.tech@gmail.com";

	// Senha do usuário
	private String senha = "htty mypi usne mjql";

	// Método genérico responsável pelo envio de e-mail com html
	@Async
	public void enviarEmailHtml(String assunto, String mensagem, String emailDestino)
			throws MessagingException, UnsupportedEncodingException {

		// Instância do objeto Properties do java.util
		Properties properties = new Properties();

		// Setando os atributos de configuração do SMTP
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls", "false");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// Instância do Session do java.mail
		// onde o session recebe a instância do properties
		// e cria uma nova autenticação
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				// Retorna a autenticação com o login e a senha
				return new PasswordAuthentication(userName, senha);

			}

		});

		// Debuga o session para mostrar os dados no console
		session.setDebug(true);

		// Array com e-mail de destino
		Address[] toUser = InternetAddress.parse(emailDestino);

		// Instância do objeto de mesagem do pacote java.mail
		// Onde recebe um MimeMessage do pactoe do java.mail.internet
		// E recebe nosso session
		Message message = new MimeMessage(session);

		// Responsável pelo envio do e-mail
		message.setFrom(new InternetAddress(userName, "Ítalo Araújo do Java Web", "UTF-8"));

		// Passando a mensagem para o usuário de destino
		message.setRecipients(Message.RecipientType.TO, toUser);

		// Passando o assunto do e-mail
		message.setSubject(assunto);

		// Passando a mensgem que será enviada para o e-mail de destino
		message.setContent(mensagem, "text/html; charset=utf-8");

		// Enviando o e-mail com o transport do pacote java.mail
		Transport.send(message);

	}

}
