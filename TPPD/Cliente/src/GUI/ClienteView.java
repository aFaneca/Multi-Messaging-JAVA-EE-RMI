package GUI;

import Controlador.Servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ClienteView extends JFrame implements Observer {
    int x = 300;
    int y = 250;

    JLabel contactos;
    JLabel caixaDeChat;
    JTextArea chat;
    Dimension chatDim;
    JLabel msg;
    JButton msgButton;
    Dimension opsDim;
    String mensagem = null;
    DefaultListModel<String> listaDeUtilizadores;

    public ClienteView(){
        super();
        this.setTitle("PD");
        this.setResizable(false);

        //window settings
        setLayout(null);
        setSize(950,670);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new java.awt.Color(135, 206, 250));

        //



        configuraContatos(new ArrayList<String>());




    }

    private void configuraContatos(ArrayList<String> lista){
        //removeAll();
        caixaDeChat = new JLabel();
        chat = new JTextArea();
        chatDim = new Dimension(400,200);
        msgButton = new JButton("Enviar");
        msg = new JLabel();
        opsDim = new Dimension(200,32);

        contactos = new JLabel();
        listaDeUtilizadores = new DefaultListModel();
        System.out.println("----------");
        for(String username : lista ){
            listaDeUtilizadores.addElement(username);
            System.out.println(username);
        }
        System.out.println("----------");
        JList uA = new JList(listaDeUtilizadores);

        contactos.setBounds(x+50,y-100, 250,375);
        contactos.setLayout(new FlowLayout());
        contactos.add(uA);
        contactos.setVisible(true);
        add(contactos);

        caixaDeChat.setBounds(x,y+100,400,200);
        caixaDeChat.setLayout(new FlowLayout());
        chat.setPreferredSize(chatDim); // 400 200
        caixaDeChat.add(chat);
        add(caixaDeChat);

        msg.setBounds(x,y+300,200,40);
        msg.setLayout(new FlowLayout());
        msgButton.setPreferredSize(opsDim);
        msg.add(msgButton);
        add(msg);
        revalidate();
        repaint();

    }
    @Override
    public void update(Observable o, Object arg) {
        Servidor s = (Servidor) arg;
        configuraContatos((ArrayList) s.getListaDeUsernames());


    }
    public void addListener(ActionListener cont, JButton b){
        b.addActionListener(cont);
    }
    public void aprensentarAlerta(String tipo, String msg) {

        switch(tipo){
            case "Info": JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Erro": JOptionPane.showMessageDialog(this, msg, "Erro!", JOptionPane.ERROR_MESSAGE);
                break;
            default: JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
    /* Getters & Setters */
    public JButton getB_Enviar(){
        mensagem = chat.getText();
        return msgButton;
    }
    public String getMsg(){
        return mensagem;
    }
}
