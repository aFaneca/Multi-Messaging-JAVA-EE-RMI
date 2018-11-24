/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import DAO.UtilizadorDao;
import Entidades.Utilizador;

/**
 *
 * @author me
 */
public class Main {
     public static void main(String[] args) {
         /*UtilizadorDao ud = new UtilizadorDao("utilizador");
         ud.guardar(u);
         Utilizador u = ud.recuperar("Tony");
         System.out.println(u);*/
         new Server();
     }
}
