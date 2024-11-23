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

// Filtro onde todas as requisições serão capturadas para autenticar
public class JWTApiAutenticacaoFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
		
		// Estabele a autentição do user com a utilização do JWTTokenAutenticacaoService
		Authentication authentication = new JWTTokenAutenticationService()
				.getAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
		
		// Coloca o processo de autenticação para o Spring Secutiry
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Continua
		chain.doFilter(request, response);
		
		}catch (Exception e) {
			
			// Mostra a mensgem de erro no console
			e.printStackTrace();
			
			// Mostra a mensgem genérica de erro para o cliente
			response.getWriter().write("Ocorreu um erro no sistema, avise ao administrador: \n" + e.getMessage());
			
		}
		
	}

}
