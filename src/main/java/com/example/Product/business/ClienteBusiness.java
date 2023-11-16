package com.example.Product.business;

import br.framework.interfaces.IConnection;
import com.example.Product.Errors.InternalServerError;
import com.example.Product.helper.ConnectionManager;
import com.example.Product.model.ClienteDao;
import com.example.Product.model.ClienteDaoImpl;
import com.example.Product.model.dto.ClienteDto;
import com.example.Product.model.entity.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteBusiness {
    private IConnection connection;

    private ClienteDao dao;

    public ClienteBusiness(IConnection connection) {
        this.connection = connection;
        this.dao = new ClienteDaoImpl(connection);
    }

    public static List<ClienteDto> getClientesRequest() {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                ClienteBusiness business = new ClienteBusiness(connection);
                return business.getClientes();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }

    public static ClienteDto getClienteRequest(String id) {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                ClienteBusiness business = new ClienteBusiness(connection);
                Cliente cliente = business.getCliente(id);
                return new ClienteDto(cliente);
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }

    private Cliente getCliente(String id) throws Exception {
        return this.dao.getCliente(Integer.valueOf(id));
    }

    private List<ClienteDto> getClientes() throws Exception {
        List<Cliente> clientes = this.dao.getClientes();
        List<ClienteDto> result = new ArrayList<>();
        clientes.stream().forEach(cliente -> result.add(new ClienteDto(cliente)));
        return result;
    }
}
