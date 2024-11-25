package br.com.loja.virtual.mentoria;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.loja.virtual.mentoria.controller.PessoaUsuarioController;
import br.com.loja.virtual.mentoria.enums.TipoEndereco;
import br.com.loja.virtual.mentoria.model.Endereco;
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
		pessoaController.salvarPessoaJuridica(pessoaJuridica);

	}

}
