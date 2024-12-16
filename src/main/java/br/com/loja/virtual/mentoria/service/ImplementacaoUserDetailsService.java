package br.com.loja.virtual.mentoria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.loja.virtual.mentoria.model.Usuario;
import br.com.loja.virtual.mentoria.repository.UsuarioRepository;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// Buscar no banco de dados
		Usuario usuario = usuarioRepository.findUserByLogin(username);

		// Verifica se o usuário é null
		if (usuario == null) {
			// Mostra a mensagem
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}

		// Retorna o usuário com login - senha e acessos
		return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
	}

}
