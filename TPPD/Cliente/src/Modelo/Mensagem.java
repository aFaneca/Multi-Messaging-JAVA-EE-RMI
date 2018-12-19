package Modelo;

import java.io.Serializable;

public class Mensagem implements Serializable {
    static final long serialVersionUID = 1000L;
    private String origem;
    private int minhaPortaTCP;
    private int minhaPortaUDP;
    private String meuEnderecoIP;
    private String destino;
    private String mensagemString;


    public Mensagem(String origem, int minhaPortaTCP, int minhaPortaUDP, String meuEnderecoIP, String destino, String mensagemString) {
        this.origem = origem;
        this.minhaPortaTCP = minhaPortaTCP;
        this.minhaPortaUDP = minhaPortaUDP;
        this.meuEnderecoIP = meuEnderecoIP;
        this.destino = destino;
        this.mensagemString = mensagemString;
    }

    /* Getters & Setters */
    public String getOrigem() {
        return origem;
    }

    public void setUsername(String username) {
        this.origem = origem;
    }

    public int getPortaTCP() {
        return minhaPortaTCP;
    }

    public void setPortaTCP(int portaTCP) {
        this.minhaPortaTCP = portaTCP;
    }

    public int getPortaUDP() {
        return minhaPortaUDP;
    }

    public void setPortaUDP(int portaUDP) {
        this.minhaPortaUDP = portaUDP;
    }

    public String getEnderecoIP() {
        return meuEnderecoIP;
    }

    public void setEnderecoIP(String enderecoIP) {
        this.meuEnderecoIP = enderecoIP;
    }

    public String getUsernameDestino(){return destino;}

    public String getDestino(){
        return destino;
    }

    public String getMensagemString(){return mensagemString;}
}

