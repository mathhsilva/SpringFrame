package com.example.Product.model.entity;

import br.framework.annotations.ID;
import br.framework.annotations.TableAnnotation;
import br.framework.classes.DataBase.EntityClass;
import br.framework.classes.DataBase.fields.Description;
import br.framework.classes.DataBase.fields.Numeric;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableAnnotation(tableName = "tb_fornecedores", prefix = "")
public class Fornecedor extends EntityClass {

    @ID(index = 0)
    private Numeric forn_id;

    private Description forn_name;

    private Numeric forn_cnpj;

    private Description forn_razaosocial;

    public Fornecedor(String condition){
        super (condition);
    }
    public Fornecedor(){

        super ();
    }
    @Override
    public void setupFields() {

    }
}
