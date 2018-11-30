/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

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
        String ipdb;

         if(args.length != 1){
             System.out.println("Sintaxe: java Servidor {IP-DA-BD}");
             return;
         }

         ipdb = args[0];

         new Server(ipdb);
     }
}
