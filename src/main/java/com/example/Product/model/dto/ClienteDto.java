package com.example.Product.model.dto;

import com.example.Product.model.entity.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDto {
    private Integer clie_id;

    private String clie_nome;

    private String clie_email;

    private String clie_telefone;


    public ClienteDto(Cliente cliente) {
        this.clie_id = cliente.getClie_id().getValue();
        this.clie_nome = cliente.getClie_nome().getValue();
        this.clie_email = cliente.getClie_email().getValue();
        this.clie_telefone = cliente.getClie_telefone().getValue();
    }
}