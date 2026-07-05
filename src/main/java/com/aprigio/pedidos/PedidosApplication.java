package com.aprigio.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @SpringBootApplication é uma annotation "3 em 1". Ela junta:
 *
 *  - @Configuration        -> marca a classe como fonte de definições de beans.
 *  - @EnableAutoConfiguration -> liga a auto-configuração do Spring Boot. É ela que,
 *                             ao ver o driver do Postgres no classpath + as props de
 *                             datasource, configura sozinho o DataSource, o JPA, etc.
 *  - @ComponentScan        -> manda o Spring VARRER este pacote (com.aprigio.pedidos)
 *                             e todos os subpacotes procurando classes anotadas com
 *                             @Component, @Service, @Repository, @RestController...
 *                             Cada uma dessas vira um "bean" gerenciado pelo Spring.
 *
 * Por isso esta classe fica na RAIZ do pacote: o scan começa a partir daqui.
 */
@SpringBootApplication
public class PedidosApplication {

	// Ponto de entrada da aplicação. SpringApplication.run() sobe o "Application Context"
	// (o container de injeção de dependência), cria todos os beans, faz a auto-configuração
	// e inicia o servidor web embutido (Tomcat) na porta definida no application.yml.
	public static void main(String[] args) {
		SpringApplication.run(PedidosApplication.class, args);
	}

}
