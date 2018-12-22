package Modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Mensagem implements Serializable {
    static final long serialVersionUID = 102020L;

    private Utilizador fromUser;
    /*private Utilizador toUser;*/
    private String mensagem;
    private String data;


    public Mensagem(Utilizador fromUser, String mensagem, String data) {
        this.fromUser = fromUser;
        this.mensagem = mensagem;
        this.data = data;
    }

    private String getDate() {
        return new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());
    }
    /* Getters & Setters */

    public Utilizador getFromUser() {
        return fromUser;
    }

    public String getUsernameRemetente(){
        return fromUser.getUsername();
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getData() {
        return data;
    }
}
