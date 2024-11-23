package br.com.loja.virtual.mentoria.security;

import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.loja.virtual.mentoria.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter implements HttpSessionListener {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// Ativando proteção de usuário que não está validado por token
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		// Desabilita configurações padrão do spring
		.disable()
		// Ativar o acesso livre ao contexto principal antes de logar
		.authorizeRequests().antMatchers("/").permitAll()
		// Permite acesso a página index para todos
		.antMatchers("/index").permitAll()
		// Evitando bloqueio de Cors no navegador para todos
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		// Redireciona ou retorna para a página index quando deslogar
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		// URL de logout do sistema
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		// Filtra as requisições para login de JWT - Depois
		.and().addFilterAfter(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		
		// Filtra as requisições para login de JWT - Antes
		.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		// Consultar o usuário no banco com Spring Security
		auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());

	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		// Ignorando URL no momento para pular serviço de autenticação
		/* 
		web.ignoring().antMatchers(
			HttpMethod.GET, "/salvarAcesso", "/deleteAcesso").antMatchers(
			HttpMethod.POST, "/salvarAcesso", "/deleteAcesso");
		*/

	}

}
