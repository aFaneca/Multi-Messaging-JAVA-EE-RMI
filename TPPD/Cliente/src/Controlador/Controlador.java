/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import GUI.ClienteView;
import GUI.LoginView;
import Modelo.Auth;
import Modelo.Constantes;
import Modelo.MSG;
import javafx.beans.InvalidationListener;

/**
 *
 * @author Ricardo Marques
 */
public class Controlador implements ActionListener{

    LoginView loginView = new LoginView();
    ClienteView clienteView = new ClienteView();
    boolean login = false;
    private Main m;
    Servidor server;

    public Controlador(Main m){
        loginView.setVisible(true);
        loginView.addListener(this, loginView.getB_Login());
        loginView.addListener(this, clienteView.getB_Enviar());
        this.m = m;
    }


    public void addListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    private void tentaLogin(String username, String password, String serverName, int serverPort){
        /*loginView.aprensentarAlerta("Info", "Username: " + username + "\nPassword: "
                + password + "\n" + "Server: " + serverName + "\n Port: " + serverPort);*/

        // 1 - Tenta conexão com o server
        server = new Servidor(serverName, serverPort);
        if(server.testaConexão()){
            loginView.aprensentarAlerta("Info", "Conexão Estabelecida!");
        }else{
            loginView.aprensentarAlerta("Erro", "Erro a estabelecer conexão...");
            return;
        }

        server.addObserver(this.clienteView); // Adiciona a view principal como observer

        // 2 - Envia username + password para o server
        server.conectar();
        server.enviarParaServidor(new MSG(Constantes.TIPOS.AUTH, new Auth(username, password, serverPort, serverPort, serverName)));

        // 3 - Espera por resposta
        try {
            MSG msg = server.receberDoServidor(Constantes.TIPOS.AUTH_REPLY);

            // 4 - Toma decisão de acordo com a resposta recebida
            if((Boolean) msg.getObj()){
                // Se a resposta do server é positiva => login é válido
                fazLogin();
            }else{
                // Senão => login não é válido
                loginView.aprensentarAlerta("Erro", "Dados de login inválidos!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void fazLogin() {
        loginView.setVisible(false);
        clienteView.setVisible(true);
        pedeInfoInicial();
    }
    /* Pede ao Servidor info inicial */
    private void pedeInfoInicial() {
        server.enviarParaServidor(new MSG(Constantes.TIPOS.GET_USER_LIST, null)); // Pede ao server a lista de utilizadores
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
        if(origem == loginView.getB_Login()){
            if(validaLogin(loginView.getUser(), loginView.getPass(), loginView.getServerName(), loginView.getServerPort()))
            tentaLogin(loginView.getUser(), loginView.getPass(), loginView.getServerName(), loginView.getServerPort());
            else{
                loginView.aprensentarAlerta("Erro", "Certifique-se de que preencheu os dados corretamente e tente novamente!");
            }
            /*if("123".equals(loginView.getPass()) && "Tony".equals(loginView.getUser())){
                loginView.loginAceite(); // IR À LOCAL BD VER SE É ACEITE
            }*/

        }else if(origem == clienteView.getB_Enviar()){
            System.out.println(clienteView.getMsg());
        }
    }
}
