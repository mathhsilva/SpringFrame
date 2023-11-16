package com.example.Product.model;

import br.framework.classes.DataBase.IRepository;
import com.example.Product.model.entity.Fornecedor;

import java.util.List;

public interface FornecedorDao extends IRepository {
    Fornecedor getFornecedorByCnpj(String cnpj) throws Exception;

    Fornecedor getFornecedor();
}
