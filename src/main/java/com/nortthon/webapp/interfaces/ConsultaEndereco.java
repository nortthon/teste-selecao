package com.nortthon.webapp.interfaces;

import com.nortthon.webapp.model.Cep;

/**
 * Interface representando um serviço já existente de Busca por endereço a
 * partir de um determinado cep.
 * 
 * @author Lucas Augusto
 * @since 15/04/2015
 */
public interface ConsultaEndereco {
	Cep consultarEndereco(int cep);
}
