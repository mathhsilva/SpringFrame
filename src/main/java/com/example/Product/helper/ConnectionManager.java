package com.example.Product.helper;

import br.framework.classes.DataBase.CustomConnection;
import br.framework.classes.DataBase.DataBaseProperties;
import br.framework.classes.DataBase.Oracle.OracleConnection;
import br.framework.classes.DataBase.PostgreSQL.PostgresConnection;
import br.framework.classes.DataBase.SqlServer.SqlServerConnection;
import br.framework.classes.helpers.Log;
import br.framework.classes.helpers.Types;
import br.framework.interfaces.IConnection;
import com.example.Product.model.Configuration;
import com.google.common.base.Strings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public class EventHandler implements CustomConnection.ConnectionEvents {

        @Override
        public synchronized void onClose(IConnection connection) {
            if (Configuration.getInstance().getDebugConnections()) {
                try {
                    throw new Exception("Fechando conexão");
                } catch (Exception e) {
                    String stack = FlexFunctions.getStackByException(e);
                    connection.setStackClose(stack);
                }
                Log.info("Fechando a conexão '" + connection.getName() + "'");
                Log.info("Stack: '" + connection.getStackClose() + "'");
            }
        }

        @Override
        public synchronized void onOpen(IConnection connection) {
            if (Configuration.getInstance().getDebugConnections()) {
                try {
                    throw new Exception("Abrindo conexão");
                } catch (Exception e) {
                    String stack = FlexFunctions.getStackByException(e);
                    connection.setStackCriacao(stack);
                }
                Log.info("Abrindo a conexão '" + connection.getName() + "'");
                Log.info("Stack: '" + connection.getStackCriacao() + "'");
            }
        }

        @Override
        public synchronized void onQueryFactory(IConnection connection, String sql) {
            if (Configuration.getInstance().getDebug()) {

            }
        }

        @Override
        public synchronized void onQuery(IConnection connection, String sql) {
            if (Configuration.getInstance().getDebug()) {

            }

        }

    }

    public enum SourceDataBase {
        RpServices,
        VdaDet,
        DiaPorcento,

    }

    private static ConnectionManager instance;
    private HikariDataSource dataSourceVdaDet;
    private HikariDataSource dataSource;
    private DataBaseProperties propertiesVdaDet;
    private Configuration configuration = Configuration.getInstance();
    private String connString;
    private EventHandler eventHandler;


    public ConnectionManager()  {
        this.buildAttributes(SourceDataBase.RpServices);
        this.eventHandler = new EventHandler();
    }




    private synchronized void closePool() throws SQLException {
        this.dataSource.close();
    }

    private String getApplicationName() {
        String versao = Configuration.getInstance().getVersion();
        String appName = "RpServices";
        appName = appName + "_" + versao + "/Usu:" + Configuration.getInstance().getDataBase().getUserName();
        return appName;
    }

    public HikariDataSource getHikariDataSource(DataBaseProperties properties) {
        final HikariConfig hikariConfig = new HikariConfig();
        String driver = "";
        String url = "";
        String appName = this.getApplicationName();
        String sqlTest = "SELECT 1";

        driver = "org.postgresql.Driver";
        url = "jdbc:postgresql://" + properties.getHostAdress();
        url += ":" + properties.getPort().toString() + "/";
        url += properties.getDataBaseName();
        url += "?application-name=" + appName + ";";


        hikariConfig.setJdbcUrl(url);
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setUsername(properties.getUserName());
        hikariConfig.setPassword(properties.getPassword());
        hikariConfig.setMaximumPoolSize(properties.getMaxConnections());
        hikariConfig.setConnectionTestQuery(sqlTest);
        hikariConfig.setIdleTimeout(60000);
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        return hikariDataSource;
    }

    private synchronized void buildAttributes(SourceDataBase sourceDataBase) {
        DataBaseProperties properties;
        if (sourceDataBase==SourceDataBase.RpServices) {
            properties = this.configuration.getDataBase();
            this.dataSource = this.getHikariDataSource(properties);
        } else if (sourceDataBase==SourceDataBase.VdaDet) {
            properties = propertiesVdaDet;
            this.dataSourceVdaDet = this.getHikariDataSource(properties);
        }
    }

    public synchronized void buildAttributesVdaDet() {
        if (this.propertiesVdaDet!=null) {
            this.buildAttributes(SourceDataBase.VdaDet);
        } else {
            this.propertiesVdaDet = this.configuration.getDataBase();
            this.propertiesVdaDet.setServerType(this.configuration.getDataBase().getServerType());
            this.dataSourceVdaDet = this.dataSource;
        }
    }

    public synchronized static ConnectionManager getInstance() {
        if (ConnectionManager.instance == null) {
            ConnectionManager.instance = new ConnectionManager();
        }
        return ConnectionManager.instance;
    }

    public synchronized static void build() {
        if (ConnectionManager.instance == null) {
            ConnectionManager.instance = new ConnectionManager();
        }
        ConnectionManager.setApplicationName();
    }

    public static synchronized StackTraceElement getStackElement(Integer layer) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Integer position = 0;
        if (stackTraceElements.length>layer) {
            position = layer;
        }
        return stackTraceElements[position];
    }

    public static void logIfDebug(String msg) {
        if (Configuration.getInstance().getDebug()) {
            Log.info(msg, ConnectionManager.getStackElement(3));
        }
    }

    public static IConnection newNoPooledConnection() throws SQLException, ClassNotFoundException {
        DataBaseProperties properties;
        properties = Configuration.getInstance().getDataBase();
        Types.DataBase Banco = properties.getServerType();
        IConnection connection = null;
        if (Banco == Types.DataBase.Postgres) {
            properties.setDriverName("org.postgresql.Driver");
            connection = new PostgresConnection(properties);
        } else if (Banco == Types.DataBase.Oracle) {
            properties.setDriverName("oracle.jdbc.OracleDriver");
            connection = new OracleConnection(properties);
        } else if (Banco == Types.DataBase.SqlServer) {
            properties.setDriverName("net.sourceforge.jtds.jdbc.Driver");
            connection = new SqlServerConnection(properties);
        }
        connection.connect();
        return connection;
    }

    public synchronized static IConnection newConnection() throws SQLException {
        ConnectionManager manager = ConnectionManager.getInstance();
        ConnectionManager.logIfDebug("Obtendo nova conexão");
        String stack = "";
        if (Configuration.getInstance().getDebugConnections()) {
            try {
                throw new Exception("Criação conexão ");
            } catch (Exception e) {
                stack = FlexFunctions.getStackByException(e);
                Log.info("Stack Criação: " + stack);
            }
        }
        IConnection connection = manager.factory();
        if (Configuration.getInstance().getDebugConnections()) {
            connection.setStackCriacao(stack);
        }
        //com.example.Product.helper.ConnectionManager.setApplicationName(connection, false);
        ConnectionManager.logIfDebug("Nova conexão obtida.");
        return connection;
    }

    public synchronized  static IConnection newConnection(SourceDataBase sourceDataBase) throws SQLException {
        ConnectionManager manager = ConnectionManager.getInstance();
        String stack = "";
        if (Configuration.getInstance().getDebugConnections()) {
            try {
                throw new Exception("Criação conexão ");
            } catch (Exception e) {
                stack = FlexFunctions.getStackByException(e);
                Log.info("Stack Criação: " + stack);
            }
        }
        IConnection connection = manager.factory();
        if (Configuration.getInstance().getDebugConnections()) {
            connection.setStackCriacao(stack);
        }
        return connection;
    }

    public synchronized static void ClearInstance() {
        ConnectionManager.instance = null;
        System.gc();
    }


    private synchronized IConnection buildConnectionInstance(DataBaseProperties properties, Connection connection) {
        IConnection result = null;
        if (properties.getServerType() == Types.DataBase.Postgres) {
            PostgresConnection psConnection = new PostgresConnection(properties, connection);
            try {
                psConnection.executeDDLCommand("set client_encoding='utf8'");
            } catch (Exception e) {
                Log.error(e);
            }
            psConnection.setEventHandler(this.eventHandler);
            result = psConnection;
        } else if (this.configuration.getDataBase().getServerType() == Types.DataBase.Oracle) {
            OracleConnection oraConnection = new OracleConnection(properties, connection);
            String schema = oraConnection.getDataBaseName();
            if (Strings.isNullOrEmpty(schema)) {
                schema = "ERP";
            }
            try {
                oraConnection.executeDDLCommand("ALTER SESSION SET CURRENT_SCHEMA = " + schema);
            } catch (Exception e) {
                System.out.println("Falha ao atribuir Schema " + schema);
                Log.error(e);
            }
            oraConnection.setEventHandler(this.eventHandler);
            result = oraConnection;
        } else if (this.configuration.getDataBase().getServerType() == Types.DataBase.SqlServer) {
            SqlServerConnection sqlSrvConnection = new SqlServerConnection(properties, connection);
            sqlSrvConnection.setEventHandler(this.eventHandler);
            result = sqlSrvConnection;
        }
        result.setName(result.getClass().getName() + "@" + connection.hashCode());
        if (Configuration.getInstance().getDebugConnections()) {
            Log.info("Criação conexão: " + result.getName());
        }
        result.setConnection(connection);
        return result;
    }

    public static synchronized void setApplicationName(IConnection connection, boolean noPooled) throws SQLException {
        if (connection.getProperties().getServerType()== Types.DataBase.Postgres) {
            ConnectionManager.logIfDebug("Inicio do setApplicationName");
            String versao = Configuration.getInstance().getVersion();
            String appName = "RpServices";
            if (noPooled) {
                appName = "NP_" + appName;
            }
            connection.executeDDLCommand("SET application_name=" + connection.castAsString(appName + "_" + versao + "/Usu:" + connection.getUserName()));
            ConnectionManager.logIfDebug("Termino do setApplicationName");
        }
    }
    public static synchronized void setApplicationName() {
        try {
            List<IConnection> connections = new ArrayList<>();
            try {
                if (Configuration.getInstance().getDataBase().getServerType() == Types.DataBase.Postgres) {
                    for (int i = 0; i < Configuration.getInstance().getDataBase().getMaxConnections(); i++) {
                        IConnection connection = ConnectionManager.newConnection();
                        connections.add(connection);
                        ConnectionManager.setApplicationName(connection, false);
                    }
                }
            } finally {
                for (IConnection connection: connections) {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        Log.error(e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized IConnection factory() throws SQLException {
        DataSource dataSource = this.dataSource;
        DataBaseProperties properties = this.configuration.getDataBase();
        IConnection connection = this.buildConnectionInstance(properties, dataSource.getConnection());
        ConnectionManager.setApplicationName(connection, false);
        return connection;
    }

    public synchronized IConnection newDirectConnection() throws SQLException {
        IConnection result = null;
        if (this.configuration.getDataBase().getServerType() == Types.DataBase.Postgres) {
            PostgresConnection psConnection = new PostgresConnection(this.configuration.getDataBase());
            try {
                psConnection.connect();
            } catch (ClassNotFoundException e) {
                Log.error(e);
                throw new SQLException(e);
            }
            psConnection.executeDDLCommand("set client_encoding='utf8'");
            result = psConnection;
        } else if (this.configuration.getDataBase().getServerType() == Types.DataBase.Oracle) {
            OracleConnection oraConnection = new OracleConnection(this.configuration.getDataBase());
            String schema = oraConnection.getDataBaseName();
            if (Strings.isNullOrEmpty(schema)) {
                schema = "ERP";
            }
            try {
                oraConnection.connect();
            } catch (ClassNotFoundException e) {
                Log.error(e);
                throw new SQLException(e);
            }
            try {
                oraConnection.executeDDLCommand("ALTER SESSION SET CURRENT_SCHEMA = " + schema + ";");
            } catch (Exception e) {
                System.out.println("Falha ao atribuir Schema " + schema);
            }
            result = oraConnection;
        } else if (this.configuration.getDataBase().getServerType() == Types.DataBase.SqlServer) {
            SqlServerConnection sqlSrvConnection = new SqlServerConnection(this.configuration.getDataBase());
            try {
                sqlSrvConnection.connect();
            } catch (ClassNotFoundException e) {
                Log.error(e);
                throw new SQLException(e);
            }
            result = sqlSrvConnection;
        }
        return result;
    }


    public String getConnString() {
        return connString;
    }

    public void setConnString(String connString) {
        this.connString = connString;
    }

    public DataSource getDataSourceVdaDet() {
        return dataSourceVdaDet;
    }

    public void setDataSourceVdaDet(HikariDataSource dataSourceVdaDet) {
        this.dataSourceVdaDet = dataSourceVdaDet;
    }

    public DataBaseProperties getPropertiesVdaDet() {
        return propertiesVdaDet;
    }

    public void setPropertiesVdaDet(DataBaseProperties propertiesVdaDet) {
        this.propertiesVdaDet = propertiesVdaDet;
        this.buildAttributesVdaDet();
    }

}
