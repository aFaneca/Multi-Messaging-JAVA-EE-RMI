package Modelo;

import java.io.Serializable;

public class Mensagem implements Serializable {
    static final long serialVersionUID = 102020L;

    private Utilizador fromUser;
    /*private Utilizador toUser;*/
    private String mensagem;


    public Mensagem(Utilizador fromUser, String mensagem) {
        this.fromUser = fromUser;
        this.mensagem = mensagem;
    }


    /* Getters & Setters */

    public Utilizador getFromUser() {
        return fromUser;
    }


    public String getMensagem() {
        return mensagem;
    }
}
