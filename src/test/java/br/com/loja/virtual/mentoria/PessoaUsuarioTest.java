package br.com.loja.virtual.mentoria;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.loja.virtual.mentoria.controller.PessoaUsuarioController;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class PessoaUsuarioTest extends TestCase {

	@Autowired
	private PessoaUsuarioController pessoaController;

	@Test
	public void testeSalvarPessoaJuridica() throws LojaVirtualMentoriaException {

		// Instância de pessoaJuridica
		PessoaJuridica pessoaJuridica = new PessoaJuridica();

		// Seta os atributos de pessoaJuridica
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("italo");
		pessoaJuridica.setEmail("ija.services.tech@gmail.com");
		pessoaJuridica.setTelefone("81996245416");
		pessoaJuridica.setInscricaoEstadual("4654654654");
		pessoaJuridica.setInscricaoMunicipal("45655456456465");
		pessoaJuridica.setNomeFantasia("ijma services tecnologia");
		pessoaJuridica.setRazaoSocial("italo jose de melo araujo me");

		// Chamando o pessoaController
		pessoaController.salvarPessoaJuridica(pessoaJuridica);

	}

}
