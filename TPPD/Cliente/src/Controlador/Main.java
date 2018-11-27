package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class Main implements Observable{
    Socket s = null;

    public Main(){
        Controlador c = new Controlador(this);
        //acedeAoServer();
    }

    public static void main(String args[]){
        new Main();
    }

    public void acedeAoServer() {

        try {
            s = new Socket("localhost", 9997);
            new Thread(new EscutaServidor(s)).start();
            DataOutputStream dout = new DataOutputStream(s.getOutputStream()); // converts data into the streams
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // take input from the user
            System.out.println("Hello baby");

            while(true){
                String so = br.readLine();
                dout.writeUTF(so);
                System.out.println("Hello baby");
                if(so.equalsIgnoreCase("exit"))
                    break;
            }
            s.close();
            System.out.println("Hello baby");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EscutaServidor implements Runnable{
        private Socket s;

        public EscutaServidor(Socket s){
            this.s = s;
        }

        @Override
        public void run() {
            System.out.println("No aguardo por mensagens...");

            try {
                DataInputStream din = new DataInputStream(s.getInputStream());
                while(true){
                    System.out.println("RECEBIDO: " + din.readUTF());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

