package br.com.loja.virtual.mentoria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.virtual.mentoria.LojaVirtualMentoriaException;
import br.com.loja.virtual.mentoria.model.Acesso;
import br.com.loja.virtual.mentoria.repository.AcessoRepository;
import br.com.loja.virtual.mentoria.service.AcessoService;

@RestController
@Controller
public class AcessoController {

	@Autowired
	private AcessoService acessoService;

	@Autowired
	private AcessoRepository acessoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarAcesso")
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws LojaVirtualMentoriaException {

		// Validando se o ID é null
		if (acesso.getId() == null) {

			// Consultar no banco de dados se já existe acesso com a mesma descrição
			List<Acesso> acessos = acessoRepository.buscarAcessoByDescricao(acesso.getDescricao().toUpperCase().trim());

			// Se encontrar no banco de dados acesso com a mesma descrição
			if (!acessos.isEmpty()) {

				// Mostra mensagem para o cliente
				throw new LojaVirtualMentoriaException("Já existe acesso com a descrição: " + acesso.getDescricao());

			}

		}

		// Chama a classe de serviço
		Acesso acessoSalvo = acessoService.salvarAcesso(acesso);

		// Retorna o objeto salvo
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "**/deleteAcesso")
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) {

		// Deleta do banco de dados
		acessoRepository.deleteById(acesso.getId());

		// Retorna a mensagem do objeto deletado
		return new ResponseEntity("Acesso removido", HttpStatus.OK);

	}

	// @CrossOrigin(origins = { "www.ijma.com.br" })
	// @Secured({ "ROLE_ADMIN", "ROLE_GERENTE" }) - Permissão de acesso
	@ResponseBody
	@DeleteMapping(value = "**/deleteAcessoPorId/{id}")
	public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) {

		// Deleta do banco de dados por ID
		acessoRepository.deleteById(id);

		// Retorna a mensagem do objeto deletado
		return new ResponseEntity("Acesso removido", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/obterAcessoPorId/{id}")
	public ResponseEntity<Acesso> obterAcessoPorId(@PathVariable("id") Long id) throws LojaVirtualMentoriaException {

		// Buscar do banco de dados por ID
		// Se não encontrar retorna null para evitar exceção
		Acesso acesso = acessoRepository.findById(id).orElse(null);
		
		// Se o acesso estiver null
		if (acesso == null) {
			
			// Mostra a mensagem customizada de acesso não não encontrato com o ID consultado
			throw new LojaVirtualMentoriaException("Acesso não encontrato com o ID: " + id);
			
		}

		// Retorna a consulta do objeto
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/obterAcessoPorDescricao/{descricao}")
	public ResponseEntity<List<Acesso>> obterAcessoPorDescricao(@PathVariable("descricao") String descricao) {

		// Buscar do banco de dados por descricao
		List<Acesso> acessos = acessoRepository.buscarAcessoByDescricao(descricao.toUpperCase().trim());

		// Retorna a consulta do objeto
		return new ResponseEntity<List<Acesso>>(acessos, HttpStatus.OK);

	}

}
