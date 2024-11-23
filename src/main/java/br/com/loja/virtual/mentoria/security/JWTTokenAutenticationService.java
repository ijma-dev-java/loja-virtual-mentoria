package br.com.loja.virtual.mentoria.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.ApplicationContextLoad;
import br.com.loja.virtual.mentoria.model.Usuario;
import br.com.loja.virtual.mentoria.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

// Responsável por criar a autenticação e retonar também a autenticação JWT
@Service
@Component
public class JWTTokenAutenticationService {

	// Token de validade de 11 dias
	private static final long EXPIRATION_TIME = 959990000;

	// Chave de senha para juntar com o JWT
	private static final String SECRET = "ss/-*-*sds565dsd-s/d-s*dsds";

	// Prefixo
	private static final String TOKEN_PREFIX = "Bearer";

	// Cabeçalho
	private static final String HEADER_STRING = "Authorization";

	// Gera o token e da a responsta para o cliente com o JWT
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {

		// Chama o gerador do token
		String JWT = Jwts.builder()
				// Adiciona o usuário
				.setSubject(username)
				// Seta a data de expiração
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				// Assinatura em HS512 com o tempo de expiração compactado
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		// Bearer
		// *-/a*dad9s5d6as5d4s5d4s45dsd54s.sd4s4d45s45d4sd54d45s4d5s.ds5d5s5d5s65d6s6d
		String token = TOKEN_PREFIX + " " + JWT;

		// Dá a resposta para tela e para o cliente,
		// para outra API, para o navegador, para o aplicativo,
		// para o javascript ou para outra chamada Java
		response.addHeader(HEADER_STRING, token);

		// Chama o liberacaoCors para fazer a liberação contra erro de Cors no navegador
		liberacaoCors(response);

		// Usado para ver no Postman para teste
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}

	// Retorna o usuário validado com token, caso contrário retona null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// Recuperar o token por requisição
		String token = request.getHeader(HEADER_STRING);

		try {

			// Validar se existe token
			if (token != null) {

				// Remover espaços em branco do token e atribuir a uma variável
				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

				// Fazer a validação do token do usuário na requisição e obter o USER
				String user = Jwts.parser()
						// Setando a chave de assinatura com a senha secreta
						.setSigningKey(SECRET)
						// Passando o token sem os espaços em branco
						.parseClaimsJws(tokenLimpo)
						// Retornando para o corpo
						.getBody()
						// o usuário autenticado
						.getSubject();

				// Validando se o usuário realmente existe
				if (user != null) {

					// Consultando o usuário com o carregador de contexto
					// ApplicationContextLoad criado para o projeto
					Usuario usuario = ApplicationContextLoad
							// Pega o contexto
							.getApplicationContext()
							// Pega a interface do usuário
							.getBean(UsuarioRepository.class)
							// Consulta o usuário no banco de dados por login
							.buscarUsuarioPorLogin(user);

					// Validando se o usuário realmente existe
					if (usuario != null) {

						// Se autenticou retorna o login, a senha e as permissões
						return new UsernamePasswordAuthenticationToken(
								// Login
								usuario.getLogin(),
								// Senha
								usuario.getSenha(),
								// Permissões
								usuario.getAuthorities());

					}

				}

			}

		} catch (SignatureException e) {

			// Retorne a mensagem de token está inválido
			response.getWriter().write("Token está inválido");

		} catch (ExpiredJwtException e) {

			// Retorne a mensagem de token está expirado
			response.getWriter().write("Token está expirado, efetue o login novamente");

		} finally {

			// Chama o liberacaoCors para fazer a liberação contra erro de Cors no navegador
			liberacaoCors(response);

		}

		// Se não autenticar retorna null
		return null;

	}

	// Fazendo liberação contra erro de Cors no navegador
	private void liberacaoCors(HttpServletResponse response) {

		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}

	}

}
