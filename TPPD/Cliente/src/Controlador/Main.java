package Controlador;

import java.io.*;
import java.net.Socket;

public class Main {
    Socket s = null;

    public Main(){

        try {
            s = new Socket("localhost", 9997);
            new Thread(new EscutaServidor(s)).start();
            DataOutputStream dout = new DataOutputStream(s.getOutputStream()); // converts data into the streams
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // take input from the user

            while(true){
                String so = br.readLine();
                dout.writeUTF(so);

                if(so.equalsIgnoreCase("exit")) break;
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        new Main();
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
    }

