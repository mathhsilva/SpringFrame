package com.example.Product.model;

import com.example.Product.model.dto.ProdutoDto;
import com.example.Product.model.entity.Produto;

import java.util.List;

public interface ProdutoDao {
    List<Produto> getProdutos() throws Exception;
    Produto getProduto(Integer id) throws Exception;
    void save (Produto produto) throws Exception;
    void delete (Produto produto) throws Exception;
    void insert(Produto produto) throws Exception;
}
