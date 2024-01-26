package com.example.Product.model;

import com.example.Product.model.entity.Produto;
import com.example.Product.model.entity.Unidade;

import java.util.*;

public interface UnidadeDao {

    List<Unidade> getUnidades() throws Exception;

    Unidade getUnidade(String cnpj) throws Exception;

    List<Produto> getProdutos() throws Exception;

    void save(Unidade unidades);
}
