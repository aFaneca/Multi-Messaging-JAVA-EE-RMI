package Modelo;


import java.io.Serializable;

/*
* Modelo padrão para o envio da mensagem do tipo Auth
*
* */
public class Auth implements Serializable {
    static final long serialVersionUID = 10L;
    private String username;
    private String password;

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /* Getters & Setters */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
