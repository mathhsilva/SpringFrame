package com.example.Product.model.dto;

import com.example.Product.model.entity.Movimento;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MovimentoDto {
    private int id;
    private String cnpj;
    private String pdv;
    private java.util.Date data_movimento;
    private Date data_servidor;
    private Date hora_servidor;
    private String versao;

    public MovimentoDto(Movimento movimento) {
        this.id = movimento.getId().getValue();
        this.cnpj = movimento.getCnpj().getValue();
        this.pdv = movimento.getPdv().getValue();
        this.data_movimento = movimento.getData_movimento().getValue();
        this.data_servidor = movimento.getData_servidor().getValue();
        this.hora_servidor = movimento.getHora_servidor().getValue();
        this.versao = movimento.getVersao().getValue();
    }
}
