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
    private String nomeDaTabela;

    public UtilizadorDao(String nomeDaTabela) {
        this.nomeDaTabela = nomeDaTabela;
    }
    
    
    
    public void guardar(Utilizador u){
        String sql = "INSERT INTO " + this.nomeDaTabela + " (username, password, estado, portaTCP, portaUDP, enderecoIP) values (?,?,?,?,?,?)"; // (?,...,?) - para evitar sql injection
        
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
    
    public Utilizador recuperar(String username){
        String sql = "SELECT * FROM " + this.nomeDaTabela + " WHERE username = ? "; // (?,...,?) - para evitar sql injection
        
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
