package com.example.Product.model.entity;

import br.framework.annotations.GeneratedValue;
import br.framework.annotations.ID;
import br.framework.annotations.TableAnnotation;
import br.framework.classes.DataBase.EntityClass;
import br.framework.classes.DataBase.fields.Description;
import br.framework.classes.DataBase.fields.FloatField;
import br.framework.classes.DataBase.fields.Numeric;
import com.example.Product.model.dto.ProdutoDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Getter
@Setter
@TableAnnotation(tableName = "tb_product", prefix = "")
public class Produto extends EntityClass {

    @ID(index = 0) @GeneratedValue (name = "tb_product_id_seq")
    private Numeric id;

    private Description name;

    private FloatField price;

    public Produto(String condition) {
        super(condition);
    }

    public Produto() {
        super();
    }

    @Override
    public void setupFields() {
    }
}
