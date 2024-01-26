package com.example.Product.model;

import br.framework.classes.DataBase.EntityManager;
import br.framework.interfaces.IConnection;
import br.framework.interfaces.IEntityClass;
import com.example.Product.model.entity.Produto;
import com.example.Product.model.entity.Unidade;

import java.util.List;

public class UnidadeDaoImpl implements UnidadeDao {
    private final IConnection connection;
    private EntityManager manager;
    private IEntityClass unidade;


    public UnidadeDaoImpl(IConnection connection) {
        this.connection = connection;
        this.manager = new EntityManager(connection);
    }

    @Override
    public List<Unidade> getUnidades() throws Exception {
        return this.manager.queryFactory(new Unidade());
    }

    @Override
    public Unidade getUnidade(String cnpj) throws Exception {
        return this.manager.findById(Unidade.class, cnpj );
    }


    @Override
    public List<Produto> getProdutos() throws Exception {
        return null;
    }

    @Override
    public void save(Unidade unidades) {

    }

}
