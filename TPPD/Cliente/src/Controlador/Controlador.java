/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


import GUI.ClienteView;
import GUI.LoginView;
import Modelo.Auth;
import Modelo.Constantes;
import Modelo.MSG;
import Modelo.Mensagem;

/**
 *
 * @author Ricardo Marques
 */
public class Controlador  implements ActionListener{

    LoginView loginView = new LoginView();
    ClienteView clienteView = new ClienteView(this);

    Controlador c;
    private Main m;
    Servidor server;

    public Controlador(Main m){
        loginView.setVisible(true);
        loginView.addListener(this, loginView.getB_Login());
        clienteView.addListener(this, clienteView.getBtn_Sair());
        clienteView.addListener(this, clienteView.getBtn_Chat());
        this.m = m;
        this.c = this;
    }


    private void tentaLogin(String username, String password, String serverName, int serverPort){

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
        server.enviarParaServidor(new MSG(Constantes.TIPOS.AUTH, new Auth(username, password, this.server.getPortoPessoal(), serverPort, serverName)));

        // 3 - Espera por resposta
        try {
            MSG msg = server.receberDoServidor(Constantes.TIPOS.AUTH_REPLY);

            // 4 - Toma decisão de acordo com a resposta recebida
            if((Boolean) msg.getObj()){
                // Se a resposta do server é positiva => login é válido
                server.setUsername(username);
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
        clienteView.setAcaoNoFechoDaJanela(this);
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

        }else if(origem == clienteView.getBtn_Sair()){
            // Terminar Sessão
            terminarSessao();
        }else if(origem == clienteView.getBtn_Chat()){
            String userDestino = clienteView.nomeSelecionado().substring(0, clienteView.nomeSelecionado().lastIndexOf(" "));
            String ultimaPalavra = clienteView.nomeSelecionado().substring(clienteView.nomeSelecionado().lastIndexOf(" ") + 1);

            if(userDestino != null && ultimaPalavra.equals("[Ativo]") && !userDestino.equals(loginView.getUser())) {
                //fazer warnings
                if(clienteView.verificaChatExistente(userDestino) == false){
                    clienteView.adicionaUsersChat(loginView.getUser(), userDestino, "");
                }else{
                    System.out.println("Não criei chat");
                }
            }
        }
    }

    public void mensagemAEnviar(String userDestino, String mensagem) {
        System.out.println("De: " + loginView.getUser() + " Para:" + userDestino +" ->" + mensagem);
        server.enviarParaServidor(new MSG(Constantes.TIPOS.GET_MENSAGEM, new Mensagem(loginView.getUser(),  loginView.getServerPort(), loginView.getServerPort() ,loginView.getServerName(),userDestino, mensagem)));
    }

    public void janelaVaiFechar(){
        terminarSessao();
    }

    private void terminarSessao() {
        server.enviarParaServidor(new MSG(Constantes.TIPOS.SAIR, server.getUsername()));
        server.fecharConexao();
        clienteView.setVisible(false);
        loginView.setVisible(true);
    }

}
