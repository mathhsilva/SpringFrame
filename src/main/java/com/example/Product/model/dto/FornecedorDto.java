package com.example.Product.model.dto;

import br.framework.annotations.TableAnnotation;
import br.framework.classes.DataBase.fields.Description;
import br.framework.classes.DataBase.fields.Numeric;
import com.example.Product.model.entity.Fornecedor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorDto {

    private Integer forn_id;

    private String forn_name;

    private Integer forn_cnpj;

    private String forn_razaosocial;

    public FornecedorDto (Fornecedor fornecedor){
        this.forn_id = fornecedor.getForn_id().getValue();
        this.forn_name = fornecedor.getForn_name().getValue();
        this.forn_cnpj = fornecedor.getForn_cnpj().getValue();
        this.forn_razaosocial = fornecedor.getForn_razaosocial().getValue();
    }
}