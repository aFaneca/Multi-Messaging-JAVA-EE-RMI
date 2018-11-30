/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;

/**
 *
 * @author me
 */
public class Utilizador  implements Serializable {
    static final long serialVersionUID = 1000L;
    private String username;
    private String password;
    private String estado;
    private int portaTCP;
    private int portaUDP;
    private String enderecoIP;
    
    
    public Utilizador(String user, String pw){
        this.username = user;
        this.password = pw;
        this.estado = "Indefinido";
        this.portaTCP = 0;
        this.portaUDP = 0;
        this.enderecoIP = "0";
    }

    public Utilizador(String username, String password, String estado, int portaTCP, int portaUDP, String enderecoIP) {
        this.username = username;
        this.password = password;
        this.estado = estado;
        this.portaTCP = portaTCP;
        this.portaUDP = portaUDP;
        this.enderecoIP = enderecoIP;
    }

    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPortaTCP() {
        return portaTCP;
    }

    public void setPortaTCP(int portaTCP) {
        this.portaTCP = portaTCP;
    }

    public int getPortaUDP() {
        return portaUDP;
    }

    public void setPortaUDP(int portaUDP) {
        this.portaUDP = portaUDP;
    }

    public String getEnderecoIP() {
        return enderecoIP;
    }

    public void setEnderecoIP(String enderecoIP) {
        this.enderecoIP = enderecoIP;
    }
    
    
}
