package br.com.loja.virtual.mentoria.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

// Filtra onde todas as requisições serão capturadas para autenticar
public class JWTApiAutenticacaoFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// Estabele a autentição do Usuário
		Authentication authentication = new JWTTokenAutenticacaoService().getAuthetication((HttpServletRequest) request,
				(HttpServletResponse) response);

		// Coloca o processo de autenticação para o spring secutiry
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Continua o processo
		chain.doFilter(request, response);

	}

}
