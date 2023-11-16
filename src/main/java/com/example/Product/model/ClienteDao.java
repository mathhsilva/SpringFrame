package com.example.Product.model;

import com.example.Product.model.entity.Cliente;
import com.example.Product.model.entity.Fornecedor;

import java.util.List;

public interface ClienteDao {

    List<Cliente> getClientes() throws Exception;

    Cliente getCliente(Integer id) throws Exception;

    void save (Cliente cliente) throws Exception;

    void delete (Cliente cliente) throws Exception;
}
