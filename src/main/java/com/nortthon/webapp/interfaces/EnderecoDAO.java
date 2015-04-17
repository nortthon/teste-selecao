package com.nortthon.webapp.interfaces;

import java.util.List;

import com.nortthon.webapp.model.Endereco;

/**
 * Interface criada para representar um DAO que pode ser implementado
 * com a utilização do banco de dados.
 * 
 * @author Lucas Augusto
 * @since 16/04/2015
 */
public interface EnderecoDAO {
	boolean incluir(Endereco endereco);

	boolean atualizar(Endereco endereco);

	boolean deletar(int id);

	List<Endereco> consultar(Integer id);
}
