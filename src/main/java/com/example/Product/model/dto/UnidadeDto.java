package com.example.Product.model.dto;

import com.example.Product.model.entity.Unidade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeDto {
    private String cnpj;
    private String codUnidade;
    private String ibge;
    private String razaoSocial;
    private String codScanntech;

    public UnidadeDto(Unidade unidades){
        this.cnpj = unidades.getCnpj().getValue();
        this.codUnidade = unidades.getCodUnidade().getValue();
        this.ibge = unidades.getIbge().getValue();
        this.razaoSocial = unidades.getRazaoSocial().getValue();
        this.codScanntech = unidades.getCodScanntech().getValue();
    }

    public UnidadeDto(){

    }

    public UnidadeDto(br.framework.classes.model.Unidade unidade) {
    }
}
