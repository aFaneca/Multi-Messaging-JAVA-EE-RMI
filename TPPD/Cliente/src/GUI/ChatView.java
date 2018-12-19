package GUI;

import Controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class ChatView implements Observer{

    private JLabel panel_talk = new JLabel();
    private JTextArea zonaChat = new JTextArea();
    private String user;
    private String userDestino;
    private JButton btn_enviar = new JButton("Enviar");
    private Controlador c;
    private String guardaChat;


    public ChatView(String user, String userDestino, Controlador c,String mensagem) {
        this.user = user;
        this.userDestino = userDestino;
        this.c = c;

        JFrame frame = new JFrame(user + " Estás a falar com " + userDestino);
        frame.setLayout(null);
        frame.setSize(950,670);


        panel_talk.setBounds(500, 50, 300, 500);
        panel_talk.setBackground(Color.WHITE);
        panel_talk.setVerticalAlignment(SwingConstants.BOTTOM);
        panel_talk.setText("<html> " +  mensagem + " </html>");
        panel_talk.setOpaque(true);

        zonaChat.setBounds(50, 350, 400, 200);
        btn_enviar.setBounds(50,560,75,25);

        frame.add(zonaChat);
        frame.add(panel_talk);
        frame.add(btn_enviar);

        btn_enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = null;
                String [] arr = null;
                String date = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());

                c.mensagemAEnviar(userDestino, zonaChat.getText());
                //guardaZonaChat(zonaChat.getText());
                if(panel_talk.getText() == ""){
                    s = "<html> " + user + ": " + zonaChat.getText() + " - " + date +" </html>";
                    panel_talk.setText(s);
                }else{
                    System.out.println(panel_talk.getText());
                    arr = panel_talk.getText().split(" ", 2); // tira o <html>
                    System.out.println("Escreve mensagem enviada: " + arr[0]);
                    System.out.println("Escreve mensagem enviada: " + arr[1]);
                    String s1 = arr[1].substring(0, arr[1].lastIndexOf(" "));


                    s = "<html> "  + s1 + "<br/>" + user + ": "+ zonaChat.getText()+ " - " + date +" </html>";
                    panel_talk.setText(s);
                }
                zonaChat.setText("");
            }
        });

        frame.setVisible(true);
    }


    public void guardaZonaChat(String text) {
        this.guardaChat = text;
    }

    public String getGuardaZonaChat(){
        return guardaChat;
    }

    public String getMensagem(){
        return zonaChat.getText();
    }


    public String getUserDestino(){
        return userDestino;
    }


    public JButton getBtn_Enviar(){return btn_enviar;}


    public void setConversacionText(String mensagem){
        String s = null;
        String [] arr = null;
        String date = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());

        //panel_talk.setText(mensagem);

        if(panel_talk.getText() == ""){
            s = "<html> " + userDestino + ": " + mensagem + " - " + date +" </html>";
            panel_talk.setText(s);
        }else{
            arr = panel_talk.getText().split(" ", 2);
            String s1 = arr[1].substring(0, arr[1].lastIndexOf(" "));

            s = "<html> "  + s1 + "<br/>" + userDestino + ": "+ mensagem + " - " + date +" </html>";
            panel_talk.setText(s);
        }
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
