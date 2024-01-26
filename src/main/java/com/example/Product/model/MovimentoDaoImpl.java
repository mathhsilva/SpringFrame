package com.example.Product.model;

import br.framework.classes.DataBase.EntityManager;
import br.framework.interfaces.IConnection;
import br.framework.interfaces.IEntityClass;
import com.example.Product.model.entity.Movimento;
import com.example.Product.model.entity.Produto;

import java.util.List;


public class MovimentoDaoImpl implements MovimentoDao {
    private final IConnection connection;
    private EntityManager manager;
    private IEntityClass movimento;

    public MovimentoDaoImpl(IConnection connection) {
        this.connection = connection;
        this.manager = new EntityManager(connection);
    }

    @Override
    public List<Movimento> getMovimentos() throws Exception {
        return this.manager.queryFactory(new Movimento());
    }
}
