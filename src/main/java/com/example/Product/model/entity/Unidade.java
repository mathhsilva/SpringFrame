package com.example.Product.model.entity;

import br.framework.annotations.ID;
import br.framework.annotations.TableAnnotation;
import br.framework.classes.DataBase.EntityClass;
import br.framework.classes.DataBase.fields.Description;
import br.framework.classes.DataBase.fields.Numeric;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@TableAnnotation(tableName = "unidades", prefix = "unid_")
public class Unidade extends EntityClass {

    private Description cnpj;
    private Description codUnidade;
    private Description ibge;
    private Description razaoSocial;
    private Description codScanntech;

    public Unidade(String condition){
        super(condition);
    }

    public Unidade(){
        super();
    }

    @Override
    public void setupFields() {
    }
}
