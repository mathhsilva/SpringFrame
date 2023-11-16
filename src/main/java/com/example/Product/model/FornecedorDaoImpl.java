package com.example.Product.model;

import br.framework.classes.DataBase.EntityManager;
import br.framework.classes.DataBase.QueryBuilder;
import br.framework.classes.DataBase.Repository;
import br.framework.interfaces.IConnection;
import ch.qos.logback.core.util.DelayStrategy;
import com.example.Product.model.entity.Fornecedor;

import java.util.ArrayList;
import java.util.List;

public class FornecedorDaoImpl extends Repository implements FornecedorDao {

    public FornecedorDaoImpl(IConnection connection) {
        super(connection, Fornecedor.class);
    }

    @Override
    public Fornecedor getFornecedorByCnpj(String cnpj) throws Exception {
        QueryBuilder sql = QueryBuilder.create()
                .select("*")
                .from(Fornecedor.class)
                .where("forn_cnpj = @param@")
                .build()
                .setString(0, cnpj);
        List<Fornecedor> fornecedores = this.getManager().queryFactory(sql, Fornecedor.class);
        if (!fornecedores.isEmpty()) {
            return fornecedores.get(0);
        }
        return null;
    }

    @Override
    public Fornecedor getFornecedor() {
        return null;
    }
}