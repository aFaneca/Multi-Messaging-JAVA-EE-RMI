package Modelo;

import java.io.Serializable;

public class Constantes implements Serializable {
    static final long serialVersionUID = 100L;
    private Constantes(){}

    // TIPOS DE MENSAGEM
    public enum TIPOS {
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

    /* SEND_PRIVATE_CHAT_MESSAGE */
    public static final String MENSAGEM_TEXTO = "MENSAGEM_TEXTO";
    public static final String MENSAGEM_REMETENTE = "MENSAGEM_REMETENTE";
    public static final String MENSAGEM_CHAT_PRIVADO = "MENSAGEM_CHAT_PRIVADO";

    /*public class TIPOS{
        public static final String AUTH = "AUTH";
        public static final String SAIR = "SAIR";
    }*/
}

