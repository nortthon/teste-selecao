package com.nortthon.webapp.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nortthon.webapp.interfaces.EnderecoDAO;
import com.nortthon.webapp.model.Cep;
import com.nortthon.webapp.model.Endereco;
import com.nortthon.webapp.model.Resposta;
import com.nortthon.webapp.model.Resposta.StatusResposta;

/**
 * @author Lucas Augusto
 * @since 16/04/2015
 */
@RestController
@RequestMapping("endereco")
public class EnderecoController {

	@Autowired
	private WebTarget target;

	@Autowired
	private EnderecoDAO enderecoDao;

	/**
	 * Recebe um json no formato abaixo para incluir na base de dados.
	 * 
	 * <pre class="code">
	 * {
	 * 	"rua" : "XXXX",
	 * 	"numero" : "9999",
	 * 	"cep" : "99999999",
	 * 	"cidade" : "XXXXX",
	 * 	"estado" : "XX",
	 * 	"bairro" : "XXXXXX",
	 * 	"complemento" : "X"
	 * }
	 * </pre>
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Resposta<?> post(@RequestBody @Valid Endereco endereco,
			BindingResult result) {
		Resposta<?> resposta = new Resposta<>();

		if (result.hasErrors()) {
			resposta.setStatus(StatusResposta.ERRO);
			resposta.setMensagem("Os campos rua, número, cep, cidade e estado são obrigatórios.");
			return resposta;
		}

		Resposta<Cep> response = target.path("buscaDeCep")
				.queryParam("cep", endereco.getCep()).request()
				.get(new GenericType<Resposta<Cep>>() {
				});

		if (StatusResposta.OK.equals(response.getStatus())) {
			if (enderecoDao.incluir(endereco)) {
				resposta.setStatus(StatusResposta.OK);
				resposta.setMensagem("Endereço incluido com sucesso.");
			} else {
				resposta.setStatus(StatusResposta.ERRO);
				resposta.setMensagem("Não foi possivel incluir o endereço.");
			}
		} else {
			resposta.setStatus(StatusResposta.ERRO);
			resposta.setMensagem(response.getMensagem());
		}

		return resposta;
	}

	/**
	 * Recebe um json no formato abaixo para alteração na base de dados. É
	 * necessário enviar o campo {@code id} para que alteração seja feita com
	 * sucesso.
	 * 
	 * <pre class="code">
	 * {
	 *  "id" : "9",
	 * 	"rua" : "XXXX",
	 * 	"numero" : "9999",
	 * 	"cep" : "99999999",
	 * 	"cidade" : "XXXXX",
	 * 	"estado" : "XX",
	 * 	"bairro" : "XXXXXX",
	 * 	"complemento" : "X"
	 * }
	 * </pre>
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public Resposta<?> put(@RequestBody @Valid Endereco endereco,
			BindingResult result) {
		Resposta<?> resposta = new Resposta<>();

		if (result.hasErrors()) {
			resposta.setStatus(StatusResposta.ERRO);
			resposta.setMensagem("Os campos rua, número, cep, cidade e estado são obrigatórios.");
			return resposta;
		}

		Resposta<Cep> response = target.path("buscaDeCep")
				.queryParam("cep", endereco.getCep()).request()
				.get(new GenericType<Resposta<Cep>>() {
				});

		if (StatusResposta.OK.equals(response.getStatus())) {
			if (enderecoDao.atualizar(endereco)) {
				resposta.setStatus(StatusResposta.OK);
				resposta.setMensagem("Endereço alterado com sucesso.");
			} else {
				resposta.setStatus(StatusResposta.ERRO);
				resposta.setMensagem("Não foi possivel atualizar o endereço.");
			}
		} else {
			resposta.setStatus(StatusResposta.ERRO);
			resposta.setMensagem(response.getMensagem());
		}

		return resposta;
	}

	/**
	 * Faz a exclusão do endereço com base no parametro id.
	 * 
	 * @param id
	 *            Id do endereço que deseja excluir.
	 * @return Retorna status OK se a exclusão ocorrer com sucesso. Caso
	 *         contrário retorna status=ERRO.
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public Resposta<?> delete(@RequestParam(required = true) int id) {
		Resposta<List<Endereco>> resposta = new Resposta<>();

		if (enderecoDao.deletar(id)) {
			resposta.setStatus(StatusResposta.OK);
			resposta.setMensagem("Endereço deletado com sucesso.");
		} else {
			resposta.setStatus(StatusResposta.ERRO);
			resposta.setMensagem("Não foi possivel excluir este endereço.");
		}

		return resposta;
	}

	/**
	 * Realiza consulta de endereço com base no id registrado na base. Se o id
	 * não for passado como parametro, retorna todos os enderecos registrados na
	 * base.
	 * 
	 * @param id
	 *            Id do endereço que deseja consultar
	 * @return Retorna o endereço se encontrar, senão retorna vazio.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Resposta<List<Endereco>> get(
			@RequestParam(required = false) Integer id) {
		Resposta<List<Endereco>> resposta = new Resposta<>();

		List<Endereco> enderecos = enderecoDao.consultar(id);

		if (enderecos.isEmpty()) {
			resposta.setStatus(StatusResposta.AVISO);
			resposta.setMensagem("Nenhum endereço foi encontrado.");
		} else {
			resposta.setStatus(StatusResposta.OK);
			resposta.setResultado(enderecos);
		}

		return resposta;
	}
}