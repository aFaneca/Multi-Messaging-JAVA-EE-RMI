package Controlador;

import Modelo.MSG;
import Modelo.Utilizador;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ServidorUDP {
    protected Controlador c;
    protected DatagramSocket socket;
    protected int serverPort;


    public ServidorUDP(Controlador c) {
        this.c = c;
    }


    public void iniciarServidorUDP(){
        try {
            socket = new DatagramSocket(0);
            serverPort = socket.getLocalPort();
            System.out.println("Servidor UDP à escuta em " + socket.getLocalSocketAddress() + " - " + serverPort);
            new Thread(new EscutaUDP()).start();
        } catch (SocketException e) {
            System.out.println("Erro a criar o datagram socket");
            e.printStackTrace();
        }
    }


    private class EscutaUDP implements Runnable{

        @Override
        public void run() {
            System.out.println("[Servidor UDP] À espera de mensagens...");

            while(true){
                byte[] b1 = new byte[4096];
                try {
                    DatagramPacket dp = new DatagramPacket(b1, b1.length);
                    socket.receive(dp);
                    System.out.println("RECEBIIII UDP!");
                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(b1));
                    MSG msg = (MSG) iStream.readObject();
                    iStream.close();
                    List<Utilizador> listaDeUtilizadores = (ArrayList) msg.getObj();
                    atualizaListaDeUtilizadores(listaDeUtilizadores);

                    System.out.println("NOVA MENSAGEM DO SERVIDOR: " + listaDeUtilizadores);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        private void atualizaListaDeUtilizadores(List<Utilizador> lista) {
            c.getServer().listaDeUtilizadores = (List<Utilizador>)lista;

            /* Transformar a ArrayList num HashMap <Username, Status> */
            for(Utilizador u : c.getServer().listaDeUtilizadores){
                c.getServer().mapaDeUtilizadores.put(u.getUsername(), u.getEstado());
            }
            c.getServer().notificaObservadores();
        }
    }


}
