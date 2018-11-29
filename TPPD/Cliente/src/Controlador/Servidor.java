package Controlador;

import Modelo.Constantes;
import Modelo.MSG;

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
            //s.close();
            return true;
        }catch(IOException e) {

            return false;
        }
    }

    public void conectar(){
        try{
            //s = new Socket(this.serverName, this.serverPort); // ligação TCP permanente com o server criada aqui
            out = new ObjectOutputStream(s.getOutputStream()); // stream de saída
            in = new ObjectInputStream(s.getInputStream()); // stream de entrada
            new Thread (new EscutaServidor(s)).start();



        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void enviarParaServidor(MSG msg){
        try {
            //out = new ObjectOutputStream(s.getOutputStream()); // stream de saída
            out.writeObject(msg);
            out.flush();
            //out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Bloqueia aqui até que receba um objeto do servidor identificado pelo TIPO requirido (todos os outros são descartados)
    *  Só está planeado utilizar este método antes de a autenticação se realizar. Após isso, é esperado
    *  que novos pedidos sejam lidos pela thread dedicada
    * */
    public MSG receberDoServidor(Constantes.TIPOS tipo) throws IOException, ClassNotFoundException {
        MSG msg;

        while(true){
            msg = (MSG) in.readObject();
            if(msg.getTipo().equals(tipo)) break;
        }

        return msg;
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
