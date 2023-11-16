package com.example.Product.model;

import br.framework.classes.DataBase.DataBaseProperties;
import com.example.Product.helper.ConfigManager;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class Configuration {
    private DataBaseProperties dataBase;
    private String path;
    private String secretkey = "7f-j&CKk=coNzZc0y7_4obMP?#TfcYs)(*323476262#$####$!@@&¨$6";
    private static Configuration instance;
    private Integer http_port;
    private Integer https_port;
    private Boolean compression;
    private boolean debugConnections;
    private boolean debug;
    private String version = "2.0.0.05";
    private boolean loaded;
    private Boolean prometheus;
    private boolean raiseException;
    private boolean log;
    private String telegramBotToken;
    private Integer numOfCores;
    private Instant startTime = new Date().toInstant();
    private boolean gerarArquivo;
    private String keyStorePath;
    private String keyStorePassword;
    private String keyStoreType;

    public Configuration() {
        this.dataBase = new DataBaseProperties("","","","");
        this.dataBase.setMaxConnections(20);
        this.debugConnections = false;
        this.debug = false;
        this.path = null;
        this.http_port = null;
        this.https_port = null;
        this.compression = false;
        this.setIsLoaded(false);
    }

    public synchronized static Configuration getInstance() {
        Configuration.build();
        return Configuration.instance;
    }

    public synchronized static Configuration getInstance(String path) {
        Configuration.build(path);
        return Configuration.instance;
    }

    public synchronized static void build() {
        Configuration.build(null);
    }

    public synchronized static void build(String path) {
        if (Configuration.instance==null) {
            Configuration.instance = new Configuration();
            Configuration.instance.path = path;
            ConfigManager manager = new ConfigManager();
            try {
                manager.loadConfig(Configuration.instance);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public Integer getHttp_port() {
        return http_port;
    }

    public void setHttp_port(Integer http_port) {
        this.http_port = http_port;
    }

    public Integer getHttps_port() {
        return https_port;
    }

    public void setHttps_port(Integer https_port) {
        this.https_port = https_port;
    }

    public Boolean getCompression() {
        return compression;
    }

    public void setCompression(Boolean compression) {
        this.compression = compression;
    }

    public boolean getDebugConnections() {
        return debugConnections;
    }

    public void setDebugConnections(boolean debugConnections) {
        this.debugConnections = debugConnections;
    }

    public boolean getDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DataBaseProperties getDataBase() {
        return dataBase;
    }

    public void setDataBase(DataBaseProperties dataBase) {
        this.dataBase = dataBase;
    }

    public Boolean isLoaded() {
        return this.loaded;
    }

    public void setIsLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    public void setPrometheus(Boolean prometheus) {
        this.prometheus = prometheus;
    }

    public Boolean getPrometheus() {
        return prometheus;
    }

    public void setRaiseException(boolean raiseException) {
        this.raiseException = raiseException;
    }

    public boolean isRaiseException() {
        return raiseException;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public boolean isLog() {
        return log;
    }

    public void setTelegramBotToken(String telegramBotToken) {
        this.telegramBotToken = telegramBotToken;
    }

    public String getTelegramBotToken() {
        return telegramBotToken;
    }

    public void setNumOfCores(Integer numOfCores) {
        this.numOfCores = numOfCores;
    }

    public Integer getNumOfCores() {
        return numOfCores;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setGerarArquivo(boolean gerarArquivo) {
        this.gerarArquivo = gerarArquivo;
    }

    public boolean isGerarArquivo() {
        return gerarArquivo;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }
}
