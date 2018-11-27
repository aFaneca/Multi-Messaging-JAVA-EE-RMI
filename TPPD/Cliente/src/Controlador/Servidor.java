package Controlador;

import java.io.*;
import java.net.Socket;

public class Servidor {
    protected Socket s;
    protected String serverName;
    protected int serverPort;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;

    public Servidor(String serverName, int serverPort) {

        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public boolean testaConexão(){
        try{
            s = new Socket(this.serverName, this.serverPort);
            s.close();
            return true;
        }catch(IOException e) {
            return false;
        }
    }

    public void conectar(){
        try{
            s = new Socket(this.serverName, this.serverPort);
            new Thread (new EscutaServidor(s)).start();
            in = new ObjectInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void fecharConexao(){
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* Getters & Setters */
    public Socket getSocket() {
        return s;
    }

    public void setSocket(Socket s) {
        this.s = s;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    private class EscutaServidor implements Runnable{
        private Socket s;

        public EscutaServidor(Socket s){
            this.s = s;
        }

        @Override
        public void run() {
            while(true){
                // Lê mensagens que recebe do servidor e interpreta-as
            }
        }
    }
}
