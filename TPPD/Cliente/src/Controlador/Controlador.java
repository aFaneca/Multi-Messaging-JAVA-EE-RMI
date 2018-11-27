/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import GUI.ClienteFrame;
import javafx.beans.InvalidationListener;

/**
 *
 * @author Ricardo Marques
 */
public class Controlador implements ActionListener{

    ClienteFrame f = new ClienteFrame();
    boolean login = false;
    private Main m;

    public Controlador(Main m){
        f.setVisible(true);
        f.addListener(this, f.getB_Login());
        f.addListener(this, f.getB_Enviar());
        this.m = m;
    }


    public void addListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource();
        //Menu iniciar
        if(origem == f.getB_Login()){
            if("123".equals(f.getPass()) && "Tony".equals(f.getUser())){
                f.loginAceite(); // IR À LOCAL BD VER SE É ACEITE
            }

        }else if(origem == f.getB_Enviar()){
            System.out.println(f.getMsg());
        }
    }
}
