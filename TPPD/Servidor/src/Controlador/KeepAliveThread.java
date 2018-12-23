package Controlador;

import DAO.UtilizadorDao;
import Modelo.Constantes;
import Modelo.MSG;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class KeepAliveThread implements Runnable{
    protected Conexao conexao;
    protected Server.AtendeCliente utils;
    boolean podeLer = false;

    public KeepAliveThread(Conexao conexao, Server.AtendeCliente utils) {
        this.conexao = conexao;
        this.utils = utils;
    }

    public int enviarParaCliente(MSG msg){
        try {
            //out = new ObjectOutputStream(s.getOutputStream()); // stream de saída
            conexao.enviar().writeObject(msg);
            conexao.enviar().reset();
            conexao.enviar().flush();
            //out.close();
        }catch( SocketException e) {
            //System.out.println("Erro a escrever no socket.");
            return -1;
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){

        }

        return 1;
    }

    public int receberDoServidor(Constantes.MENSAGEM_TIPO tipo) throws IOException, ClassNotFoundException {
        String msg;
        conexao.getSocket().setSoTimeout(Constantes.KEEP_ALIVE_TIMEOUT_LIMIT);
        while(true){
            if(podeLer){
                try{
                    msg = (String) conexao.receber().readObject();
                    if(msg.equals(tipo.toString())) break;
                }catch(SocketTimeoutException e){
                    // TIMEOUT SEM RESPOSTA
                    System.out.println("TIMEOUT. A conexão " + conexao.getUtilizador().getUsername() + "(" + conexao.getSocket().getRemoteSocketAddress() + " não respondeu ao pedido.");
                    conexao.getSocket().setSoTimeout(0); // Colocar o timeout em 0 novamente
                    return -1;
                }catch(Exception e){
                    //receberDoServidor(tipo);
                }
            }

        }
        conexao.getSocket().setSoTimeout(0); // Colocar o timeout em 0 novamente
        return 1;
    }


    @Override
    public void run() {
        while(true){
            try {
                if(conexao.getUtilizador() != null){
                    if(UtilizadorDao.getNrFalhas(conexao.getUtilizador().getUsername()) >= Constantes.KEEP_ALIVE_LIMITE_FALHAS){
                        utils.desautenticarUtilizador(conexao.getUtilizador().getUsername());

                        //utils.podeLer = true;
                        System.out.println("User " + conexao.getUtilizador().getUsername() + "(" + conexao.getSocket().getRemoteSocketAddress() + ") desautenticado.");
                        conexao.getSocket().close();
                        break;
                    }
                    //utils.podeLer = false;
                    Thread.sleep(Constantes.KEEP_ALIVE_TIME_INTERVAL);

                    if(enviarParaCliente(new MSG(Constantes.MENSAGEM_TIPO.KEEP_ALIVE, null)) == -1){
                        UtilizadorDao.changeNrFalhas(conexao.getUtilizador().getUsername(), +1);
                        System.out.println("Impossível entrar em contato com " + conexao.getUtilizador().getUsername() + "(" + conexao.getSocket().getRemoteSocketAddress() + "). A tentar novamente...");
                        continue;
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                UtilizadorDao.changeNrFalhas(conexao.getUtilizador().getUsername(), +1);
                e.printStackTrace();
            }
        }
    }
}
