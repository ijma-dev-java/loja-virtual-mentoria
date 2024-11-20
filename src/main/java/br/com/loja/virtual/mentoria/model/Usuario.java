package br.com.loja.virtual.mentoria.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuario")
@SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1, initialValue = 1)
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
	private Long id;

	private String login;

	private String senha;

	@Temporal(TemporalType.DATE)
	private Date dataAtualSenha;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "usuario_acesso",
			uniqueConstraints = @UniqueConstraint (columnNames = {"usuario_id", "acesso_id"},
			name = "usuario_acesso_unique"),
	joinColumns = @JoinColumn(
			name = "usuario_id",
			referencedColumnName = "id",
			table = "usuario",
			unique = false,
			foreignKey = @ForeignKey(
					name = "usuario_fk",
					value = ConstraintMode.CONSTRAINT)),
	inverseJoinColumns = @JoinColumn(
			name = "acesso_id",
			unique = false,
			referencedColumnName = "id",
			table = "acesso",
			foreignKey = @ForeignKey(
					name = "acesso_fk",
					value = ConstraintMode.CONSTRAINT)))
	private List<Acesso> acessos;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { // Acessos
		return this.acessos;
	}

	@Override
	public String getPassword() { // Senha do usuário
		return this.senha;
	}

	@Override
	public String getUsername() { // Login do usuário
		return this.login;
	}

	@Override
	public boolean isAccountNonExpired() { // Conta não expira
		return true;
	}

	@Override
	public boolean isAccountNonLocked() { // Conta não está bloqueada
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { // Credenciais não expiraram
		return true;
	}

	@Override
	public boolean isEnabled() { // Conta está ativa
		return true;
	}

}
