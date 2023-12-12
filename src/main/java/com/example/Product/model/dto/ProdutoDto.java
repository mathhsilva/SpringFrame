package com.example.Product.model.dto;

import br.framework.classes.helpers.Types;
import com.example.Product.model.entity.Produto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProdutoDto {
    private Integer id;

    private String name;

    private Double price;

    public ProdutoDto(Produto produto) {
        this.id = produto.getId().getValue();
        this.name = produto.getName().getValue();
        this.price = produto.getPrice().getValue();
    }
}
