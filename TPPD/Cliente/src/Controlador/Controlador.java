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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GUI.ChatView;
import GUI.ClienteView;
import GUI.LoginView;
import Modelo.*;

/**
 *
 * @author Ricardo Marques
 */
public class Controlador implements ActionListener{

    LoginView loginView = new LoginView();
    //ClienteView clienteView = new ClienteView();
    ClienteView clienteView = new ClienteView(this);
    boolean login = false;
    private Main m;
    Servidor server;

    public Controlador(Main m){
        loginView.setVisible(true);
        loginView.addListener(this, loginView.getB_Login());
        clienteView.addListener(this, clienteView.getBtn_Sair());
        clienteView.addListener(this, clienteView.getBtn_Chat());
        //loginView.addListener(this, clienteView.getB_Enviar());
        this.m = m;
    }


    private void tentaLogin(String username, String password, String serverName, int serverPort){
        /*loginView.aprensentarAlerta("Info", "Username: " + username + "\nPassword: "
                + password + "\n" + "Server: " + serverName + "\n Port: " + serverPort);*/

        // 1 - Tenta conexão com o server
        server = new Servidor(this, serverName, serverPort);
        if(server.testaConexão()){
            loginView.aprensentarAlerta("Info", "Conexão Estabelecida!");
        }else{
            loginView.aprensentarAlerta("Erro", "Erro a estabelecer conexão...");
            return;
        }

        server.addObserver(this.clienteView); // Adiciona a view principal como observer

        // 2 - Envia username + password para o server
        server.conectar();
        server.enviarParaServidor(new MSG(Constantes.MENSAGEM_TIPO.AUTH, new Auth(username, password, serverPort, serverPort, serverName)));

        // 3 - Espera por resposta
        try {
            MSG msg = server.receberDoServidor(Constantes.MENSAGEM_TIPO.AUTH_REPLY);

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
        clienteView.setTitle("PD - " + getServer().getUsername());
        pedeInfoInicial();
    }
    /* Pede ao Servidor info inicial */
    private void pedeInfoInicial() {
        server.enviarParaServidor(new MSG(Constantes.MENSAGEM_TIPO.GET_USER_LIST, null)); // Pede ao server a lista de utilizadores
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
            String userDestino = null;
            String userStatus = null;

            if(clienteView.nomeSelecionado() == null) return; // this means no name was selected
            userDestino = clienteView.nomeSelecionado().substring(0, clienteView.nomeSelecionado().lastIndexOf(" ["));
            userStatus = clienteView.nomeSelecionado().substring(clienteView.nomeSelecionado().lastIndexOf("[ "));

            if(userDestino != null && userStatus.equals("[ Ativo ]")){
                iniciarChat(userDestino);
            }



            /*
            String userDestino = null;
            String ultimaPalavra = null;

            if(clienteView.nomeSelecionado() == null) return; // this means no name was selected

            userDestino = clienteView.nomeSelecionado().substring(0, clienteView.nomeSelecionado().lastIndexOf(" "));
            ultimaPalavra = clienteView.nomeSelecionado().substring(clienteView.nomeSelecionado().lastIndexOf(" ") + 1);

            List<Utilizador> utilizadoresNoChat = new ArrayList<>();

            ChatPrivado chatPrivado = new ChatPrivado(null);

            ChatView cv = new ChatView(server.getUsername(), "jo", chatPrivado);
            */
        }
    }

    private void iniciarChat(String userDestino) {
        List<String> users = new ArrayList<>();
        users.add(server.getUsername());
        users.add(userDestino);

        server.enviarParaServidor(new MSG(Constantes.MENSAGEM_TIPO.BEGIN_CHAT, users));
    }


    public void janelaVaiFechar(){
        terminarSessao();
    }

    public void janelaDeChatVaiFechar(ChatView view, ChatPrivado cp){
        server.listaDeChatsPrivados.remove(cp);
        server.listaDeChatViews.remove(view);

    }

    private void terminarSessao() {
        server.enviarParaServidor(new MSG(Constantes.MENSAGEM_TIPO.SAIR, server.getUsername()));
        server.fecharConexao();
        clienteView.setVisible(false);
        loginView.setVisible(true);
    }


    /* Getters & Setters */

    public Servidor getServer() {
        return server;
    }

    public void enviarMensagemChatPrivado(ChatPrivado chatPrivado, String text, String username) {

        /* OBJETO A ENVIAR */
        /*List<Object> objs = new ArrayList<>();

        objs.add(text);
        objs.add(chatPrivado);*/

        Map<String, Object> obj= new HashMap<>();
        obj.put(Constantes.MENSAGEM_TEXTO, text);
        obj.put(Constantes.MENSAGEM_REMETENTE, username);
        obj.put(Constantes.MENSAGEM_CHAT_PRIVADO, chatPrivado);


        server.enviarParaServidor(new MSG(Constantes.MENSAGEM_TIPO.SEND_PRIVATE_CHAT_MESSAGE, obj));
    }
}
