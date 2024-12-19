package br.com.loja.virtual.mentoria;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.loja.virtual.mentoria.model.dto.ObjetoErroDTO;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {

	@ExceptionHandler(LojaVirtualMentoriaException.class)
	public ResponseEntity<Object> handleExceptionCustom(LojaVirtualMentoriaException ex) {

		// Instanciando o ObjetoErroDTO
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		// Setando os erros
		objetoErroDTO.setError(ex.getMessage());
		objetoErroDTO.setCode(HttpStatus.OK.toString());

		// Retorno o DTO com os erros
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.OK);
	}

	// Captura execeções do projeto
	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		// Instanciando o ObjetoErroDTO
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		// Variavel que será usada para mostrar os erros
		String msg = "";

		// Verificando a classe de exceção
		if (ex instanceof MethodArgumentNotValidException) {

			// Adicionando todos os erros encontrado numa lista
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

			// Varrendo da lista com os erros
			for (ObjectError objectError : list) {

				// Adicionando os erros a vaviável de mensagem para
				// mostrar os erros para mostrar na tela
				msg += objectError.getDefaultMessage() + "\n";

			}

			// Verificando a classe de exceção
		} else if (ex instanceof HttpMessageNotReadableException) {

			msg = "Não está sendo enviado dados para o BODY corpo da requisição";

			// Se for de outra classe
		} else {

			// Mostrar os erros com mensagem genérica
			msg = ex.getMessage();

		}

		// Setando os erros
		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());

		// Mostrando mensagem de erro no console
		ex.printStackTrace();

		// Retorno o DTO com os erros
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	// Captura erro na parte de banco
	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class })
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {

		// Instanciando o ObjetoErroDTO
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		// Variavel que será usada para mostrar os erros
		String msg = "";

		// Verificando a classe de exceção
		if (ex instanceof DataIntegrityViolationException) {

			// Mostrar mensagem de erro
			msg = "Erro de integridade no banco: "
					+ ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();

			// Verificando a classe de exceção
		} else if (ex instanceof ConstraintViolationException) {

			// Mostrar mensagem de erro
			msg = "Erro de chave estrangeira: "
					+ ((ConstraintViolationException) ex).getCause().getCause().getMessage();

			// Verificando a classe de exceção
		} else if (ex instanceof SQLException) {

			// Mostrar mensagem de erro
			msg = "Erro de SQL do Banco: " + ((SQLException) ex).getCause().getCause().getMessage();

		} else {

			// Mostrar os erros com mensagem genérica
			msg = ex.getMessage();

		}

		// Setando os erros
		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

		// Mostrando mensagem de erro no console
		ex.printStackTrace();

		// Retorno o DTO com os erros
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
