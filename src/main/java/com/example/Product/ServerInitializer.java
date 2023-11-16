package com.example.Product;

import br.framework.classes.helpers.Log;
import br.framework.classes.helpers.StringList;
import br.framework.interfaces.IConnection;
import com.example.Product.helper.ConnectionManager;
import com.example.Product.model.Configuration;
import com.google.common.io.Files;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ServerInitializer implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        this.init();
    }

    private void startLogger() throws IOException {
        Log.build("produtosws");
        Log.setPath(new File(".").getCanonicalPath() );
        Log.setOutType(Log.OutType.SYSTEM_OUT);
        File fileIO = new File(Log.getPath()+ "\\logs\\log.txt");
        Files.createParentDirs(fileIO);
        if (!Log.isRunning()) {
            Log.start();
        }
    }

    private void buildConfiguration() {
        Log.info("Carregando configurações");
        Configuration configuration = Configuration.getInstance();
        Log.info("Configurações carregadas");
        Log.info("Versão: " + configuration.getVersion());
        this.logConfig();
    }

    private void logConfig() {
        Configuration configuration = Configuration.getInstance();
        Log.info("db.type: " + configuration.getDataBase().getServerType().name());
        Log.info("db.host: " + configuration.getDataBase().getHostAdress());
        Log.info("db.port: " + configuration.getDataBase().getPort());
        Log.info("db.name: " + configuration.getDataBase().getDataBaseName());
        Log.info("db.user: " + configuration.getDataBase().getUserName());
        Log.info("db.tnsName: " + configuration.getDataBase().getTnsName());
        Log.info("db.serviceName: " + configuration.getDataBase().getServiceName());
        Log.info("db.instance: " + configuration.getDataBase().getInstance());
        Log.info("db.maxConnections: " + configuration.getDataBase().getMaxConnections());
    }

    private void logParalelismConfig() {
        Configuration configuration = Configuration.getInstance();
        Integer cores = Runtime.getRuntime().availableProcessors();
        configuration.setNumOfCores(cores);

        Log.info("Máximo de conexões: " + configuration.getDataBase().getMaxConnections().toString());
        Log.info("Núcleos lógicos: " + cores.toString());

    }

    private void logInfos() {

    }

    private void buildConnections() {
        Log.info("Criando pool de conexões");
        ConnectionManager.build();
        Log.info("Pool de conexões inicializado");
    }

    private void init() throws IOException {
        Log.info("Inicializando Aplicação");
        this.startLogger();
        this.buildConfiguration();
        this.logParalelismConfig();
        this.logInfos();
        this.buildConnections();
        this.onStart();
        Log.info("Aplicação inicializada");
    }

    private void onStart() {
        try {
            IConnection connection = ConnectionManager.newConnection();
            try {
                ResultSet q = connection.queryFactory("select current_setting('transaction_isolation') as isolation");
                if (q.next()) {
                    Log.info("Transaction Isolation: " + q.getString("isolation"));
                } else {
                    Log.info("Erro ao ler query");
                }
                q.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        ConnectionManager.ClearInstance();
        if (Log.isRunning()) {
            Log.info("Aplicação finalizada");
            Log.stop();
        }
    }
}
