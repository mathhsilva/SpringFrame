package com.example.Product.helper;

import br.framework.classes.DataBase.DataBaseProperties;
import br.framework.classes.helpers.PropertyFile;
import br.framework.classes.helpers.StringList;
import br.framework.classes.helpers.Types;
import com.example.Product.model.Configuration;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private PropertyFile propertyFile;
    private String filePath;

    public ConfigManager() {
        super();
        try {
            this.setFilePath(new File(".").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean hasPWL() {
        Boolean Result;
        File file = new File(this.getFilePath() + "/ERP.pwl");
        Result = file.exists();
        return Result;
    }

    public String tryLoadPWL() {
        Integer[] posicoes = { 138, 40, 193, 9, 155, 33, 98, 47, 120, 26 };
        String Result;
        try {
            StringList Lista = new StringList();
            Lista.loadFromFile(this.getFilePath() + "/ERP.pwl");
            Result = "";
            Integer p;
            for (int i = 0; i < 10; i++) {
                p = posicoes[i] - 1;
                if ((Lista.size() > i) && (Lista.getString(i + 1).length() >= p)) {
                    Result += Lista.getString(i + 1).substring(p, p + 1);
                }
            }
            Result = Result.trim();
        } catch (Exception e) {
            e.printStackTrace();
            Result = "";
        }
        return Result;
    }

    private void loadProperties(DataBaseProperties properties, String dbName) {
        String dataBaseName = System.getenv(dbName.toUpperCase() + "_NAME");
        if (Strings.isNullOrEmpty(dataBaseName)) {
            dataBaseName = this.propertyFile.getProperty(dbName + ".name");
        }
        properties.setDataBaseName(dataBaseName);
        String dataBaseType = System.getenv(dbName.toUpperCase() + "_TYPE");
        if (Strings.isNullOrEmpty(dataBaseType)) {
            dataBaseType = this.propertyFile.getProperty(dbName + ".type");
        }
        Types.DataBase Banco = Types.DataBase.valueOf(dataBaseType);
        String host = System.getenv(dbName.toUpperCase()+ "_HOST");
        if (Strings.isNullOrEmpty(host)) {
            host = this.propertyFile.getProperty(dbName + ".host");
        }
        String user = System.getenv(dbName.toUpperCase()+ "_USER");
        if (Strings.isNullOrEmpty(user)) {
            user = this.propertyFile.getProperty(dbName + ".user");
        }
        String tnsName = System.getenv(dbName.toUpperCase()+ "_TNSNAME");
        if (Strings.isNullOrEmpty(tnsName)) {
            tnsName = this.propertyFile.getProperty(dbName + ".tnsName");
        }
        String password = System.getenv(dbName.toUpperCase()+ "_PASSWORD");
        if (Strings.isNullOrEmpty(password)) {
            password = this.propertyFile.getProperty(dbName + ".password");
        }
        String numConnectionsStr = System.getenv(dbName.toUpperCase()+ "_MAXCONNECTIONS");
        if (Strings.isNullOrEmpty(numConnectionsStr)) {
            numConnectionsStr = this.propertyFile.getProperty(dbName + ".maxConnections");
        }


        Integer numConnections;
        try {
            numConnections = FlexFunctions.inteiro(numConnectionsStr).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            numConnections = 0;
        }
        if (numConnections<5) {
            numConnections = 5;
        }

        properties.setMaxConnections(numConnections);

        if ("ERP".equalsIgnoreCase(user)) {
            password = "erp94587361";
            if (this.hasPWL()) {
                password = this.tryLoadPWL();
            }
        }
        properties.setHostAdress(host);
        properties.setUserName(user);
        properties.setTnsName(tnsName);
        properties.setPassword(password);
        properties.setServerType(Banco);
        if (Banco == Types.DataBase.Postgres) {
            properties.setDriverName("org.postgresql.Driver");
            String portStr = System.getenv(dbName.toUpperCase()+ "_PORT");
            if (portStr == null) {
                portStr = this.propertyFile.getProperty(dbName + ".port");
            }
            properties.setPort(Integer.valueOf(portStr));
        } else if (Banco == Types.DataBase.Oracle) {
            properties.setDriverName("oracle.jdbc.OracleDriver");
            String serviceName = System.getenv(dbName.toUpperCase()+ "_SERVICENAME");
            if (Strings.isNullOrEmpty(serviceName)) {
                serviceName = this.propertyFile.getProperty(dbName + ".serviceName");
            }
            properties.setServiceName(serviceName);
            String portStr = System.getenv(dbName.toUpperCase()+ "_PORT");
            if (portStr == null) {
                portStr = this.propertyFile.getProperty(dbName + ".port");
            }
            try {
                Integer port = FlexFunctions.inteiro(portStr).intValue();
                properties.setPort(port);
            } catch (Exception e) {
                e.printStackTrace();
                properties.setPort(0);
            }
        } else if (Banco == Types.DataBase.SqlServer) {
            properties.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String instance = System.getenv(dbName.toUpperCase()+ "_INSTANCE");
            if (Strings.isNullOrEmpty(instance)) {
                instance = this.propertyFile.getProperty(dbName + ".instance");
            }
            String portStr = System.getenv(dbName.toUpperCase()+ "_PORT");
            if (portStr == null) {
                portStr = this.propertyFile.getProperty(dbName + ".port");
            }
            try {
                Integer port = FlexFunctions.inteiro(portStr).intValue();
                properties.setPort(port);
            } catch (Exception e) {
                e.printStackTrace();
                properties.setPort(0);
            }
            properties.setInstance(instance);
        }
    }

    public void loadConfig(Configuration configuration) throws IOException {
        String path = this.getFilePath() + "/config.properties";
        if (!Strings.isNullOrEmpty(configuration.getPath())) {
            path = configuration.getPath();
        }
        this.propertyFile = new PropertyFile(path);
        File file = new File(path);
        if (file.exists()) {
            this.propertyFile.load();
        }
        DataBaseProperties properties = configuration.getDataBase();

        this.loadConfigTelegram(configuration);
        this.loadConfigLog(configuration);
        this.loadConfigDefault(configuration);
        this.loadConfigPrometheus(configuration);

        this.loadProperties(properties, "db");
        configuration.setDataBase(properties);
        configuration.setIsLoaded(true);
    }

    private void loadConfigPrometheus(Configuration configuration) {
        Boolean prometheus = false;
        try {
            String value = System.getenv("PROMETHEUS");
            if (Strings.isNullOrEmpty(value)) {
                value = this.propertyFile.getProperty("management.metrics.export.prometheus");
            }
            prometheus = "enabled".equalsIgnoreCase(value);
        } catch (Exception e) {
            e.printStackTrace();
            prometheus = false;
        }
        configuration.setPrometheus(prometheus);
    }

    private void loadSSLCertificate(Configuration configuration) {

        try {
            String keyStorePath = System.getenv("APPLICATION_HTTPS_KEYSTORE_PATH");
            if (Strings.isNullOrEmpty(keyStorePath)) {
                keyStorePath = this.propertyFile.getProperty("application.https.keyStore.path");
            }
            configuration.setKeyStorePath(keyStorePath);
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setKeyStorePath(null);
        }
        try {
            String keyStorePass = System.getenv("APPLICATION_HTTPS_KEYSTORE_PASSWORD");
            if (Strings.isNullOrEmpty(keyStorePass)) {
                keyStorePass = this.propertyFile.getProperty("application.https.keyStore.password");
            }
            configuration.setKeyStorePassword(keyStorePass);
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setKeyStorePassword(null);
        }

        try {
            String keyStoreType = System.getenv("APPLICATION_HTTPS_KEYSTORE_TYPE");
            if (Strings.isNullOrEmpty(keyStoreType)) {
                keyStoreType = this.propertyFile.getProperty("application.https.keyStore.type");
            }
            configuration.setKeyStoreType(keyStoreType);
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setKeyStoreType(null);
        }
    }

    private void loadConfigDefault(Configuration configuration) {


        try {
            String gerarArquivo = System.getenv("GERAR_ARQUIVO");
            if (Strings.isNullOrEmpty(gerarArquivo)) {
                gerarArquivo = this.propertyFile.getProperty("gerararquivo");
            }
            configuration.setGerarArquivo(false);
            if (!Strings.isNullOrEmpty(gerarArquivo)) {
                configuration.setGerarArquivo(!"disabled".equalsIgnoreCase(gerarArquivo));
            }
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setGerarArquivo(false);
        }

        try {
            String httpsPort = System.getenv("APPLICATION_HTTPS_PORT");
            if (Strings.isNullOrEmpty(httpsPort)) {
                httpsPort = this.propertyFile.getProperty("application.https.port");
            }
            configuration.setHttps_port(null);
            if (!Strings.isNullOrEmpty(httpsPort)) {
                configuration.setHttps_port(FlexFunctions.inteiro(httpsPort).intValue());
                this.loadSSLCertificate(configuration);
            }
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setHttps_port(null);
        }

        try {
            String httpPort = System.getenv("APPLICATION_HTTP_PORT");
            if (Strings.isNullOrEmpty(httpPort)) {
                httpPort = this.propertyFile.getProperty("application.http.port");
            }
            configuration.setHttp_port(null);
            if (!Strings.isNullOrEmpty(httpPort)) {
                configuration.setHttp_port(FlexFunctions.inteiro(httpPort).intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setHttp_port(null);
        }


        try {
            String compression = System.getenv("APPLICATION_COMPRESSION");
            if (Strings.isNullOrEmpty(compression)) {
                compression = this.propertyFile.getProperty("application.compression");
            }
            configuration.setCompression("enabled".equalsIgnoreCase(compression));
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setCompression(false);
        }

        try {
            String raiseExceptions = System.getenv("APPLICATION_RAISE_EXCEPTIONS");
            if (Strings.isNullOrEmpty(raiseExceptions)) {
                raiseExceptions = this.propertyFile.getProperty("application.raiseExceptions");
            }
            configuration.setRaiseException("enabled".equalsIgnoreCase(raiseExceptions));
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setRaiseException(false);
        }
    }

    private void loadConfigLog(Configuration configuration) {
        try {
            String debug = System.getenv("APPLICATION_DEBUG");
            if (Strings.isNullOrEmpty(debug)) {
                debug = this.propertyFile.getProperty("application.debug");
            }
            configuration.setDebug("enabled".equalsIgnoreCase(debug));
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setDebug(false);
        }

        try {
            String debug = System.getenv("APPLICATION_DEBUG_CONNECTIONS");
            if (Strings.isNullOrEmpty(debug)) {
                debug = this.propertyFile.getProperty("application.debugConnections");
            }
            configuration.setDebugConnections("enabled".equalsIgnoreCase(debug));
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setDebugConnections(false);
        }

        try {

            String log = System.getenv("APPLICATION_LOG");
            if (Strings.isNullOrEmpty(log)) {
                log = this.propertyFile.getProperty("application.log");
            }
            configuration.setLog("enabled".equalsIgnoreCase(log));
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setLog(false);
        }
    }


    public void loadConfigTelegram(Configuration configuration) {
        try {
            String propName = "application.telegram.bot.token";
            String telegramBotToken = System.getenv(propName.toUpperCase().replace(".","_"));
            if (Strings.isNullOrEmpty(telegramBotToken)) {
                telegramBotToken = this.propertyFile.getProperty(propName);
            }
            configuration.setTelegramBotToken(telegramBotToken);
        } catch (Exception e) {
            e.printStackTrace();
            configuration.setTelegramBotToken(null);
        }
    }

    public void saveConfig(Configuration configuration) throws IOException {
        this.propertyFile.setProperty("db.name", configuration.getDataBase().getDataBaseName());
        this.propertyFile.setProperty("db.type", configuration.getDataBase().getServerType().toString());
        this.propertyFile.setProperty("db.host", configuration.getDataBase().getHostAdress());
        this.propertyFile.setProperty("db.user", configuration.getDataBase().getUserName());
        this.propertyFile.setProperty("db.serviceName", configuration.getDataBase().getServiceName());
        this.propertyFile.setProperty("db.password", configuration.getDataBase().getPassword());
        this.propertyFile.setProperty("db.port", configuration.getDataBase().getPort().toString());
        this.propertyFile.setProperty("db.instance", configuration.getDataBase().getInstance());
        this.propertyFile.setProperty("db.maxConnections", configuration.getDataBase().getMaxConnections().toString());
        this.propertyFile.save();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
