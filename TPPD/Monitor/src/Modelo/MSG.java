package Modelo;

import java.io.Serializable;

public class MSG implements Serializable {
    static final long serialVersionUID = 1L;
    private int msgId;
    private static int i = 0;
    private Constantes.MENSAGEM_TIPO tipo;
    private Object obj;


    public MSG(Constantes.MENSAGEM_TIPO tipo, Object obj) {
        this.msgId = i++;
        this.tipo = tipo;
        this.obj = obj;
    }

    /* Getters & Setters*/
    public Constantes.MENSAGEM_TIPO getTipo() {
        return tipo;
    }

    public void setTipo(Constantes.MENSAGEM_TIPO tipo) {
        this.tipo = tipo;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
