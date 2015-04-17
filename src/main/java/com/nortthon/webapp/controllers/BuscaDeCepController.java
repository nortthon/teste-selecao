package com.nortthon.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nortthon.webapp.interfaces.ConsultaEndereco;
import com.nortthon.webapp.model.Cep;
import com.nortthon.webapp.model.Resposta;
import com.nortthon.webapp.model.Resposta.StatusResposta;

/**
 * @author Lucas Augusto
 * @since 15/04/2015
 */
@RestController
@RequestMapping("/buscaDeCep")
public class BuscaDeCepController {

	private ConsultaEndereco consultaEndereco;

	@Autowired
	public BuscaDeCepController(ConsultaEndereco consultaEndereco) {
		this.consultaEndereco = consultaEndereco;
	}

	/**
	 * Realiza consulta por cep e retorna o endereço correspondente.
	 * <p>
	 * Se o endereço para o cep desejado não for encontrado, substitui o digito
	 * da direita para a esquerda por 0 até que o endereço seja localizado. Ex:
	 * <i>Dado 22333999, também faz tentativas com 22333990, 22333900, e assim
	 * por diante</i>. <br>
	 * Se o {@code cep} não for encontrado, retorna "Cep Inválido".
	 * 
	 * @param cep
	 *            Número do cep que deseja buscar o endereço.
	 * @return Retorna {@code status=OK} se o endereço for encontrado, senão,
	 *         retorna {@code status=ERRO} + {@code mensagem} se não encontrado
	 *         ou cep for inválido.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Resposta<Cep> getCep(@RequestParam int cep) {

		Cep endereco = null;
		int cpfBusca = cep;
		int mod = 10;

		while (cpfBusca > 0 && endereco == null) {
			endereco = consultaEndereco.consultarEndereco(cpfBusca);
			if (endereco == null) {
				cpfBusca = cep - (cep % mod);
				mod *= 10;
			}
		}

		Resposta<Cep> resposta = new Resposta<>();
		if (endereco == null) {
			resposta.setStatus(StatusResposta.ERRO);
			resposta.setMensagem("Cep inválido.");
		} else {
			resposta.setStatus(StatusResposta.OK);
			resposta.setResultado(endereco);
		}

		return resposta;
	}
}
