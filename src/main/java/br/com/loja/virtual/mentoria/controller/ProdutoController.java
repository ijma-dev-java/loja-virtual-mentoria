package br.com.loja.virtual.mentoria.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.Produto;
import br.com.loja.virtual.mentoria.repository.ProdutoRepository;
import br.com.loja.virtual.mentoria.service.ServiceSendEmail;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ServiceSendEmail serviceSendEmail;

	@ResponseBody
	@PostMapping(value = "**/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto)
			throws LojaVirtualMentoriaException, IOException, MessagingException {

		// Varifica se o tipo da uniade está null ou vazia
		if (produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Tipo da unidade deve ser informada");

		}

		// Varifica tamanho minino do campo nome
		if (produto.getNome().length() < 10) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Nome do produto deve ter mais de 10 letras");

		}

		// Varifica se o ID é null ou menor que 0(zero)
		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Empresa responsável deve ser informada");

		}

		// Varifica se o ID é null
		if (produto.getId() == null) {

			// Consultar no banco de dados se já existe produto com o mesmo nome e empresa
			// por id
			List<Produto> produtos = produtoRepository.buscarProdutoByNomeByIdEmpresaLista(
					produto.getNome().toUpperCase().trim(), produto.getEmpresa().getId());

			// Se encontrar no banco de dados acesso com o mesmo nome
			if (!produtos.isEmpty()) {

				// Mostra mensagem para o cliente
				throw new LojaVirtualMentoriaException("Já existe produto com o nome: " + produto.getNome());

			}

		}

		// Varifica se o ID é null ou se ID é menor que 0(zero)
		if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Categoria do produto deve ser informada");

		}

		// Varifica se o ID é null ou se ID é menor que 0(zero)
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Marca do produto deve ser informada");

		}

		// Verifica se a quantidade no estoque é menor que 1
		if (produto.getQtdEstoque() < 1) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("O produto dever ter no minímo 1 produto no estoque");

		}

		// Verifica se a lista de imagens está null ou vazia ou o tamanho da lista está
		// 0(zero)
		if (produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Deve ser informado imagens para o produto");

		}

		// Verifica a quantidade minima de imagens
		if (produto.getImagens().size() < 3) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Deve ser informado pelo menos 3 imagens para o produto");
		}

		// Verifica a quantidade máxima de imagens
		if (produto.getImagens().size() > 6) {

			// Mostra mensagem para o cliente
			throw new LojaVirtualMentoriaException("Deve ser informado no máximo 6 imagens");

		}

		// Verifica se o id do produto está null
		if (produto.getId() == null) {

			// Varrendo a lista de imagens
			for (int x = 0; x < produto.getImagens().size(); x++) {

				// Seta o produto para lista de imagens
				produto.getImagens().get(x).setProduto(produto);

				// Seta a empresa do produto para a lista de imagens
				produto.getImagens().get(x).setEmpresa(produto.getEmpresa());

				// String vazia para armazenar o dados da imagem em base64
				String base64Image = "";

				// Verifica o conteúdo
				if (produto.getImagens().get(x).getImagemOriginal().contains("data:image")) {

					// adiciona uma ,(virgula)
					base64Image = produto.getImagens().get(x).getImagemOriginal().split(",")[1];

					// Caso contrário continua o processo normal
				} else {

					// continuação d processo normal
					base64Image = produto.getImagens().get(x).getImagemOriginal();

				}

				// Converta para byte[]
				byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

				// Fazendo a leitura através do objeto BufferedImage
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

				// Verifica se a imagem em byte[] é diferente de null
				if (bufferedImage != null) {

					// Atribui o tipo da imagem(extensão)
					int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();

					// Atribui a largura da imagem em pixel
					int largura = Integer.parseInt("800");

					// Atribui a altura da imagem em pixel
					int altura = Integer.parseInt("600");

					// Passando a largura - altura e o type da imagem
					BufferedImage resizedImage = new BufferedImage(largura, altura, type);

					// Instanciando o Graphics2D para criação gráfica
					Graphics2D g = resizedImage.createGraphics();

					// Passando as informações da imagem
					g.drawImage(bufferedImage, 0, 0, largura, altura, null);

					// Confirmando a utlizando do Graphics2D
					g.dispose();

					// Instanciando o objeto ByteArrayOutputStream
					ByteArrayOutputStream baos = new ByteArrayOutputStream();

					// Escrita da imagem
					ImageIO.write(resizedImage, "png", baos);

					// Pegando a imagem convertida(miniatura)
					String miniImgBase64 = "data:image/png;base64,"
							+ DatatypeConverter.printBase64Binary(baos.toByteArray());

					// Passando a imagem(miniatura) convertida para lista de imagens
					produto.getImagens().get(x).setImagemMiniatura(miniImgBase64);

					// Atualizando o bufferedImage
					bufferedImage.flush();

					// Atualizando o resizedImage
					resizedImage.flush();

					// Atualizando o baos
					baos.flush();

					// Finalizando o baos
					baos.close();

				}
			}
		}

		// Salvo no banco de dados
		Produto produtoSalvo = produtoRepository.save(produto);

		// Verifica a quantidade de produto no estoque
		if (produto.getAlertaQtdEstoque() && produto.getQtdEstoque() <= 1) {

			// Instanciando o StringBuilder para envio de e-mail em HTML
			StringBuilder html = new StringBuilder();

			// Setando o nome do produto com a quantido em estoque
			html.append("<h2>").append("Produto: " + produto.getNome())
					.append(" com estoque baixo: " + produto.getQtdEstoque());
			// Setando o ID do produto
			html.append("<p> Id Prod.:").append(produto.getId()).append("</p>");

			// Verifanco e-mail da empresa do produto
			if (produto.getEmpresa().getEmail() != null) {

				// Enviando e-mail para empresa do produto
				serviceSendEmail.enviarEmailHtml("Produto sem estoque", html.toString(),
						produto.getEmpresa().getEmail());

			}

		}

		// Retorna o objeto salvo
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "**/deleteProduto")
	public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) {

		// Deleta do banco de dados
		produtoRepository.deleteById(produto.getId());

		// Retorna a mensagem do objeto deletado
		return new ResponseEntity<String>("Produto removido", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteProdutoPorId/{id}")
	public ResponseEntity<?> deleteProdutoPorId(@PathVariable("id") Long id) {

		// Deleta do banco de dados por ID
		produtoRepository.deleteById(id);

		// Retorna a mensagem do objeto deletado
		return new ResponseEntity<String>("Produto removido", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarProdutoPorId/{id}")
	public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable("id") Long id) throws LojaVirtualMentoriaException {

		// Buscar do banco de dados por ID
		// Se não encontrar retorna null para evitar exceção
		Produto produto = produtoRepository.findById(id).orElse(null);

		// Se o produto estiver null
		if (produto == null) {

			// Mostra a mensagem customizada de produto não encontrato com o ID
			// consultado
			throw new LojaVirtualMentoriaException("Produto não encontrato com o ID: " + id);

		}

		// Retorna a consulta do objeto
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarProdutoByNomeLista/{nome}")
	public ResponseEntity<List<Produto>> buscarProdutoByNomeLista(@PathVariable("nome") String nome) {

		// Buscar do banco de dados por descricao
		List<Produto> produtos = produtoRepository.buscarProdutoByNomeLista(nome.toUpperCase().trim());

		// Retorna a consulta do objeto
		return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);

	}

}
