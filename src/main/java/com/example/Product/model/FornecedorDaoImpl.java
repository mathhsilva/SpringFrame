package com.example.Product.model;

import br.framework.classes.DataBase.EntityManager;
import br.framework.classes.DataBase.QueryBuilder;
import br.framework.classes.DataBase.Repository;
import br.framework.classes.helpers.Types;
import br.framework.interfaces.IConnection;
import com.example.Product.model.entity.Fornecedor;

import java.util.List;

public class FornecedorDaoImpl extends Repository implements FornecedorDao {

    private final IConnection connection;
    private EntityManager manager;

    public FornecedorDaoImpl(IConnection connection, IConnection connection1) {
        super(connection, Fornecedor.class);
        this.connection = connection1;
    }

    @Override
    public Fornecedor getFornecedorByCnpj(String cnpj) throws Exception {
        QueryBuilder sql = QueryBuilder.create()
                .select("*")
                .from(Fornecedor.class)
                .where("forn_cnpj", "=", cnpj)
                .build();
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

    public void save(Fornecedor fornecedores) throws Exception {
        Fornecedor fornecedoresDb = this.getFornecedor(fornecedores.getForn_id().getValue());
        if (fornecedoresDb != null) {
            fornecedoresDb.setOperation(Types.Operations.Update);
            fornecedoresDb.setCondition("id = " + fornecedores.getForn_id().getValue());
        }
        this.connection.getNewTransaction()
                .addEntity(fornecedores)
                .commit();
    }

    public Fornecedor getFornecedor(Integer id) throws Exception{
        return this.manager.findById(Fornecedor.class, id);
    }
}