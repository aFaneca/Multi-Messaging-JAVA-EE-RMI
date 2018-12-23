package Modelo;

import java.io.Serializable;
import java.util.*;

public class ChatPrivado implements Serializable {
    static final long serialVersionUID = 123456L;
    List<Utilizador> users;
    List<Mensagem> mensagens;

    public ChatPrivado(List<Utilizador> users) {
        this.users = users;
        this.mensagens = new ArrayList<>();
        //Collections.sort(users);
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

        return new HashSet<>(this.getUsers()).equals(new HashSet<>(that.getUsers()));
        //return equalsWithoutOrder(this.users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }

    public static boolean equalsWithoutOrder(List<?> fst, List<?> snd){
        if(fst != null && snd != null){
            if(fst.size() == snd.size()){
                // create copied lists so the original list is not modified
                List<?> cfst = new ArrayList<Object>(fst);
                List<?> csnd = new ArrayList<Object>(snd);

                Iterator<?> ifst = cfst.iterator();
                boolean foundEqualObject;
                while( ifst.hasNext() ){
                    Iterator<?> isnd = csnd.iterator();
                    foundEqualObject = false;
                    while( isnd.hasNext() ){
                        if( ifst.next().equals(isnd.next()) ){
                            ifst.remove();
                            isnd.remove();
                            foundEqualObject = true;
                            break;
                        }
                    }

                    if( !foundEqualObject ){
                        // fail early
                        break;
                    }
                }
                if(cfst.isEmpty()){ //both temporary lists have the same size
                    return true;
                }
            }
        }else if( fst == null && snd == null ){
            return true;
        }
        return false;
    }

    /* Getters & Setters */

    public List<Utilizador> getUsers() {
        return users;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }
}
