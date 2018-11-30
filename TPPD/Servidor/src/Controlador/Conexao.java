package Controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Conexao {
    protected Socket socket;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;

    public Conexao(Socket socket) {
        this.socket = socket;
    }

    public void iniciarInputStream() throws IOException {
        in = new ObjectInputStream(socket.getInputStream());
    }
    public void iniciarOutputStream() throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    /* Getters & Setters */
    public ObjectInputStream receber(){
        return in;
    }

    public ObjectOutputStream enviar(){
        return out;
    }

    public Socket getSocket(){
        return socket;
    }

    public void fecharSocket() throws IOException {
        socket.close();
    }
}
