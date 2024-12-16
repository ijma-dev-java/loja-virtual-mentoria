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

		// Obriga a autenticar a URL
		super(new AntPathRequestMatcher(url));

		// Gerenciador de autentição
		setAuthenticationManager(authenticationManager);

	}

	// Retorna o usuário ao processar a autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		// Obter o usuário
		Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

		// Retorna o usuário(user) com login e senha
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		try {

			// Utilizando o serviço de autenticação JWTTokenAutenticacaoService
			new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());

		} catch (Exception e) {

			// Mostrar mensgem noo consoe
			e.printStackTrace();

		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		// Exceção de dados inválidos
		if (failed instanceof BadCredentialsException) {

			// Mostra mensagem
			response.getWriter().write("User e senha não encontrado");

		} else {

			// Mensagem genérica
			response.getWriter().write("Falha ao logar: " + failed.getMessage());
		}

	}

}
