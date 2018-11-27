package Controlador;

import java.io.IOException;
import java.net.Socket;

public class Servidor {
    protected Socket s;
    protected String serverName;
    protected int serverPort;


    public Servidor(String serverName, int serverPort) {

        this.serverName = serverName;
        this.serverPort = serverPort;
    }

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

    public boolean testaConexão(){

        try{
            s = new Socket(this.serverName, this.serverPort);
            s.close();
            return true;
        }catch(IOException e) {
            return false;
        }
    }
}
