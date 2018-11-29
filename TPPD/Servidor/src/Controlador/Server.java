package Controlador;

import DAO.Conector;
import DAO.UtilizadorDao;
import Modelo.Auth;
import Modelo.Constantes;
import Modelo.MSG;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class Server {
    protected static final int SERVER_PORT = 9997;
    protected static final int SERVER_TIMEOUT = 0; //10000;
    protected boolean clienteConectado = false;
    protected List<Socket> clientesConectados;
    public Server(String ipDB){
        Conector.setIpDB(ipDB);
        clientesConectados = new ArrayList<>();

        try {
            ServerSocket s = new ServerSocket(SERVER_PORT);
            s.setSoTimeout(SERVER_TIMEOUT);
            System.out.println("À espera de clientes...");
            while(true){
                if(clienteConectado)
                    if(Thread.activeCount() < 2) break; // se não houver nenhum cliente ativo, sair

                Socket ss = s.accept();
                clientesConectados.add(ss); // adiciona cliente à lista de clientes
                clienteConectado = true;
                new Thread(new AtendeCliente(ss)).start();
            }
            System.out.println("A desligar...");
        } catch (IOException e) {
            System.out.println("Erro: Criação do ServerSocket");
            e.printStackTrace();
        }
    }

    private void encaminharParaTodosOsClientes(String msg){
        /*System.out.println("klklkl: " + msg);*/
        for(Socket cliente : clientesConectados){
            try {
                DataOutputStream dout = new DataOutputStream(cliente.getOutputStream());
                //PrintWriter dout = new PrintWriter(cliente.getOutputStream());
                dout.writeUTF(msg);
                dout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class AtendeCliente implements Runnable {
        Socket s; // Socket connection to the client
        ObjectInputStream in;
        ObjectOutputStream out;
        MSG msg;

        public AtendeCliente(Socket s){
            this.s = s;
            try {
                in = new ObjectInputStream(s.getInputStream());
                out = new ObjectOutputStream(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void run(){
            System.out.println("Conectado a partir de " + s.getInetAddress());
            try{
                while(true){
                    //in = new ObjectInputStream(s.getInputStream());
                    msg = (MSG) in.readObject(); // Fica à espera de receber um objeto do cliente
                    processaMsg(msg);
                    System.out.println("Mensagem Enviada!");
                    //in.close();
                    //s.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally{
                clientesConectados.remove(s);
            }

        }

        private void processaMsg(MSG msg) {

            switch(msg.getTipo()){
                case AUTH:
                    processaAuth(msg);
                    break;
                default:
                    break;
            }

        }

        private void processaAuth(MSG msg) {
            Auth auth = (Auth) msg.getObj();
            Boolean valido;

            if(UtilizadorDao.validaLogin(auth.getUsername(), auth.getPassword())) {
                // Se o login é válido
                valido = true;
            }else{
                // Se o lógin é inválido
                valido = false;
            }
            enviarParaCliente(new MSG(Constantes.TIPOS.AUTH_REPLY, new Boolean(valido))); // Envia a resposta de volta para o cliente
        }

        public void enviarParaCliente(MSG msg){
            try {
                //out = new ObjectOutputStream(s.getOutputStream()); // stream de saída
                out.writeObject(msg);
                out.flush();
                //out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
