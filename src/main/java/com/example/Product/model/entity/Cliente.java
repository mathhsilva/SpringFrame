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
@TableAnnotation(tableName = "tb_clientes", prefix = "")
public class Cliente extends EntityClass {
    @ID(index = 0)
    private Numeric clie_id;

    private Description clie_nome;

    private Description clie_email;

    private Description clie_telefone;
}
