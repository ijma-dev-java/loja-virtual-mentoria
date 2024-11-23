package br.com.loja.virtual.mentoria;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	// Responsável por captura execeções do projeto
	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		// Instância do objeto DTO para mostra os erros
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		// Iniciar uma variável vazia que será utilizada para armazenar as mensagens de
		// erro
		String msg = "";

		// Instância da classe de argumento do método não é exceção válida
		if (ex instanceof MethodArgumentNotValidException) {

			// Mostrar todos os erros
			List<ObjectError> objectErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

			// Varrendo a lista de objeto de erros
			for (ObjectError objectError : objectErrors) {

				// Mostrando a mensagem padrão com os erros encontrados
				msg += objectError.getDefaultMessage() + "\n";

			}

		} else {

			// Mensagem genérica
			msg = ex.getMessage();

		}

		// Setando a mensgem de erro
		objetoErroDTO.setError(msg);

		// Setando o código de erro da mensagem
		objetoErroDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());

		// Monstrando mensagem de erro no console
		ex.printStackTrace();

		// Retorna o objeto de erro DTO
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	// Captura erro na parte de banco de dados
	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class })
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {

		// Instância do objeto DTO para mostra os erros
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		// Iniciar uma variável vazia que será utilizada para armazenar as mensagens de
		// erro
		String msg = "";

		// Se for exceção de integridade no banco de dados
		if (ex instanceof DataIntegrityViolationException) {

			// Mostrar a mensgem de exceção de integridade no banco de dados
			msg = "Erro de integridade no banco de dados: "
					+ ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();

			// Se for exceção de chave estrangeira
		} else if (ex instanceof ConstraintViolationException) {

			// Mostrar a mensgem de exceção de chave estrangeira
			msg = "Erro de chave estrangeira: "
					+ ((ConstraintViolationException) ex).getCause().getCause().getMessage();

			// Se for exceção de sql no banco de dados
		} else if (ex instanceof SQLException) {

			// Mostrar a mensgem de exceção de sql no banco de dados
			msg = "Erro de SQL do Banco: " + ((SQLException) ex).getCause().getCause().getMessage();

		} else {

			// Mensagem genérica
			msg = ex.getMessage();

		}

		// Setando a mensgem de erro
		objetoErroDTO.setError(msg);

		// Setando o código de erro da mensagem
		objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

		// Monstrando mensagem de erro no console
		ex.printStackTrace();

		// Retorna o objeto de erro DTO
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
