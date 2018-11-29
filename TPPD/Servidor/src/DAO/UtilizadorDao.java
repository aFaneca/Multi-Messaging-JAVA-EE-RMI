/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Entidades.Utilizador;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author me
 */
public class UtilizadorDao {
    private static String nomeDaTabela = "utilizador";


    
    
    
    public static void guardar(Utilizador u){
        String sql = "INSERT INTO " + nomeDaTabela + " (username, password, estado, portaTCP, portaUDP, enderecoIP) values (?,?,?,?,?,?)"; // (?,...,?) - para evitar sql injection
        
        try {
            PreparedStatement ps = Conector.getConexao().prepareStatement(sql);
            
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getEstado());
            ps.setInt(4, u.getPortaTCP());
            ps.setInt(5, u.getPortaUDP());
            ps.setString(6, u.getEnderecoIP());
            
            ps.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(UtilizadorDao.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public static Utilizador recuperar(String username){
        String sql = "SELECT * FROM " + nomeDaTabela + " WHERE username = ? "; // (?,...,?) - para evitar sql injection
        
        try {
            PreparedStatement ps = Conector.getConexao().prepareStatement(sql);
            
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            
            // Transforma o ResultSet num objeto de Utilizador
            return transformarEmObj(rs);
            
        } catch (SQLException ex) {
            Logger.getLogger(UtilizadorDao.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return null;
    }

    public static boolean validaLogin(String username, String password){

        // Verifica se existe algum utilizador na BD com esse username
        Utilizador u = recuperar(username);
        if(u != null){
            // User foi encontrado na BD
            // Comparar password -> ver se dá match
            return u.getPassword().equals(password); // se houver match, o login é válido
        }else{
            // User não foi encontrado na BD
            return false;
        }
    }

    private static Utilizador transformarEmObj(ResultSet rs) throws SQLException{
        String username, password;
        String estado;
        int portaTCP;
        int portaUDP;
        String enderecoIP;
        
        if(rs.first()){
            return new Utilizador(rs.getString("username"), rs.getString("password"), rs.getString("estado"), rs.getInt("portaTCP"), rs.getInt("portaUDP"), rs.getString("enderecoIP"));
        }

        return null;
    }
}
