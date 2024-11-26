package br.com.loja.virtual.mentoria;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.loja.virtual.mentoria.controller.PessoaUsuarioController;
import br.com.loja.virtual.mentoria.enums.TipoEndereco;
import br.com.loja.virtual.mentoria.model.Endereco;
import br.com.loja.virtual.mentoria.model.PessoaFisica;
import br.com.loja.virtual.mentoria.model.PessoaJuridica;
import br.com.loja.virtual.mentoria.repository.PessoaJuridicaRepository;
import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class PessoaUsuarioTest extends TestCase {

	@Autowired
	private PessoaUsuarioController pessoaUsuarioController;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

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

		// Instância de enderecoCobranca
		Endereco enderecoCobranca = new Endereco();

		// Seta os atributos de enderecoCobranca
		enderecoCobranca.setLogradouro("sdfsdfsdfsdfsdf");
		enderecoCobranca.setCep("55554545");
		enderecoCobranca.setNumero("3223");
		enderecoCobranca.setComplemento("fsfsfsfsd");
		enderecoCobranca.setBairro("fdsfsfsfsd");
		enderecoCobranca.setCidade("adasdasdadasd");
		enderecoCobranca.setUf("PE");
		enderecoCobranca.setTipoEndereco(TipoEndereco.COBRANCA);
		enderecoCobranca.setPessoa(pessoaJuridica);
		enderecoCobranca.setEmpresa(pessoaJuridica);

		// Instância de enderecoEntrega
		Endereco enderecoEntrega = new Endereco();

		// Seta os atributos de enderecoEntrega
		enderecoEntrega.setLogradouro("sdfsdfsdfsdfsdf");
		enderecoEntrega.setCep("55554545");
		enderecoEntrega.setNumero("3223");
		enderecoEntrega.setComplemento("fsfsfsfsd");
		enderecoEntrega.setBairro("fdsfsfsfsd");
		enderecoEntrega.setCidade("adasdasdadasd");
		enderecoEntrega.setUf("PE");
		enderecoEntrega.setTipoEndereco(TipoEndereco.ENTREGA);
		enderecoEntrega.setPessoa(pessoaJuridica);
		enderecoEntrega.setEmpresa(pessoaJuridica);

		// Adicionando o enderecoCobranca na lista da pessoaJuridica
		pessoaJuridica.getEnderecos().add(enderecoCobranca);

		// Adicionando o enderecoEntrega na lista da pessoaJuridica
		pessoaJuridica.getEnderecos().add(enderecoEntrega);

		// Chamando o pessoaController
		pessoaJuridica = pessoaUsuarioController.salvarPessoaJuridica(pessoaJuridica).getBody();

		// Validando se o ID é maior que ZERO após salvar
		// com os dados de endereços
		assertEquals(true, pessoaJuridica.getId() > 0);

		// Varrendo a lista de endereços
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {

			// Validando se foi gerando ID para endereços
			assertEquals(true, endereco.getId() > 0);

		}

		// Validando se cadastros os endereços:
		// Endereço de cobrança e de entrega
		assertEquals(2, pessoaJuridica.getEnderecos().size());

	}

	@Test
	public void testeSalvarPessoaFisica() throws LojaVirtualMentoriaException {

		// Buscar CNPJ cadastrado
		PessoaJuridica pessoaJuridica = pessoaJuridicaRepository.buscarCnpjCadastrado("1732420760758");

		// Instância de pessoaJuridica
		PessoaFisica pessoaFisica = new PessoaFisica();

		// Seta os atributos de pessoaJuridica
		pessoaFisica.setCpf("01895117020");
		pessoaFisica.setNome("italo");
		pessoaFisica.setEmail("ijma.services.tech@gmail.com");
		pessoaFisica.setTelefone("81996245416");
		pessoaFisica.setTipoPessoa("FISICA");
		pessoaFisica.setEmpresa(pessoaJuridica);

		// Instância de enderecoCobranca
		Endereco enderecoCobranca = new Endereco();

		// Seta os atributos de enderecoCobranca
		enderecoCobranca.setLogradouro("sdfsdfsdfsdfsdf");
		enderecoCobranca.setCep("55554545");
		enderecoCobranca.setNumero("3223");
		enderecoCobranca.setComplemento("fsfsfsfsd");
		enderecoCobranca.setBairro("fdsfsfsfsd");
		enderecoCobranca.setCidade("adasdasdadasd");
		enderecoCobranca.setUf("PE");
		enderecoCobranca.setTipoEndereco(TipoEndereco.COBRANCA);
		enderecoCobranca.setPessoa(pessoaFisica);
		enderecoCobranca.setEmpresa(pessoaJuridica);

		// Instância de enderecoEntrega
		Endereco enderecoEntrega = new Endereco();

		// Seta os atributos de enderecoEntrega
		enderecoEntrega.setLogradouro("sdfsdfsdfsdfsdf");
		enderecoEntrega.setCep("55554545");
		enderecoEntrega.setNumero("3223");
		enderecoEntrega.setComplemento("fsfsfsfsd");
		enderecoEntrega.setBairro("fdsfsfsfsd");
		enderecoEntrega.setCidade("adasdasdadasd");
		enderecoEntrega.setUf("PE");
		enderecoEntrega.setTipoEndereco(TipoEndereco.ENTREGA);
		enderecoEntrega.setPessoa(pessoaFisica);
		enderecoEntrega.setEmpresa(pessoaJuridica);

		// Adicionando o enderecoCobranca na lista da pessoaFisica
		pessoaFisica.getEnderecos().add(enderecoCobranca);

		// Adicionando o enderecoEntrega na lista da pessoaFisica
		pessoaFisica.getEnderecos().add(enderecoEntrega);

		// Chamando o pessoaController
		pessoaFisica = pessoaUsuarioController.salvarPessoaFisica(pessoaFisica).getBody();

		// Validando se o ID é maior que ZERO após salvar
		// com os dados de endereços
		assertEquals(true, pessoaFisica.getId() > 0);

		// Varrendo a lista de endereços
		for (Endereco endereco : pessoaFisica.getEnderecos()) {

			// Validando se foi gerando ID para endereços
			assertEquals(true, endereco.getId() > 0);

		}

		// Validando se cadastros os endereços:
		// Endereço de cobrança e de entrega
		assertEquals(2, pessoaFisica.getEnderecos().size());

	}

}
