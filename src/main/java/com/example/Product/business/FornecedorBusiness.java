package com.example.Product.business;

import br.framework.interfaces.IConnection;
import com.example.Product.Errors.InternalServerError;
import com.example.Product.helper.ConnectionManager;
import com.example.Product.model.FornecedorDao;
import com.example.Product.model.FornecedorDaoImpl;
import com.example.Product.model.dto.FornecedorDto;
import com.example.Product.model.entity.Fornecedor;

import java.util.ArrayList;
import java.util.List;

public class FornecedorBusiness {

    private IConnection connection;

    private FornecedorDao dao;

    public FornecedorBusiness(IConnection connection) {
        this.connection = connection;
        this.dao = new FornecedorDaoImpl(connection);
    }

    public static List<FornecedorDto> getFornecedoresRequest() {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                FornecedorBusiness business = new FornecedorBusiness(connection);
                return business.getFornecedores();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<FornecedorDto> getFornecedores() throws Exception {
        List<Fornecedor> fornecedores = this.dao.findAll();
        List<FornecedorDto> result = new ArrayList<>();
        fornecedores.stream().forEach(fornecedor -> result.add(new FornecedorDto(fornecedor)));
        return result;
    }

    private Fornecedor getFornecedor(Integer id) throws Exception {
        return this.dao.findById(id);
    }

    public static FornecedorDto getFornecedorRequest(Integer id) {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                FornecedorBusiness business = new FornecedorBusiness(connection);
                Fornecedor fornecedor = business.getFornecedor(id);
                return new FornecedorDto(fornecedor);
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }
}
