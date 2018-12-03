package Controlador;

import Modelo.Constantes;
import Modelo.MSG;
import Modelo.Utilizador;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Servidor extends Observable{
    protected Socket s;
    protected String serverName;
    protected int serverPort;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;
    protected List<Utilizador> listaDeUtilizadores;
    protected String username;

    public Servidor(String serverName, int serverPort) {

        this.serverName = serverName;
        this.serverPort = serverPort;
        this.listaDeUtilizadores = new ArrayList<Utilizador>();
    }

    private void notificaObservadores(){
        setChanged();
        notifyObservers(this);
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
        new Thread (new EscutaServidor(s)).start(); // dá início á thread que vai passar a ler ciclicamente novas mensagens recebidas
        return msg;
    }


    public void fecharConexao(){
        try {
            if(s.isConnected())
                s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected class EscutaServidor implements Runnable{
        private Socket s;

        public EscutaServidor(Socket s){
            this.s = s;
        }

        @Override
        public void run() {
            while(true){
                System.out.println("1");
                // Lê mensagens que recebe do servidor e interpreta-as
                MSG msg = null;
                try {
                    msg = (MSG) in.readObject();
                    processaMsg(msg);
                } catch (IOException e) {
                    System.out.println("Ligação terminada com o servidor.");
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }

            }
        }

        private void processaMsg(MSG msg) {
            switch(msg.getTipo()){
                case GET_USER_LIST_REPLY:
                    recebeListaDeUtilizadores(msg);
                    break;
                default: break;
            }
        }

        private void recebeListaDeUtilizadores(MSG msg) {


            listaDeUtilizadores = (List<Utilizador>) msg.getObj();

            for(Utilizador u : listaDeUtilizadores)
                System.out.println(u.getUsername() + " - " + u.getEstado());
            notificaObservadores();
            
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

    public List<Utilizador> getListaDeUtilizadores() {
        return listaDeUtilizadores;
    }

    public List<String> getListaDeUsernames() {
        List<String> lista = new ArrayList<>();

        for(Utilizador u : listaDeUtilizadores){
            lista.add(u.getUsername() + " [ " + u.getEstado() + " ]");
        }

        return lista;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String utilizador) {
        this.username = utilizador;
    }
}
