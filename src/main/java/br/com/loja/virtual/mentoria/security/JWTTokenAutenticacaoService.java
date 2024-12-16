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
public class JWTTokenAutenticacaoService {

	// Token com validade de 11 dias
	private static final long EXPIRATION_TIME = 959990000;

	/// Chave de senha secreta para juntar com o JWT
	private static final String SECRET = "ss/-*-*sds565dsd-s/d-s*dsds";

	// Prefixo do Token
	private static final String TOKEN_PREFIX = "Bearer";

	// Prefixo do cabeçalho do Token
	private static final String HEADER_STRING = "Authorization";

	// Gerar o token e dar a responsta para o cliente com o JWT
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {

		// Chama o gerador de token
		String JWT = Jwts.builder()
				// Adiciona o usuário que vem por parametro
				.setSubject(username)
				// Seta o tempo de expiração com a data atual do sistema
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				// Compacta o tempo de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		// Exemplo do token gerado: Bearer fsfsfsfsd3534.ffsdf5s4d5fd5s.dfhfghfh5d6s6d

		// Juntando o prefixo com espaços em branco e com token gerado
		String token = TOKEN_PREFIX + " " + JWT;

		// Adiciona o token ao cabeçalho
		response.addHeader(HEADER_STRING, token);

		// Chama o método de liberação contra erro de cors no navegador
		liberacaoCors(response);

		// Usado para ver no Postman para teste
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}

	// Responsável por retornar o usuário validado com token
	// ou caso não seja validado retona null
	public Authentication getAuthetication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// Recuperando o token que foi adicioando ao cabeçalho
		String token = request.getHeader(HEADER_STRING);

		try {

			// Verifica se existe token
			if (token != null) {

				// Remove os prefixo e os espaços em branco do token
				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

				// Faz a validação do token do usuário na requisição e obtem o usuário
				String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenLimpo).getBody().getSubject();

				// Verifica se existe usário
				if (user != null) {

					// Buscando usuário no banco de dados utlizando o ApplicationContextLoad
					Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
							.findUserByLogin(user);

					// Verifica se existe usuário
					if (usuario != null) {

						// Retorna o usuário com a senha e com seus acessos
						return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
								usuario.getAuthorities());
					}

				}

			}

			// Exceção de token inválido
		} catch (SignatureException e) {

			// Mostre a mensagem de token inválido
			response.getWriter().write("Token está inválido.");

			// Exceção de token expirado
		} catch (ExpiredJwtException e) {

			// Mostre a mensagem de token expirado
			response.getWriter().write("Token está expirado, efetue o login novamente" + "");

		} finally {

			// Chama o método de liberação contra erro de Cors no navegador
			liberacaoCors(response);

		}

		// Retorna null se não encontrar o usuário
		return null;

	}

	// Responsável por fazer a liberação contra erro de Cors no navegador
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