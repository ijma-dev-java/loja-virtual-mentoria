package br.com.loja.virtual.mentoria.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.loja.virtual.mentoria.model.Usuario;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	// Configurando o gerenciador de autenticação
	public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

		// Obriga autenticar a URL
		super(new AntPathRequestMatcher(url));

		// Seta o gerenciador da autenticação
		setAuthenticationManager(authenticationManager);

	}

	// Retorna o usuário ao processar a autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		// Obtendo o usuário
		Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

		// Retorna o usuário com login e senha
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));

	}

	// Autenticação realizada com sucesso
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		try {

			// Utilizando nossa classe responsável por criar e retonar a autenticação JWT
			new JWTTokenAutenticationService().addAuthentication(response, authResult.getName());

		} catch (Exception e) {

			// Mostra mensagem no console caso ocorra algum erro
			e.printStackTrace();

		}

	}

	// Autenticação não realizada
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		// Se a autenticação falhar
		if (failed instanceof BadCredentialsException) {

			// Retorne a mensagem de usuário e senha não encontrado
			response.getWriter().write("Usuário e senha não encontrado");

		} else { // Se for de outra instância mostrar mensagem generica

			// Mensagem genérica
			response.getWriter().write("Falha ao logar: " + failed.getMessage());

		}

		// super.unsuccessfulAuthentication(request, response, failed);

	}

}
