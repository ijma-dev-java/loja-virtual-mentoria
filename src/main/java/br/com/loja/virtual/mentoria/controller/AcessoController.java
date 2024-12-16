package br.com.loja.virtual.mentoria.controller;

import java.util.List;

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
import br.com.loja.virtual.mentoria.model.Acesso;
import br.com.loja.virtual.mentoria.repository.AcessoRepository;
import br.com.loja.virtual.mentoria.service.AcessoService;

@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;

	@Autowired
	private AcessoRepository acessoRepository;

	@ResponseBody
	@PostMapping(value = "salvarAcesso")
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws LojaVirtualMentoriaException {

		// Verificando se o id está nulo
		if (acesso.getId() == null) {

			// Buscar no banco de dados se existe por descrição
			List<Acesso> acessos = acessoRepository.buscarAcessoByDescricao(acesso.getNomeDesc().toUpperCase());

			// Verifica se tem registro no mesma descrição
			if (!acessos.isEmpty()) {

				// Mostra mensagem de registro existente
				throw new LojaVirtualMentoriaException("Já existe acesso com a descrição: " + acesso.getNomeDesc());

			}

		}

		// Salva no banco de dados
		Acesso acessoSalvo = acessoService.save(acesso);

		// Retorna o objeto salvo
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);

	}

	@ResponseBody
	@PostMapping(value = "deleteAcesso")
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) {

		// Deleta do banco de dados
		acessoRepository.deleteById(acesso.getId());

		// Retorna uma mensagem
		return new ResponseEntity<String>("Acesso Removido", HttpStatus.OK);

	}

	// @Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })
	@ResponseBody
	@DeleteMapping(value = "deleteAcessoById/{id}")
	public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) throws LojaVirtualMentoriaException {

		// Consulta no banco de dados
		Acesso acesso = acessoRepository.findById(id).orElse(null);

		// Verifica se o registro está nulo
		if (acesso == null) {

			// Mostrar mensagem de registro inexistente
			throw new LojaVirtualMentoriaException("Não encontrou Acesso com código: " + id);

		}

		// Deletar do banco de dados
		acessoRepository.deleteById(id);

		// Retorna uma mensagem
		return new ResponseEntity("Acesso Removido", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "obterAcesso/{id}")
	public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) throws LojaVirtualMentoriaException {

		// Consulta no banco de dados
		Acesso acesso = acessoRepository.findById(id).orElse(null);

		// Verifica se o registro está nulo
		if (acesso == null) {

			// Mostrar mensagem de registro inexistente
			throw new LojaVirtualMentoriaException("Não encontrou Acesso com código: " + id);

		}

		// Retorna uma mensagem
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "buscarAcessoByDescricao/{nomeDesc}")
	public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("nomeDesc") String nomeDesc) {

		// Buscar no banco de dados
		List<Acesso> acesso = acessoRepository.buscarAcessoByDescricao(nomeDesc.toUpperCase());

		// Retorno do objeto com os dados
		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);

	}

}