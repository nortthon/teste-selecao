package com.nortthon.webapp.model;

import java.io.Serializable;

/**
 * @author Lucas Augusto
 * @since 15/04/2015
 */
public class Resposta <T> implements Serializable{
	
	private static final long serialVersionUID = -3354359245609841451L;
	private StatusResposta status;
	private String mensagem;
	private T resultado;
	
	public StatusResposta getStatus() {
		return status;
	}
	public void setStatus(StatusResposta status) {
		this.status = status;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public T getResultado() {
		return resultado;
	}
	public void setResultado(T resultado) {
		this.resultado = resultado;
	}

	public enum StatusResposta {
		OK, ERRO, AVISO;
	}
}
