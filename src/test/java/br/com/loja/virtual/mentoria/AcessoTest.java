package br.com.loja.virtual.mentoria;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.loja.virtual.mentoria.controller.AcessoController;
import br.com.loja.virtual.mentoria.model.Acesso;

@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class AcessoTest {

	@Autowired
	private AcessoController acessoController;

	@Test
	public void testeSalvarAcesso() {

		// Instância do objeto
		Acesso acesso = new Acesso();

		// Setando os valores dos atributos
		acesso.setDescricao("ROLE_USER_T_C_S_R");

		// Chama a classe de controller
		acessoController.salvarAcesso(acesso);

	}

}
