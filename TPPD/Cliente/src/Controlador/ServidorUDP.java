package Controlador;

import GUI.ChatView;
import Modelo.ChatPrivado;
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
                    processaMsg(msg);
                    iStream.close();


                    System.out.println("NOVA MENSAGEM DO SERVIDOR: " + c.getServer().listaDeUtilizadores);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processaMsg(MSG msg) {
            switch(msg.getTipo()){
                case GET_USER_LIST_REPLY:
                    recebeListaDeUtilizadores(msg);
                    break;
                case BEGIN_CHAT_REPLY:
                    recebeChatPrivado(msg);
                    break;
                case NEW_PRIVATE_CHAT_MESSAGE:
                    recebeNovaMensagemChatPrivado(msg);
                    break;
                default:
                    break;
            }
        }


        private void recebeChatPrivado(MSG msg) {
            ChatPrivado cv = (ChatPrivado) msg.getObj();
            boolean jaExiste = c.getServer().listaDeChatsPrivados.contains(cv);
            boolean isForMe = false;

            for(Utilizador u : cv.getUsers()) {
                if (u.getUsername().equalsIgnoreCase(c.getServer().getUsername()))
                    isForMe = true;
            }

            if(!isForMe) return;
            //System.out.println("IS FOR ME");
            if(jaExiste){
                // Janela já está aberta, atualizar só as mensagens
                for(ChatView v : c.getServer().listaDeChatViews){
                    if(v.getChatPrivado().equals(cv)){
                        v.setVisible(true);
                        break;
                    }
                }
            }else{
                // Abrir janela de chat
                String userDestino = "N/D";

                for(Utilizador u : cv.getUsers())
                    if(!u.getUsername().equalsIgnoreCase(c.getServer().getUsername()))
                        userDestino = u.getUsername();
                ChatView cv2 = new ChatView(c.getServer().getUsername(), userDestino, c, cv);
                c.getServer().addObserver(cv2);
                c.getServer().listaDeChatViews.add(cv2);

                // Adicionar à lista de chats
                c.getServer().listaDeChatsPrivados.add(cv);
            }
        }


        private void recebeNovaMensagemChatPrivado(MSG msg) {
            ChatPrivado cv = (ChatPrivado) msg.getObj();
            //boolean jaExiste = listaDeChatsPrivados.contains(cv);
            boolean isForMe = false;

            for(Utilizador u : cv.getUsers()) {
                if (u.getUsername().equalsIgnoreCase(c.getServer().getUsername()))
                    isForMe = true;
            }

            if(!isForMe) return;

            // Janela já está aberta, atualizar só as mensagens
            for(ChatPrivado cp1 : c.getServer().listaDeChatsPrivados){
                List<Utilizador> cp1Users = cp1.getUsers();
                List<Utilizador> cvUsers = cv.getUsers();
                if(cp1Users.containsAll(cvUsers) &&  cvUsers.containsAll(cp1Users))
                    //cp1 = cv;
                   c.getServer().listaDeChatsPrivados.set(c.getServer().listaDeChatsPrivados.indexOf(cp1), cv);
            }

            c.getServer().notificaObservadores();
        }


        private void recebeListaDeUtilizadores(MSG msg){
            List<Utilizador> listaDeUtilizadores = (ArrayList) msg.getObj();
            atualizaListaDeUtilizadores(listaDeUtilizadores);
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
