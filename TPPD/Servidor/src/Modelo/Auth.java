package Modelo;


import java.io.Serializable;

/*
 * Modelo padrão para o envio da mensagem do tipo Auth
 *
 * */
public class Auth implements Serializable {
    static final long serialVersionUID = 10L;
    private String username;
    private String password;
    private int portaTCP;
    private int portaUDP;
    private String enderecoIP;


    public Auth(String username, String password, int portaTCP, int portaUDP, String enderecoIP) {
        this.username = username;
        this.password = password;
        this.portaTCP = portaTCP;
        this.portaUDP = portaUDP;
        this.enderecoIP = enderecoIP;
    }

    /* Getters & Setters */
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
