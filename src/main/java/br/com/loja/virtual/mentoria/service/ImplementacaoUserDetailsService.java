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

		// Recebendo o login para consultar no banco de dados
		Usuario usuario = usuarioRepository.buscarUsuarioPorLogin(username);

		// Mostrando uma mensagem se o usuário não for encontrado
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		// Retorna o login a senha e as permissões de acesso
		return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());

	}

}
