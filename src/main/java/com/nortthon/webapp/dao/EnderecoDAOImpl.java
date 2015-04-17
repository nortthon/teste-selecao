package com.nortthon.webapp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.nortthon.webapp.interfaces.EnderecoDAO;
import com.nortthon.webapp.model.Endereco;

/**
 * Simulação da implementação de um DAO que gerencia o (CRUD) do endereço.
 * <p>
 * Os dados nessa implementação são gravados e gerenciados na sessão.
 * Uma implementação bem simples somente para atender as exigencias da
 * implementação do CRUD.
 * 
 * @deprecated Classe utilizada somente para simular um DAO.
 */
@Component
public class EnderecoDAOImpl implements EnderecoDAO {
	
	private Properties properties;
	private int index = 1;
	
	public EnderecoDAOImpl() {		
		properties = new Properties();			
	}
	
	@Override
	public boolean incluir(Endereco endereco) {
		endereco.setId(index);
		properties.put(index, endereco);
		index++;
		return true;
	}
	
	@Override
	public boolean deletar(int id) {		
		if (properties != null && properties.containsKey(id)) {
			properties.remove(Integer.valueOf(id));
			return true;
		}
		return false;
	}
	
	@Override
	public List<Endereco> consultar(Integer id) {
		List<Endereco> enderecos = new ArrayList<>();
		if (properties != null) {
			if (null!=id){
				Endereco endereco = (Endereco)properties.get(id.intValue());
				if (endereco != null) {
					enderecos.add(endereco);
				}
			} else {
				for(Entry<Object, Object> e : properties.entrySet()) {
					enderecos.add((Endereco)e.getValue());
		        }				
			}			
		} 
		return enderecos;				
	}
	
	@Override
	public boolean atualizar(Endereco endereco) {		
		if (properties != null && properties.containsKey(endereco.getId())) {
			properties.put(endereco.getId(), endereco);
			return true;
		}
		return false;
	}

}
