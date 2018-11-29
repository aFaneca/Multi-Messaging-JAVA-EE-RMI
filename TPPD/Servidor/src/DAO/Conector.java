/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conector {
    protected static String url = "jdbc:mysql://localhost/pd?serverTimezone=UTC";
    protected static String user = "pd";
    protected static String password = "pd";
    protected static Connection con;

    protected static String ipDB;

    public static Connection getConexao() throws SQLException {

        try{
            //Class.forName("org.gjt.mm.mysql.Driver");
            if(con == null){
                con = DriverManager.getConnection(url,user,password);
            }
            return con;
        }catch(Exception e){
            throw new SQLException(e.getMessage());
        }
    }

    public static String getIpDB() {
        return ipDB;
    }

    public static void setIpDB(String ipDB) {
        Conector.ipDB = ipDB;
        Conector.url = "jdbc:mysql://" + ipDB + "/?serverTimezone=UTC";
    }

    public static void fecharConexao() throws SQLException{
        con.close();
    }
}
