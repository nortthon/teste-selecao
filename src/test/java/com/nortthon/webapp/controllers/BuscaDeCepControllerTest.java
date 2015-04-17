package com.nortthon.webapp.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.nortthon.webapp.interfaces.ConsultaEndereco;
import com.nortthon.webapp.model.Cep;
import com.nortthon.webapp.model.Resposta;
import com.nortthon.webapp.model.Resposta.StatusResposta;

/**
 * @author Lucas Augusto
 * @since 16/04/2015
 */
public class BuscaDeCepControllerTest {

	@Mock
	private ConsultaEndereco consultaEndereco;
	private BuscaDeCepController buscaCep;
	private Cep endereco;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		endereco = new Cep("Nome Rua", "Nome Bairro", "Cidade", "UF");
		Mockito.when(consultaEndereco.consultarEndereco(12345000)).thenReturn(endereco);
		buscaCep = new BuscaDeCepController(consultaEndereco);
	}
	
	@Test
	public void deveRetornarCepInvalido() {
		
		Resposta<Cep> resposta = buscaCep.getCep(99999999);
		
		assertEquals(StatusResposta.ERRO, resposta.getStatus());
		assertEquals("Cep inválido.", resposta.getMensagem());
		assertNull(resposta.getResultado());
	}
	
	@Test
	public void deveRetornarCepValido() {
		Resposta<Cep> resposta = buscaCep.getCep(12345000);
		
		assertEquals(StatusResposta.OK, resposta.getStatus());
		assertNull(resposta.getMensagem());
		assertEquals(endereco, resposta.getResultado());
	}
	
	@Test
	public void deveRetornarEnderecoDoCep12345000APartirDoCep12345999() {
		Resposta<Cep> resposta = buscaCep.getCep(12345999);
		
		assertEquals(StatusResposta.OK, resposta.getStatus());
		assertNull(resposta.getMensagem());
		assertEquals(endereco, resposta.getResultado());
	}
}
