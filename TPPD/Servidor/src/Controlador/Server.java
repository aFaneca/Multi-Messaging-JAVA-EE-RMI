package Controlador;

import DAO.Conector;
import DAO.UtilizadorDao;
import Modelo.*;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    protected static final int SERVER_PORT = 9997;
    protected static final int SERVER_TIMEOUT = 0; //10000;
    protected boolean clienteConectado = false;
    protected List<Conexao> clientesConectados;

    public Server(String ipDB){
        Conector.setIpDB(ipDB);
        clientesConectados = new ArrayList<>();

        try {
            ServerSocket s = new ServerSocket(SERVER_PORT);
            s.setSoTimeout(SERVER_TIMEOUT);
            System.out.println("À espera de clientes...");
            while(true){
                Conexao c = new Conexao (s.accept());
                clientesConectados.add(c); // adiciona cliente à lista de clientes
                clienteConectado = true;
                new Thread(new AtendeCliente(c)).start();
            }

        } catch (BindException e1) {
            System.out.println("Já existe um server aberto: " + e1.getMessage());
            System.exit(1);
        }catch (IOException e) {
            System.out.println("Erro: Criação do ServerSocket");
            e.printStackTrace();
        }
    }

    private void enviarParaTodosOsClientes(MSG msg){
        /*System.out.println("klklkl: " + msg);*/
        for(Conexao cliente : clientesConectados){
            //System.out.println(cliente.getSocket().getPort());

            try {
                cliente.enviar().writeObject(msg);
                cliente.enviar().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class AtendeCliente implements Runnable {
        Conexao c; // Socket connection to the client
        ObjectInputStream in;
        ObjectOutputStream out;
        MSG msg;

        public AtendeCliente(Conexao c){
            this.c = c;
            try {
                c.iniciarInputStream();
                c.iniciarOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            System.out.println("Conectado a partir de " + c.getSocket().getInetAddress());
            try{
                while(true){
                    //in = new ObjectInputStream(s.getInputStream());
                    msg = (MSG) c.receber().readObject(); // Fica à espera de receber um objeto do cliente
                    if(msg.getTipo() != Constantes.TIPOS.SAIR)
                        processaMsg(msg);
                    else{
                        clientesConectados.remove(c);
                        desautenticarUtilizador((String) msg.getObj());
                        break;
                    }

                    System.out.println("Mensagem Enviada!");
                    //in.close();
                    //s.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally{
                    c.fecharSocket();
            }
        }

        private void processaMsg(MSG msg) {

            switch(msg.getTipo()){
                case AUTH:
                    processaAuth(msg);
                    break;
                case GET_USER_LIST:
                    processaUserList();
                    break;
                case GET_MENSAGEM:
                    enviarMensagemParaUmCliente(msg);
                default:
                    break;
            }
        }


        private void enviarMensagemParaUmCliente(MSG msg) {
            Mensagem mensagem = (Mensagem)  msg.getObj();
            int portaTCPDestino = UtilizadorDao.getClienteTCP(mensagem.getDestino());
            System.out.println("A ENVIAR PARA DE: " + mensagem.getOrigem() + "PARA: " + mensagem.getDestino() + " " + portaTCPDestino + " - " + mensagem.getMensagemString());

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(mensagem.getOrigem());
            stringBuilder.append(" ");
            stringBuilder.append(mensagem.getMensagemString());

            String finalString = stringBuilder.toString();

            for(Conexao cliente : clientesConectados){
               if(cliente.getSocket().getPort() == portaTCPDestino){
                  try {
                       cliente.enviar().writeObject(new MSG(Constantes.TIPOS.GET_MENSAGEM_REPLY, finalString));
                       cliente.enviar().flush();
                   } catch (IOException e) {
                      e.printStackTrace();
                    }
                }
            }
        }


        private void processaUserList() {
            List<Utilizador> lista= UtilizadorDao.recuperarTodosOsUtilizadores();
            enviarParaTodosOsClientes(new MSG(Constantes.TIPOS.GET_USER_LIST_REPLY, lista));
        }

        private void processaAuth(MSG msg) {
            Auth auth = (Auth) msg.getObj();

            Boolean valido;

            if(UtilizadorDao.validaLogin(auth.getUsername(), auth.getPassword())) {
                // Se o login é válido
                valido = true;
                autenticarUtilizador(auth);
            }else{
                // Se o lógin é inválido
                valido = false;
            }
            enviarParaCliente(new MSG(Constantes.TIPOS.AUTH_REPLY, new Boolean(valido))); // Envia a resposta de volta para o cliente
        }

        private void autenticarUtilizador(Auth auth) {
            UtilizadorDao.autenticarUtilizador(auth);
            processaUserList();
        }

        private void desautenticarUtilizador(String username) {
            UtilizadorDao.desautenticarUtilizador(username);
            processaUserList();
        }

        public void enviarParaCliente(MSG msg){
            try {
                //out = new ObjectOutputStream(s.getOutputStream()); // stream de saída
                c.enviar().writeObject(msg);
                c.enviar().flush();
                //out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
