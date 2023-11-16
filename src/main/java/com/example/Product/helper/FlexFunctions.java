package com.example.Product.helper;

import br.framework.classes.DataBase.Oracle.OracleCommandDDLExecuter;
import br.framework.classes.DataBase.Oracle.OracleConnection;
import br.framework.classes.DataBase.PostgreSQL.PostgresCommandDDLExecuter;
import br.framework.classes.DataBase.PostgreSQL.PostgresConnection;
import br.framework.classes.DataBase.SequenceProperties;
import br.framework.classes.DataBase.SqlServer.SqlServerCommandDDLExecuter;
import br.framework.classes.DataBase.SqlServer.SqlServerConnection;
import br.framework.classes.helpers.MemoryStream;
import br.framework.classes.helpers.StringFunctions;
import br.framework.classes.helpers.StringList;
import br.framework.classes.helpers.Types;
import br.framework.exceptions.AnnotationNotFound;
import br.framework.exceptions.RelationEntityNotFoundException;
import br.framework.interfaces.ICommandDDLExecuter;
import br.framework.interfaces.IConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class FlexFunctions {


    public enum ModType {
        MOD,
        MOD10,
        MOD11
    }


    protected static String getStackByException(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stack = sw.toString();
        return stack;
    }

    public static long qtdeDias(Date dtInicial, Date dtFinal) {
        if ((dtFinal != null) && (dtInicial != null)) {
            return (dtFinal.getTime() - dtInicial.getTime()) / 86400000;
        }
        return 1;
    }

    public static String getImageType(MemoryStream stream) throws Exception {
        String mimeType = URLConnection.guessContentTypeFromStream(stream.toInputStream());
        byte[] bytes = stream.getBytes();
        String result = "";
        if ((bytes!=null) && (bytes.length>2)) {
            if ((mimeType == null) && ((bytes[0]==66) && (bytes[1]==77))) {
                mimeType = "bmp";
            }
        }
        if (!com.google.common.base.Strings.isNullOrEmpty(mimeType)) {
            mimeType = mimeType.toLowerCase();
            if (mimeType.contains("bmp")) {
                result = "BMP";
            } else if (mimeType.contains("png")) {
                result = "PNG";
            } else if (mimeType.contains("jpeg")) {
                result = "JPEG";
            }
        }
        return result;
    }

    public static long qtdeMeses(Date dtInicial, Date dtFinal) {
        int count = 0;
        if ((dtInicial == null) || (dtFinal == null)) {
            count = -1;
        }
        if ((dtFinal != null) && (dtInicial != null)) {
            if (dtInicial != null && dtFinal != null && dtInicial.before(dtFinal)) {
                Calendar clStart = Calendar.getInstance();
                clStart.setTime(dtInicial);
                Calendar clEnd = Calendar.getInstance();
                clEnd.setTime(dtFinal);
                while (clStart.get(Calendar.MONTH) != clEnd.get(Calendar.MONTH) || clStart.get(Calendar.YEAR) != clEnd.get(Calendar.YEAR)) {
                    clStart.add(Calendar.MONTH, 1);
                    count++;
                }
            }
        }
        return count;
    }

    public static Long inteiro(String str, Integer defaultValue) {
        long result = defaultValue;
        String ss = "";
        if (!Strings.isNullOrEmpty(str)) {
            for (Integer i = 0; i < str.length(); i++) {
                Character chr = str.charAt(i);
                if (chr.equals('.')) {
                    break;
                }
                if (chr.equals('-') && ("".equals(ss))) {
                    ss = ss + chr;
                }
                String values = "0123456789";
                if (Chars.contains(values.toCharArray(), chr)) {
                    ss = ss + chr;
                }
            }
            if (!"".equals(ss)) {
                result = Ints.tryParse(ss);
            }
        }
        return result;
    }

    public static Long inteiro(String str) {
        return FlexFunctions.inteiro(str, 0);
    }

    public static Double divide(Double n1, Double n2) {
        Double result = 0.0;
        try {
            if (n2 > 0) {
                result = n1 / n2;
            }
        } catch (Exception e) {
            result = 0.0;
        }
        return result;
    }

    public static Double round(Double valor, Integer decimais) {
        DecimalFormat df2 = new DecimalFormat(StringFunctions.padRight("###.", "#", decimais + 4));
        return Double.valueOf(df2.format(valor).replace(',', '.'));
    }

    public static String getES(String tipoDcto) {
        String result = "N";
        if ("EAQ,EOE,EET,EDV,ETE,EBR,EPD,ESE,EES,EEC".contains(tipoDcto)) {
            result = "E";
        }
        if ("EOS,EST,EDC,EVD,EVA,EVP,EVL,ETS,EDO,EBC,ECI,ETM,ESC,EFE,EPE,ETA,ESS".contains(tipoDcto)) {
            result = "S";
        }
        return result;
    }

    public static Calendar getCalendarDateOnly(Date data) {
        Calendar dt = Calendar.getInstance();
        dt.setTime(data);
        dt.set(Calendar.HOUR, 0);
        dt.set(Calendar.MINUTE, 0);
        dt.set(Calendar.MILLISECOND, 0);
        return dt;
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private static Date joinDataHora(Date data, Date hora) {
        Calendar calendar = new GregorianCalendar();
        Calendar calendarH = new GregorianCalendar();
        calendar.setTime(data);
        calendarH.setTime(hora);
        calendar.set(Calendar.HOUR, calendarH.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendarH.get(Calendar.MINUTE));
        return calendar.getTime();
    }

    public static StringList getTabelasPorMes(String nomeTabela, Date dataI, Date dataF) {
        StringList tabelas = new StringList();

        Calendar calendarAtual = Calendar.getInstance();
        Calendar calendarFim = Calendar.getInstance();
        calendarFim.setTime(dataF);
        calendarFim.set(Calendar.DAY_OF_MONTH, 1);
        calendarFim.set(Calendar.HOUR, 1);
        calendarFim.set(Calendar.MINUTE, 0);
        calendarFim.set(Calendar.MILLISECOND, 0);
        calendarFim.set(Calendar.SECOND, 0);

        calendarAtual.setTime(dataI);
        calendarAtual.set(Calendar.DAY_OF_MONTH, 1);
        calendarAtual.set(Calendar.HOUR, 1);
        calendarAtual.set(Calendar.MINUTE, 0);
        calendarAtual.set(Calendar.MILLISECOND, 0);
        calendarAtual.set(Calendar.SECOND, 0);

        while (calendarAtual.compareTo(calendarFim) <= 0) {
            Date dt = calendarAtual.getTime();
            String tabela = nomeTabela + StringFunctions.formatDateTime("MMYY", dt);
            tabelas.add(tabela);
            calendarAtual.add(Calendar.MONTH, 1);//incrementa quantidade de meses
        }
        return tabelas;
    }

    public static String getDigito(String n) {
        int i, ii, h, t, d;
        char currentChar;
        int currentValue;
        //
        t = 0;
        h = 9;
        d = 0;
        //
        for (i = 0; i < n.length(); i++) {
            //
            ii = n.length() - i - 1;
            currentChar = n.charAt(ii);
            currentValue = Character.getNumericValue(currentChar);
            //
            t = t + currentValue * h;
            if (h <= 2) {
                h = 9;
            } else {
                h = h - 1;
            }
            d = t % 11;
            if (d == 10) {
                d = 0;
            }
        }
        return Integer.toString(d);
    }

    public static Long getContador(IConnection connection, String nome, Boolean digitoVerificador) throws Exception {
        Long result = FlexFunctions.getSequencia(connection, nome);
        if (digitoVerificador) {
            result = Longs.tryParse(result.toString() + FlexFunctions.getDigito(result.toString()));
        }
        return result;
    }

    public static String readStr(String nome, String str) {
        Integer ii;
        Integer ff;
        String result;
        ii = str.indexOf("{{" + nome + "|") + nome.length() + 3;
        ff = str.indexOf("|" + nome + "}}");
        result = str.substring(ii, ff - ii);
        return result;
    }

    public static String getTexto(IConnection connection, String identificador) throws SQLException {
        String result = "";
        String sql = "select text_texto from textos where text_identificador=" + connection.castAsString(identificador);
        ResultSet q = connection.queryFactory(sql);
        if (q.next()) {
            result = q.getString("text_texto");
        }
        q.close();
        return result;
    }

    public static String getNomeContadorImpressoUnidade(IConnection connection, String codigoImpresso, String unidade) throws SQLException {
        return FlexFunctions.readStr(unidade, FlexFunctions.getTexto(connection, "CONTADOR_UN" + codigoImpresso));
    }


    public static Long getContadorTabela(IConnection connection, String tabela, String campoNome, String campoValor, String contador) throws SQLException {
        Long result = 0L;
        String sql = "SELECT * FROM " + tabela + " WHERE " + campoNome + "=" + connection.castAsString(contador);
        ResultSet q = connection.queryFactory(sql);
        try {
            if (q.next()) {
                if (connection.getDataBaseType() == Types.DataBase.Oracle) {
                    result = 1L;
                }
                connection.executeDDLCommand("INSERT INTO " + tabela + " (" + campoNome + "," + campoValor + ") VALUES (" + connection.castAsString(contador) + "," + result.toString() + ")");
            }
        } finally {
            q.close();
        }
        try {
            if (connection.getDataBaseType() == Types.DataBase.Postgres) {
                sql = "UPDATE " + tabela + " SET " + campoValor + "=" + campoValor + "+1 WHERE " + campoNome + "=" + connection.castAsString(contador) + " RETURNING " + campoValor;
                q = connection.queryFactory(sql);
                result = Long.valueOf(q.getInt(campoValor));
            } else if (connection.getDataBaseType() == Types.DataBase.SqlServer) {
                sql = "update " + tabela + " set " + campoValor + "=" + campoValor + "+1 where " + campoNome + "=";
                sql += connection.castAsString(contador) + "; select " + campoValor + " from " + tabela + " where " + campoNome + " = " + connection.castAsString(contador);
                q = connection.queryFactory(sql);
                result = Long.valueOf(q.getInt(campoValor));
            } else {
                sql = "SELECT " + campoValor + " FROM " + tabela + " WHERE " + campoNome + "=" + connection.castAsString(contador);
                q = connection.queryFactory(sql);
                result = Long.valueOf(q.getInt(campoValor));
                connection.executeDDLCommand("UPDATE " + tabela + " SET " + campoValor + "=" + campoValor + "+1 WHERE " + campoNome + "=" + connection.castAsString(contador));
            }
        } finally {
            q.close();
        }
        return result;
    }

    public static Long getContadorImpresso(IConnection connection, String codImpresso, String unidade) throws Exception {
        String n;
        Long result = 0L;
        if (!Strings.isNullOrEmpty(codImpresso)) {

            String sql = "select * from impressos where impr_codigo=" + connection.castAsString(codImpresso);

            ResultSet q = connection.queryFactory(sql);

            if (q.next()) {
                n = q.getString("Impr_NomeContador");
                if (n == null) {
                    n = "";
                }
                n = n.toUpperCase().trim();
                String impr_Geral = q.getString("Impr_Geral");
                if ("X".equals(impr_Geral)) {
                    n = FlexFunctions.getNomeContadorImpressoUnidade(connection, codImpresso, unidade);
                }
                if (!Strings.isNullOrEmpty(n)) {
                    if ("U".equals(impr_Geral)) {
                        n = n + unidade;
                    }
                    if ("M".equals(impr_Geral) && (!Strings.isNullOrEmpty(unidade))) {
                        n = n + unidade;
                    }
                    String comportamento = q.getString("Impr_comportamento");
                    if ((n.contains("*")) || (comportamento.contains("CT"))) {
                        result = FlexFunctions.getContadorTabela(connection, "Contadores", "Cont_Nome", "Cont_Posicao", n);
                    } else {
                        result = FlexFunctions.getContador(connection, n, false);
                    }
                }
            }
        }
        return result;
    }

    public static List<Integer> getEspecificos(IConnection connection) throws Exception {
        List<Integer> list = new ArrayList<>();
        String SQL = "select espe_codigo from especificos";
        ResultSet q = connection.queryFactory(SQL);
        while (q.next()) {
            list.add(q.getInt("espe_codigo"));
        }
        q.close();
        return list;
    }

    public static Boolean hasEspecifico(IConnection connection, Integer codigo) throws IllegalAccessException, RelationEntityNotFoundException, InstantiationException, IOException, SQLException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, AnnotationNotFound {
        String SQL = "select count(espe_codigo) as registros from especificos where espe_codigo='" + codigo + "'";
        ResultSet Q = connection.queryFactory(SQL);
        Boolean result = false;
        if (Q.next()) {
            result = Q.getInt("registros") > 0;
        }
        Q.close();
        return result;
    }

    public static String getComportamentoEspecifico(IConnection connection, Integer codigo) throws IllegalAccessException, RelationEntityNotFoundException, InstantiationException, IOException, SQLException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, AnnotationNotFound {
        String SQL = "select espe_comportamento  from especificos where espe_codigo='" + codigo + "'";
        ResultSet Q = connection.queryFactory(SQL);
        String result = "";
        if (Q.next()) {
            result = Q.getString("espe_comportamento");
        }
        Q.close();

        result = Strings.nullToEmpty(result);
        return result;
    }

    public static String strZero(Long n, Integer tamanho) {
        String s = n.toString();
        String result;
        if (s.length() > tamanho) {
            result = StringFunctions.rightStr(s, tamanho);
        } else {
            result = StringFunctions.padLeft(s, "0", tamanho);
        }
        if (n < 0) {
            result = "-" + StringFunctions.rightStr(result, result.length() - 1);
        }
        return result;
    }

    public static Long getSequencia(IConnection connection, String seqName) throws Exception {
        SequenceProperties sequenceValue = new SequenceProperties(seqName, 0, 1);
        try {
            return connection.getNextSequenceValue(sequenceValue);
        } catch (Exception e) {
            e.printStackTrace();
            if (!connection.hasSequence(seqName)) {
                ICommandDDLExecuter commandDDLExecuter = null;
                if (connection.getDataBaseType() == Types.DataBase.Postgres) {
                    commandDDLExecuter = new PostgresCommandDDLExecuter((PostgresConnection) connection, "");
                } else if (connection.getDataBaseType() == Types.DataBase.SqlServer) {
                    commandDDLExecuter = new SqlServerCommandDDLExecuter((SqlServerConnection) connection, "");
                } else if (connection.getDataBaseType() == Types.DataBase.Oracle) {
                    commandDDLExecuter = new OracleCommandDDLExecuter((OracleConnection) connection, "");
                }
                if (commandDDLExecuter != null) {
                    commandDDLExecuter.addSequence(sequenceValue);
                    commandDDLExecuter.run(true);
                }
            }
            return connection.getNextSequenceValue(sequenceValue);
        }
    }
}
