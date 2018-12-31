package Controlador;

import GUI.ChatView;
import Modelo.ChatPrivado;
import Modelo.Constantes;
import Modelo.MSG;
import Modelo.Utilizador;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Servidor extends Observable{
    protected Socket s;
    protected String serverName;
    protected int serverPort;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;
    protected List<Utilizador> listaDeUtilizadores;
    protected Map<String, String> mapaDeUtilizadores;
    protected List<ChatPrivado> listaDeChatsPrivados;
    protected List<ChatView> listaDeChatViews;
    protected String username;
    Controlador c;

    public Servidor(Controlador c, String serverName, int serverPort) {
        this.c = c;
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.listaDeUtilizadores = new ArrayList<Utilizador>();
        this.mapaDeUtilizadores = new HashMap<>();
        this.listaDeChatsPrivados = new ArrayList<>();
        this.listaDeChatViews = new ArrayList<>();
    }

    protected void notificaObservadores(){
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
            out.reset();
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
    public MSG receberDoServidor(Constantes.MENSAGEM_TIPO tipo) throws IOException, ClassNotFoundException {
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

    public ChatPrivado getChatPrivado(String user, String userDestino) {
        ChatPrivado cp = null;
        int match = 0;

        for(ChatPrivado c : listaDeChatsPrivados){
            for(Utilizador u : c.getUsers()){
                if(u.getUsername().equalsIgnoreCase(user) || u.getUsername().equalsIgnoreCase(userDestino))
                    match++;
            }
            if(match == 2){
                cp = c;
                break;
            }else match = 0;
        }

        return cp;
    }


    public class EscutaServidor implements Runnable{
        private Socket s;

        public EscutaServidor(Socket s){
            this.s = s;
        }

        @Override
        public void run() {
            while(true){
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
                case BEGIN_CHAT_REPLY:
                    recebeChatPrivado(msg);
                    break;
                case NEW_PRIVATE_CHAT_MESSAGE:
                    recebeNovaMensagemChatPrivado(msg);
                    break;
                case KEEP_ALIVE:
                    System.out.println("Recebi pedido KEEPALIVE.");
                    recebePedidoKeepAlive();
                    break;
                default: break;
            }
        }

        private void recebePedidoKeepAlive() {

            try {
                //out = new ObjectOutputStream(s.getOutputStream()); // stream de saída
                out.writeObject(Constantes.MENSAGEM_TIPO.KEEP_ALIVE_REPLY.toString());
                out.reset();
                out.flush();

                //out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void recebeNovaMensagemChatPrivado(MSG msg) {
            ChatPrivado cv = (ChatPrivado) msg.getObj();
            //boolean jaExiste = listaDeChatsPrivados.contains(cv);
            boolean isForMe = false;

            for(Utilizador u : cv.getUsers()) {
                if (u.getUsername().equalsIgnoreCase(getUsername()))
                    isForMe = true;
            }

            if(!isForMe) return;

            // Janela já está aberta, atualizar só as mensagens
            for(ChatPrivado cp1 : listaDeChatsPrivados){
                List<Utilizador> cp1Users = cp1.getUsers();
                List<Utilizador> cvUsers = cv.getUsers();
                if(cp1Users.containsAll(cvUsers) &&  cvUsers.containsAll(cp1Users))
                    //cp1 = cv;
                    listaDeChatsPrivados.set(listaDeChatsPrivados.indexOf(cp1), cv);
            }


            notificaObservadores();
        }

        private void recebeChatPrivado(MSG msg) {
            ChatPrivado cv = (ChatPrivado) msg.getObj();
            boolean jaExiste = listaDeChatsPrivados.contains(cv);
            boolean isForMe = false;
            //System.out.println("Recebi chat privado");
            //System.out.println("MEU USERNAME: " + getUsername());
            for(Utilizador u : cv.getUsers()) {
                if (u.getUsername().equalsIgnoreCase(getUsername()))
                    isForMe = true;
            }

            if(!isForMe) return;
            //System.out.println("IS FOR ME");
            if(jaExiste){
                // Janela já está aberta, atualizar só as mensagens
                for(ChatView v : listaDeChatViews){
                    if(v.getChatPrivado().equals(cv)){
                        v.setVisible(true);
                        break;
                    }
                }
            }else{
                // Abrir janela de chat
                String userDestino = "N/D";

                for(Utilizador u : cv.getUsers())
                    if(!u.getUsername().equalsIgnoreCase(getUsername()))
                        userDestino = u.getUsername();
                ChatView cv2 = new ChatView(getUsername(), userDestino, c, cv);
                addObserver(cv2);
                listaDeChatViews.add(cv2);

                // Adicionar à lista de chats
                listaDeChatsPrivados.add(cv);
            }

        }

        public void recebeListaDeUtilizadores(MSG msg) {


            listaDeUtilizadores = (List<Utilizador>) msg.getObj();

            /* Transformar a ArrayList num HashMap <Username, Status> */
            for(Utilizador u : listaDeUtilizadores){
                mapaDeUtilizadores.put(u.getUsername(), u.getEstado());
            }

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

    public Map<String, String> getMapaDeUtilizadores() {
        return mapaDeUtilizadores;
    }

    public void setListaDeUtilizadores(List<Utilizador> listaDeUtilizadores) {
        this.listaDeUtilizadores = listaDeUtilizadores;
    }

    public void setMapaDeUtilizadores(Map<String, String> mapaDeUtilizadores) {
        this.mapaDeUtilizadores = mapaDeUtilizadores;
    }
}
