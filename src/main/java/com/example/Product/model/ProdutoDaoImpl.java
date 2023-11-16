package com.example.Product.model;

import br.framework.classes.DataBase.EntityClass;
import br.framework.classes.DataBase.EntityManager;
import br.framework.classes.DataBase.QueryBuilder;
import br.framework.classes.DataBase.Transaction;
import br.framework.classes.helpers.Types;
import br.framework.interfaces.IConnection;
import com.example.Product.model.dto.ProdutoDto;
import com.example.Product.model.entity.Produto;

import java.util.List;

public class ProdutoDaoImpl implements ProdutoDao{

    private final IConnection connection;
    private EntityManager manager;

    public ProdutoDaoImpl(IConnection connection){
        this.connection = connection;
        this.manager = new EntityManager(connection);
    }
    @Override
    public List<Produto> getProdutos() throws Exception {
        return this.manager.queryFactory(new Produto());
    }

    @Override
    public Produto getProduto(String id) throws Exception {
        return this.manager.findById(Produto.class, id);
    }

    @Override
    public void save(Produto produto) throws Exception {
        Produto produtoDb = this.getProduto(String.valueOf(produto.getId().getValue()));
        if (produtoDb != null) {
            produtoDb.setOperation(Types.Operations.Update);
            produtoDb.setCondition("id = " + produto.getId().getValue());
        }
        this.connection.getNewTransaction()
                       .addEntity(produto)
                       .commit();
    }

    @Override
    public void delete(Produto produto) throws Exception {
        produto.setOperation(Types.Operations.Delete);
        produto.setCondition("id = " + produto.getId().getValue());
        this.connection.getNewTransaction()
                       .addEntity(produto)
                       .commit();
    }

    @Override
    public Produto criarProduto(ProdutoDto novoProduto) throws Exception {
        return null;

    }
}
