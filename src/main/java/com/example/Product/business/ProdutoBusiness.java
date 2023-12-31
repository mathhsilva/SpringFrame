package com.example.Product.business;

import br.framework.interfaces.IConnection;
import com.example.Product.Errors.InternalServerError;
import com.example.Product.helper.ConnectionManager;
import com.example.Product.model.ProdutoDao;
import com.example.Product.model.ProdutoDaoImpl;
import com.example.Product.model.dto.ProdutoDto;
import com.example.Product.model.entity.Produto;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoBusiness {

    private IConnection connection;
    private ProdutoDao dao;

    public ProdutoBusiness(IConnection connection){
        this.connection = connection;
        this.dao = new ProdutoDaoImpl(connection);
    }

    public static ResponseEntity<String> getProdutoRequest() {
        return null;
    }

    public static ProdutoDto criarProduto(ProdutoDto novoProduto) {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                ProdutoBusiness business = new ProdutoBusiness(connection);
                Produto produto = new Produto();
                produto.getId().setValue(novoProduto.getId());
                produto.getName().setValue(novoProduto.getName());
                produto.getPrice().setValue(novoProduto.getPrice());
                novoProduto.setId(produto.getId().getValue());
                business.save(produto);
                return novoProduto;
            } finally {
              connection.close();
            }
        } catch (Exception e) {
             e.printStackTrace();
             throw new InternalServerError(e.getMessage());
        }

    }

    private void save(Produto produto) throws Exception {
        this.dao.save(produto);
    }

    private Produto getProduto(Integer id) throws Exception {
        return this.dao.getProduto(id);
    }

    public static ProdutoDto getProdutoRequest(Integer id) {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                ProdutoBusiness business = new ProdutoBusiness(connection);
                Produto produto = business.getProduto(id);
                ProdutoDto produtoDto = new ProdutoDto(produto);
                return produtoDto;
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<ProdutoDto> getProdutos() throws Exception {
        List<Produto> produtos = this.dao.getProdutos();
        List<ProdutoDto> result = new ArrayList<>();
        produtos.stream().forEach(produto -> result.add(new ProdutoDto(produto)));
        return result;
    }
    public static List<ProdutoDto> getProdutosRequest(){
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                ProdutoBusiness business = new ProdutoBusiness(connection);
                return business.getProdutos();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }


    }
}
