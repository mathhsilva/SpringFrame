package com.example.Product.model;

import com.example.Product.model.dto.MovimentoDto;
import com.example.Product.model.entity.Movimento;

import java.util.List;

public interface MovimentoDao {
    List<Movimento> getMovimentos() throws Exception;

}
