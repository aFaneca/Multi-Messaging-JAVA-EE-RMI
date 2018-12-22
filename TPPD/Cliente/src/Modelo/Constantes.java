package Modelo;

import java.io.Serializable;

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
        KEEP_ALIVE,
        KEEP_ALIVE_REPLY,
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


    /* RMI */
    public static final String RMI_NOME_SERVICO = "ObservacaoSistema";


    /* KEEP ALIVE */
    public static final int KEEP_ALIVE_TIME_INTERVAL = 30000; // ms
    public static final int KEEP_ALIVE_LIMITE_FALHAS = 3;
    public static final int KEEP_ALIVE_TIMEOUT_LIMIT = 5000; // ms

}

