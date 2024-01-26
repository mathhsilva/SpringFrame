package com.example.Product.model.entity;

import br.framework.annotations.TableAnnotation;
import br.framework.classes.DataBase.EntityClass;
import br.framework.classes.DataBase.fields.DateField;
import br.framework.classes.DataBase.fields.DateTime;
import br.framework.classes.DataBase.fields.Description;
import br.framework.classes.DataBase.fields.Numeric;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableAnnotation(tableName = "movimentos", prefix = "mov_")
public class Movimento extends EntityClass {

    private Numeric id;
    private Description cnpj;
    private Description pdv;
    private DateField data_movimento;
    private DateField data_servidor;
    private DateTime hora_servidor;
    private Description versao;

    public Movimento(Numeric id, Description cnpj, Description pdv, DateField data_movimento, DateField data_servidor, DateTime hora_servidor) {
        this.id = id;
        this.cnpj = cnpj;
        this.pdv = pdv;
        this.data_movimento = data_movimento;
        this.data_servidor = data_servidor;
        this.hora_servidor = hora_servidor;
        this.versao = versao;
    }

    public Movimento(String condition, Numeric id, Description cnpj, Description pdv, DateField data_movimento, DateField data_servidor, DateTime hora_servidor) {
        super(condition);
        this.id = id;
        this.cnpj = cnpj;
        this.pdv = pdv;
        this.data_movimento = data_movimento;
        this.data_servidor = data_servidor;
        this.hora_servidor = hora_servidor;
        this.versao = versao;
    }

    public Movimento() {

    }
}
