package Controlador;

import DAO.Conector;
import DAO.UtilizadorDao;
import Modelo.*;
import RMI.ServicoObsInterfaceServer;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server {
    protected static final int SERVER_PORT = 0;
    protected static final int SERVER_TIMEOUT = 0; //10000;
    protected boolean clienteConectado = false;
    protected List<Conexao> clientesConectados;
    private List<Utilizador> utilizadoresAtivos;
    private List<String> usernamesAtivos;
    protected ServicoObsInterfaceServer servicoObsServer;

    public Server(String ipDB){
        Conector.setIpDB(ipDB);
        clientesConectados = new ArrayList<>();

        try {
            servicoObsServer = new ServicoObsInterfaceServer(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            ServerSocket s = new ServerSocket(SERVER_PORT);
            s.setSoTimeout(SERVER_TIMEOUT);
            System.out.println("Ligado à porta " + s.getLocalPort() + ". À espera de clientes...");
            while(true){
                Conexao c = new Conexao (s.accept());
                clientesConectados.add(c); // adiciona cliente à lista de clientes
                clienteConectado = true;
                new Thread(new AtendeCliente(c)).start();
            }

        } catch (BindException e1) {
            System.out.println("Já existe um server aberto: " + e1.getMessage());
            System.exit(1);
        }catch (IOException e) {
            System.out.println("Erro: Criação do ServerSocket");
            e.printStackTrace();
        }
    }

    private void enviarParaTodosOsClientes(MSG msg){

        for(int i = 0; i < clientesConectados.size(); i++){
            try {
                clientesConectados.get(i).enviar().writeObject(msg);
                clientesConectados.get(i).enviar().flush();
            }catch (Exception e) {
                //UtilizadorDao.desautenticarUtilizador(clientesConectados.get(i).getUtilizador().getUsername());
                clientesConectados.remove(clientesConectados.get(i)); i--;
                //e.printStackTrace();
            }
        }
    }

    private void enviarParaClientesExternos(MSG msg){

        List<Utilizador> todosOsUtilizadoresAtivos = null;
        List<Utilizador> utilizadoresExternos = new ArrayList<>();

        todosOsUtilizadoresAtivos = UtilizadorDao.recuperarTodosOsUtilizadoresAtivos();

        for(Utilizador u : todosOsUtilizadoresAtivos){
            if(!utilizadoresAtivos.contains(u)){
                utilizadoresExternos.add(u);
            }
        }

        if(utilizadoresExternos.isEmpty()) return;

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();

            for(Utilizador u1 : utilizadoresExternos){
                enviarObjPorUDP(ds, InetAddress.getByName(u1.getEnderecoIP()), u1.getPortaUDP(), msg);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarObjPorUDP(DatagramSocket ds, InetAddress endereco, int portaUDP, MSG msg) throws IOException {
        // Serializar o objeto
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(msg);
        oo.close();

        byte[] msgSerializada = bStream.toByteArray();
        DatagramPacket dp = new DatagramPacket(msgSerializada, msgSerializada.length, endereco, portaUDP); // o pacote que é enviado (dados a enviar (em bytes), tamanho dos dados, IP ADDRESS, PORT NUMBER
        ds.send(dp);
        System.out.println("Enviei mensagem para " + dp.getSocketAddress());

        /*int i = 8;
        byte[] b = String.valueOf(i).getBytes(); // O getbytes só funciona na conversão de strings, daí termos que converter i para string

        DatagramPacket dp = new DatagramPacket(b, b.length, endereco, portaUDP); // o pacote que é enviado (dados a enviar (em bytes), tamanho dos dados, IP ADDRESS, PORT NUMBER

        ds.send(dp);
        System.out.println("Enviei mensagem para " + dp.getSocketAddress());*/
    }

    public class AtendeCliente implements Runnable {
        Conexao c; // Socket connection to the client
        ObjectInputStream in;
        ObjectOutputStream out;
        MSG msg;
        boolean podeLer = true;

        public AtendeCliente(Conexao c){
            this.c = c;
            try {
                c.iniciarInputStream();
                c.iniciarOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void run(){
            System.out.println("Conectado a partir de " + c.getSocket().getRemoteSocketAddress());
            KeepAliveThread keepAlive = new KeepAliveThread(c, this);
            new Thread(keepAlive).start();
            try{
                while(true){
                    try{
                        msg = (MSG) c.receber().readObject(); // Fica à espera de receber um objeto do cliente
                    }catch(Exception e){
                        continue;
                    }

                    if(msg.getTipo() != Constantes.MENSAGEM_TIPO.SAIR)
                        processaMsg(msg);
                    else{
                        clientesConectados.remove(c);
                        desautenticarUtilizador((String) msg.getObj());
                        break;
                    }
                }

            }finally{
                    c.fecharSocket();
            }
        }

        private void processaMsg(MSG msg) {

            switch(msg.getTipo()){
                case AUTH:
                    processaAuth(msg);
                    break;
                case GET_USER_LIST:
                    processaUserList();
                    break;
                case BEGIN_CHAT:
                    processaChat(msg);
                    break;
                case SEND_PRIVATE_CHAT_MESSAGE:
                    processaNovaMensagemChat(msg);
                    break;
                default:
                    break;
            }
        }

        private void processaNovaMensagemChat(MSG msg){
            ChatPrivado cp = null;
            String mensagem = "";
            String username = "";
            Utilizador utilizadorRemetente = null;
            String data = "";
            /*List<Object> objs = (ArrayList) msg.getObj();



            for(Object o : objs) {
                if (o instanceof String)
                    mensagem = (String) o;
                else
                    cp = (ChatPrivado) o;
            }
            cp.addMessage(new Mensagem(null, mensagem, getDate()));*/

            Map<String, Object> obj = (HashMap) msg.getObj();
            cp = (ChatPrivado) obj.get(Constantes.MENSAGEM_CHAT_PRIVADO);
            mensagem = (String) obj.get(Constantes.MENSAGEM_TEXTO);
            username = (String) obj.get(Constantes.MENSAGEM_REMETENTE);
            utilizadorRemetente = UtilizadorDao.recuperar(username);
            data = getDate();

            cp.addMessage(new Mensagem(utilizadorRemetente, mensagem, data));

            ChatPrivado cp2 = cp;
            enviarParaTodosOsClientes(new MSG(Constantes.MENSAGEM_TIPO.NEW_PRIVATE_CHAT_MESSAGE, cp));
            enviarParaClientesExternos(new MSG(Constantes.MENSAGEM_TIPO.NEW_PRIVATE_CHAT_MESSAGE, cp));
        }

        private void processaChat(MSG msg){
            List<String> usernames = (ArrayList) msg.getObj();
            List<Utilizador> users = new ArrayList<>();

            for(String u : usernames){
                users.add(UtilizadorDao.recuperar(u));
            }

            ChatPrivado cp = new ChatPrivado(users);
            enviarParaTodosOsClientes(new MSG(Constantes.MENSAGEM_TIPO.BEGIN_CHAT_REPLY, cp));
            enviarParaClientesExternos(new MSG(Constantes.MENSAGEM_TIPO.BEGIN_CHAT_REPLY, cp));
        }

        private void processaUserList() {
            getUtilizadoresAtivos(); // Recria a lista de utilizadores ativos
            List<Utilizador> lista= UtilizadorDao.recuperarTodosOsUtilizadores();
            enviarParaTodosOsClientes(new MSG(Constantes.MENSAGEM_TIPO.GET_USER_LIST_REPLY, lista)); // enviar para clientes deste servidor (TCP)
            enviarParaClientesExternos(new MSG(Constantes.MENSAGEM_TIPO.GET_USER_LIST_REPLY, lista)); // enviar para clientes de outros servidores (UDP)
        }

        private void processaAuth(MSG msg) {
            Auth auth = (Auth) msg.getObj();

            Boolean valido;

            if(UtilizadorDao.validaLogin(auth.getUsername(), auth.getPassword())) {
                // Se o login é válido
                valido = true;
                autenticarUtilizador(auth);
            }else{
                // Se o lógin é inválido
                valido = false;
            }
            enviarParaCliente(new MSG(Constantes.MENSAGEM_TIPO.AUTH_REPLY, new Boolean(valido))); // Envia a resposta de volta para o cliente
            //
        }

        private void autenticarUtilizador(Auth auth) {
            UtilizadorDao.autenticarUtilizador(auth);
            c.associarUtilizador(UtilizadorDao.recuperar(auth.getUsername()));
            processaUserList();
            servicoObsServer.notificaListeners();
        }

        public void desautenticarUtilizador(String username) {
            UtilizadorDao.desautenticarUtilizador(username);
            processaUserList();
            servicoObsServer.notificaListeners();
            System.out.println("User " + username + " desautenticado.");
        }

        public void enviarParaCliente(MSG msg){
            try {
                //out = new ObjectOutputStream(s.getOutputStream()); // stream de saída
                c.enviar().writeObject(msg);
                c.enviar().reset();
                c.enviar().flush();
                //out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getDate() {
        return new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public List<Conexao> getClientesConectados() {
        return clientesConectados;
    }

    public List<Utilizador> getUtilizadoresAtivos(){
        utilizadoresAtivos = new ArrayList<>();
        for(Conexao c : clientesConectados){
            utilizadoresAtivos.add(c.getUtilizador());
        }

        return utilizadoresAtivos;
    }

    public List<String> getUsernamesAtivos(){
        usernamesAtivos = new ArrayList<>();
        for(Conexao c : clientesConectados){
            usernamesAtivos.add(c.getUtilizador().getUsername());
        }

        return usernamesAtivos;
    }
}
