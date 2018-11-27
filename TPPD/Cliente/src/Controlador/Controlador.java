/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import GUI.ClienteFrame;
import javafx.beans.InvalidationListener;

import javax.swing.*;

/**
 *
 * @author Ricardo Marques
 */
public class Controlador implements ActionListener{

    ClienteFrame f = new ClienteFrame();
    boolean login = false;
    private Main m;
    Servidor server;

    public Controlador(Main m){
        f.setVisible(true);
        f.addListener(this, f.getB_Login());
        f.addListener(this, f.getB_Enviar());
        this.m = m;
    }


    public void addListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    private void tentaLogin(String username, String password, String serverName, int serverPort){
        /*f.aprensentarAlerta("Info", "Username: " + username + "\nPassword: "
                + password + "\n" + "Server: " + serverName + "\n Port: " + serverPort);*/

        // 1 - Tenta conexão com o server
        server = new Servidor(serverName, serverPort);
        if(server.testaConexão()){
            f.aprensentarAlerta("Info", "Conexão Estabelecida!");
        }else{
            f.aprensentarAlerta("Erro", "Erro a estabelecer conexão...");
        }
        // 2 - Envia username + password para o server

        // 3 - Espera por resposta

        // Toma decisão de acordo com a resposta recebida

        return;
    }

    private boolean validaLogin(String username, String password, String serverName, int serverPort){
        if(username.isEmpty() || password.isEmpty() || serverName.isEmpty())
            return false;
        return true;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource();
        //Menu iniciar
        if(origem == f.getB_Login()){
            if(validaLogin(f.getUser(), f.getPass(), f.getServerName(), f.getServerPort()))
                tentaLogin(f.getUser(), f.getPass(), f.getServerName(), f.getServerPort());
            else{
                f.aprensentarAlerta("Erro", "Certifique-se de que preencheu os dados corretamente e tente novamente!");
            }
            /*if("123".equals(f.getPass()) && "Tony".equals(f.getUser())){
                f.loginAceite(); // IR À LOCAL BD VER SE É ACEITE
            }*/

        }else if(origem == f.getB_Enviar()){
            System.out.println(f.getMsg());
        }
    }
}
