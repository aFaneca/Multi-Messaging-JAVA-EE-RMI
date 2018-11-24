package Controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class Server {
    protected static final int SERVER_PORT = 9997;
    protected static final int SERVER_TIMEOUT = 0; //10000;
    protected boolean clienteConectado = false;
    protected List<Socket> clientesConectados;
    public Server(){
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

        public AtendeCliente(Socket s){
            this.s = s;
        }


        public void run(){
            System.out.println("Conectado a partir de " + s.getInetAddress());
            try{
                DataInputStream din = new DataInputStream(s.getInputStream());
                while(true){
                    String yoo = din.readUTF();

                    encaminharParaTodosOsClientes(yoo);
                    System.out.println("cliente: " + yoo + " | Clientes Ativos: " + clientesConectados.size());
                    if(yoo.equalsIgnoreCase("exit")) break;
                }
                clientesConectados.remove(s);
                s.close();
            }catch(Exception e){}

        }
    }
}
