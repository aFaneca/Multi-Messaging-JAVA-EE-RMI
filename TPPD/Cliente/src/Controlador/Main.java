package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import GUI.ClienteView;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class Main implements Observable{
    Socket s = null;

    public Main(){
        Controlador c = new Controlador(this);
        //new ClienteView().setVisible(true);
    }

    public static void main(String args[]){
        new Main();
    }


    @Override
    public void addListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

