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


    /*public class TIPOS{
        public static final String AUTH = "AUTH";
        public static final String SAIR = "SAIR";
    }*/
}

