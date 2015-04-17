package com.nortthon.webapp.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import com.nortthon.webapp.interfaces.EnderecoDAO;
import com.nortthon.webapp.model.Cep;
import com.nortthon.webapp.model.Endereco;
import com.nortthon.webapp.model.Resposta;
import com.nortthon.webapp.model.Resposta.StatusResposta;

/**
 * @author Lucas Augusto
 * @since 16/04/2015
 */
public class EnderecoControllerTest {

	@InjectMocks
	private EnderecoController controller;
	@Mock
	private EnderecoDAO enderecoDao;
	@Mock
	private BindingResult result;
	@Mock
	private WebTarget target;
	@Mock
	private Builder builder;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(target.path("buscaDeCep")).thenReturn(target);
		Mockito.when(target.queryParam("cep", "1255000")).thenReturn(target);
		Mockito.when(target.request()).thenReturn(builder);
		
		Mockito.when(enderecoDao.consultar(null)).thenReturn(getListaEndereco(3));
		Mockito.when(enderecoDao.consultar(1)).thenReturn(getListaEndereco(1));
		Mockito.when(enderecoDao.consultar(2)).thenReturn(getListaEndereco(0));
		Mockito.when(enderecoDao.deletar(1)).thenReturn(true);
		Mockito.when(enderecoDao.deletar(2)).thenReturn(false);
		Mockito.when(enderecoDao.atualizar(getEndereco(1))).thenReturn(true);
		Mockito.when(enderecoDao.atualizar(getEndereco(2))).thenReturn(false);
		Mockito.when(enderecoDao.incluir(getEndereco(1))).thenReturn(true);
		Mockito.when(enderecoDao.incluir(getEndereco(2))).thenReturn(false);
	}
	
	@Test
	public void deveRetornarCepInvalidoAoTentarIncluirNovoEndereco() {
		Mockito.when(target.request().get(new GenericType<Resposta<Cep>>(){})).thenReturn(getResposta(StatusResposta.ERRO));
		Resposta<?> resposta = controller.post(getEndereco(1), result);
		
		assertEquals(StatusResposta.ERRO, resposta.getStatus());
		assertEquals("Cep Inválido.", resposta.getMensagem());
	}
	
	@Test
	public void deveIncluirNovoEnderecoComSucesso() {
		Mockito.when(target.request().get(new GenericType<Resposta<Cep>>(){})).thenReturn(getResposta(StatusResposta.OK));
		Resposta<?> resposta = controller.post(getEndereco(1), result);
		
		assertEquals(StatusResposta.OK, resposta.getStatus());
		assertEquals("Endereço incluido com sucesso.", resposta.getMensagem());
	}
	
	@Test
	public void deveRetornarErroAoTentarIncluirNovoEndereco() {
		Mockito.when(target.request().get(new GenericType<Resposta<Cep>>(){})).thenReturn(getResposta(StatusResposta.OK));
		Resposta<?> resposta = controller.post(getEndereco(2), result);
		
		assertEquals(StatusResposta.ERRO, resposta.getStatus());
		assertEquals("Não foi possivel incluir o endereço.", resposta.getMensagem());
	}
	
	@Test
	public void deveRetornarCepInvalidoAoTentarAlterarEndereco() {
		Mockito.when(target.request().get(new GenericType<Resposta<Cep>>(){})).thenReturn(getResposta(StatusResposta.ERRO));
		Resposta<?> resposta = controller.put(getEndereco(1), result);
		
		assertEquals(StatusResposta.ERRO, resposta.getStatus());
		assertEquals("Cep Inválido.", resposta.getMensagem());
	}
	
	@Test
	public void deveAlterarOEnderecoComSucesso() {
		Mockito.when(target.request().get(new GenericType<Resposta<Cep>>(){})).thenReturn(getResposta(StatusResposta.OK));
		Resposta<?> resposta = controller.put(getEndereco(1), result);
		
		assertEquals(StatusResposta.OK, resposta.getStatus());
		assertEquals("Endereço alterado com sucesso.", resposta.getMensagem());
	}
	
	@Test
	public void deveRetornarErroAoTentarAtualizarEndereco() {
		Mockito.when(target.request().get(new GenericType<Resposta<Cep>>(){})).thenReturn(getResposta(StatusResposta.OK));
		Resposta<?> resposta = controller.put(getEndereco(2), result);
		
		assertEquals(StatusResposta.ERRO, resposta.getStatus());
		assertEquals("Não foi possivel atualizar o endereço.", resposta.getMensagem());
	}
	
	@Test
	public void deveExcluirEnderecoComSucesso() {
		Resposta<?> resposta = controller.delete(1);
		
		assertEquals(StatusResposta.OK, resposta.getStatus());
		assertEquals("Endereço deletado com sucesso.", resposta.getMensagem());
	}
	
	@Test
	public void deveRetornarErroAoTentarExcluirEndereco() {
		Resposta<?> resposta = controller.delete(2);
		
		assertEquals(StatusResposta.ERRO, resposta.getStatus());
		assertEquals("Não foi possivel excluir este endereço.", resposta.getMensagem());
	}

	@Test
	public void deveRetornarUmaListaDeEnderecos() {
		Resposta<List<Endereco>> resposta = controller.get(null);
		
		assertEquals(StatusResposta.OK, resposta.getStatus());
		assertEquals(3, resposta.getResultado().size());
	}
	
	@Test
	public void deveRetornarOEnderecoCorrespondenteAoId1() {
		Resposta<List<Endereco>> resposta = controller.get(1);
		
		assertEquals(StatusResposta.OK, resposta.getStatus());
		assertEquals(1, resposta.getResultado().size());
		assertEquals(getEndereco(1), resposta.getResultado().get(0));
	}
	
	@Test
	public void deveRetornarMensagemDeAvidoDeEnderecoNaoEncontrado() {
		Resposta<List<Endereco>> resposta = controller.get(2);
		
		assertEquals(StatusResposta.AVISO, resposta.getStatus());
		assertEquals("Nenhum endereço foi encontrado.", resposta.getMensagem());
		assertNull(resposta.getResultado());
	}
	
	private List<Endereco> getListaEndereco(int qtd) {
		List<Endereco> enderecos = new ArrayList<>();
		
		for (int i = 1; i <= qtd; i++) {
			enderecos.add(getEndereco(qtd));
		}
		
		return enderecos;
	}
	
	private Endereco getEndereco(int id) {
		
		Endereco endereco = new Endereco();
		endereco.setId(id);
		endereco.setBairro("Bairro");
		endereco.setCep("1255000");
		endereco.setCidade("Cidade");
		endereco.setEstado("UF");
		endereco.setNumero("1234");
		endereco.setRua("Rua");
		
		return endereco;
	}

	private Resposta<Cep> getResposta(StatusResposta status) {
		Resposta<Cep> resposta = new Resposta<>();
		resposta.setStatus(status);
		resposta.setMensagem("Cep Inválido.");
		
		return resposta;
	}
}
