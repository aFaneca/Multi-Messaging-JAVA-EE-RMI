/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

/**
 *
 * @author António Faneca e Ricardo Marques
 */
public class Main {
     public static void main(String[] args) {
        String ipdb;

         if(args.length != 1){
             System.out.println("Sintaxe: java Servidor {IP-DA-BD}");
             return;
         }

         ipdb = args[0];

         new Server(ipdb);
     }
}
