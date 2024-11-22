package br.com.loja.virtual.mentoria;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.loja.virtual.mentoria.controller.AcessoController;
import br.com.loja.virtual.mentoria.model.Acesso;
import br.com.loja.virtual.mentoria.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class AcessoTest extends TestCase {

	@Autowired
	private AcessoController acessoController;

	@Autowired
	private AcessoRepository acessoRepository;

	@Test
	public void testeSalvarAcesso() {

		// Instância do objeto
		Acesso acesso = new Acesso();

		// Setando os valores dos atributos
		acesso.setDescricao("ROLE_JUNIT_APROVADO");

		// Validando se o ID é null
		assertEquals(true, acesso.getId() == null);

		// Chama a classe de controller
		acesso = acessoController.salvarAcesso(acesso).getBody();

		// Validando se gravou o acesso com novo ID, sendo maior que 0
		assertEquals(true, acesso.getId() > 0);

		// Validando se gravou o acesso com novo ID, sendo menor que 0
		// assertEquals(true, acesso.getId() < 0);

		// Validando se gravou com a descrição como: ROLE_JUNIT_APROVADO
		assertEquals("ROLE_JUNIT_APROVADO", acesso.getDescricao());

		// Pegar o ID do objeto acesso e atribuindo ao acesso novo
		Acesso acessoNovo = acessoRepository.findById(acesso.getId()).get();

		// Validando se o ID do objeto acesso é igual ao objeto acessoNovo
		assertEquals(acesso.getId(), acessoNovo.getId());

		// Deletando objeto acessoNovo por id
		acessoRepository.deleteById(acessoNovo.getId());

		// Rodar esse SQL anterior de delete por ID no banco de dados
		acessoRepository.flush();

		// Validando se o objeto acessoNovo foi encontrado,
		// caso contrário retorne null para evitar exception
		// e atribuindo ao objeto acessoDiferente
		Acesso acessoDiferente = acessoRepository.findById(acessoNovo.getId()).orElse(null);

		// Validando se o objeto acessoDiferente é igual null
		assertEquals(true, acessoDiferente == null);
		
		// Instância de novo acesso
		acesso = new Acesso();
		
		// Setando valores do atributo
		acesso.setDescricao("Acesso para teste de Query");
		
		// Salvando no banco de dados
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		// Atribuinda a lista acessos a busca acesso por descricao
		List<Acesso> acessos = acessoRepository.buscarAcessoByDescricao("Query".trim().toUpperCase());
		
		// Verificando se tem 1 registro encontrado
		assertEquals(1, acessos.size());
		
		// Deleta o acesso encontrado após a consulta
		acessoRepository.deleteById(acesso.getId());

	}

}
