package com.example.Product.model;

import br.framework.classes.DataBase.EntityManager;
import br.framework.classes.DataBase.Repository;
import br.framework.interfaces.IConnection;
import com.example.Product.model.entity.Cliente;

import java.util.List;

public class ClienteDaoImpl extends Repository implements ClienteDao {

    public ClienteDaoImpl(IConnection connection) {
        super(connection, Cliente.class);
    }

    @Override
    public List<Cliente> getClientes() throws Exception {
        return this.findAll();
    }

    @Override
    public Cliente getCliente(Integer id) throws Exception {
        return this.findById(id);
    }

    public void save (Cliente cliente) throws Exception {
        super.save(cliente);
    }

    public void delete (Cliente cliente) throws Exception {
        super.delete(cliente);
    }

}
