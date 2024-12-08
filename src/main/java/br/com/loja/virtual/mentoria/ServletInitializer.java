package br.com.loja.virtual.mentoria;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

		// Retornando a classe inicial do projeto
		return builder.sources(LojaVirtualMentoriaApplication.class);

	}

}
