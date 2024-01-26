package com.example.Product.business;

import br.framework.interfaces.IConnection;
import com.example.Product.Errors.InternalServerError;
import com.example.Product.helper.ConnectionManager;
import com.example.Product.model.UnidadeDao;
import com.example.Product.model.UnidadeDaoImpl;
import com.example.Product.model.dto.UnidadeDto;
import com.example.Product.model.entity.Unidade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnidadeBusiness {
    private IConnection connection;
    private UnidadeDao dao;

    public static List<UnidadeDto> getUnidadesRequest() {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                UnidadeBusiness business = new UnidadeBusiness(connection);
                return business.getUnidades();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UnidadeDto getUnidadeRequest(String cnpj) {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                UnidadeBusiness business = new UnidadeBusiness(connection);
                Unidade unidade = business.getUnidade(String.valueOf(cnpj));
                UnidadeDto unidadeDto = new UnidadeDto(unidade);
                return unidadeDto;
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }

    private Unidade getUnidade(String cnpj) throws Exception {
        return this.dao.getUnidade(cnpj);
    }

    private List<UnidadeDto> getUnidades() throws Exception {
        List<Unidade> unidades = this.dao.getUnidades();
        List<UnidadeDto> result = new ArrayList<UnidadeDto>();
        unidades.forEach(unidade -> result.add(new UnidadeDto(unidade)));
        return result;
    }

    private void save(Unidade unidades) throws Exception {
        this.dao.save(unidades);
    }

    public UnidadeBusiness(IConnection connection){
        this.connection = connection;
        this.dao = new UnidadeDaoImpl(connection);
    }

    public static UnidadeDto criarUnidade(UnidadeDto novaUnidade){
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                UnidadeBusiness business = new UnidadeBusiness(connection);
                Unidade unidades = new Unidade();

                unidades.getCnpj().setValue(novaUnidade.getCnpj());
                unidades.getCodUnidade().setValue(novaUnidade.getCodUnidade());
                unidades.getIbge().setValue(novaUnidade.getIbge());
                unidades.getRazaoSocial().setValue(novaUnidade.getRazaoSocial());
                unidades.getCodScanntech().setValue(novaUnidade.getCodScanntech());
                business.save(unidades);
                return novaUnidade;
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }
}