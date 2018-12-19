package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatPrivado implements Serializable {
    static final long serialVersionUID = 123456L;
    List<Utilizador> users;
    List<Mensagem> mensagens;

    public ChatPrivado(List<Utilizador> users) {
        this.users = users;
        this.mensagens = new ArrayList<>();
    }

    public void addUserToChat(Utilizador user){
        this.users.add(user);
    }

    public void removeUserFromChat(Utilizador user){
        this.users.remove(user);
    }

    public void addMessage(Mensagem msg){
        mensagens.add(msg);
    }
    /* Equals & HashCode - same users = same private chat */

   @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatPrivado that = (ChatPrivado) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }

    /* Getters & Setters */

    public List<Utilizador> getUsers() {
        return users;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }
}
