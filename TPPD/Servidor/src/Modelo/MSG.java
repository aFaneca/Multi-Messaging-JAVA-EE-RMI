package Modelo;

import java.io.Serializable;

public class MSG implements Serializable {
    static final long serialVersionUID = 1L;
    private int msgId;
    private static int i = 0;
    private Constantes.TIPOS tipo;
    private Object obj;


    public MSG(Constantes.TIPOS tipo, Object obj) {
        this.msgId = i++;
        this.tipo = tipo;
        this.obj = obj;
    }

    /* Getters & Setters*/
    public Constantes.TIPOS getTipo() {
        return tipo;
    }

    public void setTipo(Constantes.TIPOS tipo) {
        this.tipo = tipo;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
