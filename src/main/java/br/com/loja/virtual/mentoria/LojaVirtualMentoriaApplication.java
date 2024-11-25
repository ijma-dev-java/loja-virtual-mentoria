package br.com.loja.virtual.mentoria;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = { "br.com.loja.virtual.mentoria.model" })
@ComponentScan(basePackages = { "br.*" })
@EnableJpaRepositories(basePackages = { "br.com.loja.virtual.mentoria.repository" })
@EnableTransactionManagement
public class LojaVirtualMentoriaApplication implements AsyncConfigurer {

	public static void main(String[] args) {

		SpringApplication.run(LojaVirtualMentoriaApplication.class, args);

	}

	@Override
	@Bean
	public Executor getAsyncExecutor() {

		// Instância do ThreadPoolTaskExecutor
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		
		// Setando o tamanho do pool principal
		threadPoolTaskExecutor.setCorePoolSize(10);
		// Setando o tamanho do pool máximo
		threadPoolTaskExecutor.setMaxPoolSize(20);
		// Setando a defilinição da fila
		threadPoolTaskExecutor.setQueueCapacity(500);
		// Setando o prefixo padrão
		threadPoolTaskExecutor.setThreadNamePrefix("Assyncrono Thread");
		
		// Inicializando
		threadPoolTaskExecutor.initialize();
		
		// Retornando o objeto threadPoolTaskExecutor
		return threadPoolTaskExecutor;

	}

}
