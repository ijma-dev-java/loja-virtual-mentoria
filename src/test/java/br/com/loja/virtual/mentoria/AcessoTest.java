package br.com.loja.virtual.mentoria;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.loja.virtual.mentoria.controller.AcessoController;
import br.com.loja.virtual.mentoria.model.Acesso;
import br.com.loja.virtual.mentoria.repository.AcessoRepository;
import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class AcessoTest extends TestCase {

	@Autowired
	private AcessoController acessoController;

	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private WebApplicationContext wac;
	
	/* ====  testeSalvarAcesso com Mockito ==== */
	
	@Test
	public void testeSalvarAcessoMockito() throws JsonProcessingException, Exception {
		
		// Instância do objeto DefaultMockMvcBuilder
		DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(this.wac);
		
		// Instância do objeto MockMvc que recebe a instância do objeto DefaultMockMvcBuilder
		MockMvc mockMvc = mockMvcBuilder.build();
		
		// Instância do objeto Acesso
		Acesso acesso = new Acesso();
		
		// Setando valores dos atributos do objeto acesso
		acesso.setDescricao("ROLE_COMPRADOR");
		
		// Instância do objeto ObjectMapper para trabalhar com json do jackson
		ObjectMapper objectMapper = new ObjectMapper();
		
		// ResultActions responsável pelo retorna da API
		ResultActions retornoApi = mockMvc
				// Chama o end-point
				.perform(MockMvcRequestBuilders.post("/salvarAcesso")
				// Passar o conteúdo de acesso em forma de json do jackson
				.content(objectMapper.writeValueAsString(acesso))
				// Tipo de dados
				.accept(MediaType.APPLICATION_JSON)
				// Tipo de conteúdo
				.contentType(MediaType.APPLICATION_JSON));
		
		// Mostre no console o retorno da API
		System.out.println("RETORNO DA API: " + retornoApi.andReturn().getResponse().getContentAsString());
		
		// Converter o retorno da API para um objeto de acesso utilizando o objeto ObjectMapper
		Acesso objetoRetorno = objectMapper
				// Lendo os valores de retorno da API Acesso
				.readValue(
				// Retorno da API em JSON
				retornoApi.andReturn().getResponse().getContentAsString(), 
				// Tipo da classe
				Acesso.class);
		
		// Validando se as informações salva no acesso.getDescricao()
		// é a mesma depois da conversão objetoRetorno.getDescricao()
		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
		
	}
	
	/* ====  testeDeleteAcessoMockito por POST ==== */
	
	@Test
	public void testeDeleteAcessoMockito() throws JsonProcessingException, Exception {
		
		// Instância do objeto DefaultMockMvcBuilder
		DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(this.wac);
		
		// Instância do objeto MockMvc que recebe a instância do objeto DefaultMockMvcBuilder
		MockMvc mockMvc = mockMvcBuilder.build();
		
		// Instância do objeto Acesso
		Acesso acesso = new Acesso();
		
		// Setando valores dos atributos do objeto acesso
		acesso.setDescricao("ROLE_COMPRADOR_DELETE");
		
		// Salva no banco de dados
		acesso = acessoRepository.save(acesso);
		
		// Instância do objeto ObjectMapper para trabalhar com json do jackson
		ObjectMapper objectMapper = new ObjectMapper();
		
		// ResultActions responsável pelo retorna da API
		ResultActions retornoApi = mockMvc
				// Chama o end-point
				.perform(MockMvcRequestBuilders.post("/deleteAcesso")
				// Passar o conteúdo de acesso em forma de json do jackson
				.content(objectMapper.writeValueAsString(acesso))
				// Tipo de dados
				.accept(MediaType.APPLICATION_JSON)
				// Tipo de conteúdo
				.contentType(MediaType.APPLICATION_JSON));
		
		// Mostre no console o retorno da API
		System.out.println("RETORNO DA API: " + retornoApi.andReturn().getResponse().getContentAsString());
		
		// Mostre no console o status de retorno da API
		System.out.println("STATUS DE RETORNO DA API: " + retornoApi.andReturn().getResponse().getStatus());
		
		// Validando se a mensagem de retorno da API está correta
		assertEquals("Acesso removido", retornoApi.andReturn().getResponse().getContentAsString());
		
		// Validando se o status de retorno da API é 200
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		
	}
	
	/* ====  testeDeleteAcessoMockito por DELETE ==== */
	
	@Test
	public void testeDeleteAcessoPorIdMockito() throws JsonProcessingException, Exception {
		
		// Instância do objeto DefaultMockMvcBuilder
		DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(this.wac);
		
		// Instância do objeto MockMvc que recebe a instância do objeto DefaultMockMvcBuilder
		MockMvc mockMvc = mockMvcBuilder.build();
		
		// Instância do objeto Acesso
		Acesso acesso = new Acesso();
		
		// Setando valores dos atributos do objeto acesso
		acesso.setDescricao("ROLE_COMPRADOR_DELETE_POR_ID");
		
		// Salva no banco de dados
		acesso = acessoRepository.save(acesso);
		
		// Instância do objeto ObjectMapper para trabalhar com json do jackson
		ObjectMapper objectMapper = new ObjectMapper();
		
		// ResultActions responsável pelo retorna da API
		ResultActions retornoApi = mockMvc
				// Chama o end-point
				.perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/" + acesso.getId())
				// Passar o conteúdo de acesso em forma de json do jackson
				.content(objectMapper.writeValueAsString(acesso))
				// Tipo de dados
				.accept(MediaType.APPLICATION_JSON)
				// Tipo de conteúdo
				.contentType(MediaType.APPLICATION_JSON));
		
		// Mostre no console o retorno da API
		System.out.println("RETORNO DA API: " + retornoApi.andReturn().getResponse().getContentAsString());
		
		// Mostre no console o status de retorno da API
		System.out.println("STATUS DE RETORNO DA API: " + retornoApi.andReturn().getResponse().getStatus());
		
		// Validando se a mensagem de retorno da API está correta
		assertEquals("Acesso removido", retornoApi.andReturn().getResponse().getContentAsString());
		
		// Validando se o status de retorno da API é 200
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		
	}
	
	/* ====  testeObterAcessoPorIdMockito por GET ==== */
	
	@Test
	public void testeObterAcessoPorIdMockito() throws JsonProcessingException, Exception {
		
		// Instância do objeto DefaultMockMvcBuilder
		DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(this.wac);
		
		// Instância do objeto MockMvc que recebe a instância do objeto DefaultMockMvcBuilder
		MockMvc mockMvc = mockMvcBuilder.build();
		
		// Instância do objeto Acesso
		Acesso acesso = new Acesso();
		
		// Setando valores dos atributos do objeto acesso
		acesso.setDescricao("ROLE_OBTER_ACESSO_POR_ID");
		
		// Salva no banco de dados
		acesso = acessoRepository.save(acesso);
		
		// Instância do objeto ObjectMapper para trabalhar com json do jackson
		ObjectMapper objectMapper = new ObjectMapper();
		
		// ResultActions responsável pelo retorna da API
		ResultActions retornoApi = mockMvc
				// Chama o end-point
				.perform(MockMvcRequestBuilders.get("/obterAcessoPorId/" + acesso.getId())
				// Passar o conteúdo de acesso em forma de json do jackson
				.content(objectMapper.writeValueAsString(acesso))
				// Tipo de dados
				.accept(MediaType.APPLICATION_JSON)
				// Tipo de conteúdo
				.contentType(MediaType.APPLICATION_JSON));
		
		// Validando se o status de retorno da API é 200
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		// Converter o retorno da API para um objeto de acesso utilizando o objeto ObjectMapper
		Acesso objetoRetorno = objectMapper
				// Lendo os valores de retorno da API Acesso
				.readValue(
				// Retorno da API em JSON
				retornoApi.andReturn().getResponse().getContentAsString(), 
				// Tipo da classe
				Acesso.class);
				
		// Mostre no console o retorno da API
		System.out.println("RETORNO DA API: " + retornoApi.andReturn().getResponse().getContentAsString());
		
		// Validando se as informações salva no acesso.getId()
		// é a mesma depois da conversão objetoRetorno.getId()
		assertEquals(acesso.getId(), objetoRetorno.getId());

		// Validando se as informações salva no acesso.getDescricao()
		// é a mesma depois da conversão objetoRetorno.getDescricao()
		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
		
	}
	
	/* ====  testeObterAcessoPorDescricaoMockito - por GET de ACESSOS ==== */
	
	@Test
	public void testeObterAcessoPorDescricaoMockito() throws JsonProcessingException, Exception {
		
		// Instância do objeto DefaultMockMvcBuilder
		DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(this.wac);
		
		// Instância do objeto MockMvc que recebe a instância do objeto DefaultMockMvcBuilder
		MockMvc mockMvc = mockMvcBuilder.build();
		
		// Instância do objeto Acesso
		Acesso acesso = new Acesso();
		
		// Setando valores dos atributos do objeto acesso
		acesso.setDescricao("ROLE_OBTER_ACESSOS_POR_DESCRICAO");
		
		// Salva no banco de dados
		acesso = acessoRepository.save(acesso);
		
		// Instância do objeto ObjectMapper para trabalhar com json do jackson
		ObjectMapper objectMapper = new ObjectMapper();
		
		// ResultActions responsável pelo retorna da API
		ResultActions retornoApi = mockMvc
				// Chama o end-point
				.perform(MockMvcRequestBuilders.get("/obterAcessoPorDescricao/POR_DESC")
				// Passar o conteúdo de acesso em forma de json do jackson
				.content(objectMapper.writeValueAsString(acesso))
				// Tipo de dados
				.accept(MediaType.APPLICATION_JSON)
				// Tipo de conteúdo
				.contentType(MediaType.APPLICATION_JSON));
		
		// Validando se o status de retorno da API é 200
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		// Converter o retorno da API para uma lista de objeto de acesso utilizando o objeto ObjectMapper
		List<Acesso> objetosRetorno = objectMapper
				// Lendo os valores de retorno da API Acesso
				.readValue(
				// Retorno da API em JSON
				retornoApi.andReturn().getResponse().getContentAsString(), 
				// Tipo da classe
				new TypeReference<List<Acesso>>() {});
		
		// Validando se retorna apenas um registro encontrado
		assertEquals(1, objetosRetorno.size());
		
		// Validando se as informações salva no acesso.getDescricao()
		// é a mesma depois da conversão da lista objetosRetorno na posição inicial
		// do Retorno.getDescricao()
		assertEquals(acesso.getDescricao(), objetosRetorno.get(0).getDescricao());
		
		// Deleta do banco de dados por ID
		acessoRepository.deleteById(acesso.getId());
		
	}
	
	/* ====  testeSalvarAcesso com Junit ==== */

	@Test
	public void testeSalvarAcessoJunit() throws LojaVirtualMentoriaException {

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
