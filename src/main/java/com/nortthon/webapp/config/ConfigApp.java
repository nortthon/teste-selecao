package com.nortthon.webapp.config;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.nortthon.webapp.interfaces.ConsultaEndereco;
import com.nortthon.webapp.model.Cep;

/**
 * @author Lucas Augusto
 * @since 15/04/2015
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "com.nortthon.webapp" })
public class ConfigApp extends WebMvcConfigurerAdapter {

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * Bean criado para simular a injeção de dependência do serviço de busca
	 * endereço pelo cep.
	 * 
	 * @return Retorna o endereço correspondente ao cep ou {@code null} se não
	 *         encontrar.
	 */
	@Bean
	public ConsultaEndereco simulacaoConsultaEndereco() {
		return new ConsultaEndereco() {
			public Cep consultarEndereco(int cep) {
				Map<Integer, Cep> enderecos = new HashMap<Integer, Cep>();
				enderecos.put(1255000, new Cep("Av. Dr. Arnaldo", "Sumaré", "São Paulo", "SP"));
				enderecos.put(51030000, new Cep("Boa Viagem", "Boa Viagem", "Recife", "PE"));
				enderecos.put(85851000, new Cep("Av. Brasil", "Centro", "São Paulo", "SP"));
				enderecos.put(13590910, new Cep("José Felipe Elias", "Jardim do Lago", "Descalvado", "SP"));

				return enderecos.get(cep);
			}
		};
	}
	
	/**
	 * Bean criado para simular uma conexão com o Rest service da buscaDeCep.
	 * <p>
	 * Está apontando para o mesmo projeto, para não ter que fazer dois 
	 * projetos, já que estamos só simulando.
	 * <p>
	 * Se seu servidor estiver subindo em uma porta diferente da 8080, deve
	 * mudar essa informação aqui neste método também.
	 * 
	 * @return Retorna instância para a interface {@code WebTarget} apontando 
	 * 		   para o mesmo projeto.
	 */
	@Bean
	public WebTarget webTarget() {
		Client client = ClientBuilder.newBuilder().build();
        return client.target("http://localhost:8080/teste-selecao");
	}
}
