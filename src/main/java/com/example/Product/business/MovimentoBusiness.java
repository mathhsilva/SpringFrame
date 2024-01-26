package com.example.Product.business;

import br.framework.interfaces.IConnection;
import com.example.Product.model.MovimentoDao;
import com.example.Product.helper.ConnectionManager;
import com.example.Product.model.MovimentoDaoImpl;
import com.example.Product.model.dto.MovimentoDto;
import com.example.Product.model.dto.UnidadeDto;
import com.example.Product.model.entity.Movimento;

import java.util.*;
import java.sql.SQLException;

public class MovimentoBusiness {

    private IConnection connection;
    private MovimentoDao dao;

    public MovimentoBusiness(IConnection connection) {
        this.connection = connection;
        this.dao = new MovimentoDaoImpl(connection);
    }


    public static List<MovimentoDto> getMovimentosRequest() {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                MovimentoBusiness business = new MovimentoBusiness(connection);
                return business.getMovimentos();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MovimentoDto> getMovimentos() throws Exception {
        List<Movimento> movimentos = this.dao.getMovimentos();
        List<MovimentoDto> result = new ArrayList<MovimentoDto>();
        movimentos.forEach(movimento -> result.add(new MovimentoDto(movimento)));
        return result;
    }
}