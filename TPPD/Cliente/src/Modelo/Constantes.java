package Modelo;

import javax.swing.*;
import java.io.Serializable;
import java.net.URL;

public class Constantes implements Serializable {
    static final long serialVersionUID = 100L;
    private Constantes(){}

    /* MENSAGEM_TIPO DE MENSAGEM */
    public enum MENSAGEM_TIPO {
        AUTH,
        AUTH_REPLY,
        GET_USER_LIST,
        GET_USER_LIST_REPLY,
        BEGIN_CHAT,
        BEGIN_CHAT_REPLY,
        SEND_PRIVATE_CHAT_MESSAGE,
        NEW_PRIVATE_CHAT_MESSAGE,
        SAIR
    }

    /* HashMap Keys for users */
    public enum USER_MAP {
        USERNAME,
        STATUS
    }

    public enum USER_STATUS {
        ATIVO,
        INATIVO
    }

    /* SEND_PRIVATE_CHAT_MESSAGE */
    public static final String MENSAGEM_TEXTO = "MENSAGEM_TEXTO";
    public static final String MENSAGEM_REMETENTE = "MENSAGEM_REMETENTE";
    public static final String MENSAGEM_CHAT_PRIVADO = "MENSAGEM_CHAT_PRIVADO";


    /* ICONS */
    public enum RECURSOS {
        ICON_ONLINE("Recursos/icon_online.png"),
        ICON_OFFLINE("Recursos/icon_offline.png");

        private final String conteudo;

        RECURSOS(String s) {
            this.conteudo = s;
        }

        @Override
        public String toString(){
            return conteudo;
        }
    }

    public  ImageIcon ICON_ONLINE = new ImageIcon(Constantes.class.getResource(RECURSOS.ICON_ONLINE.toString()));
    public  ImageIcon ICON_OFFLINE = new ImageIcon(Constantes.class.getResource(RECURSOS.ICON_OFFLINE.toString()));
}

