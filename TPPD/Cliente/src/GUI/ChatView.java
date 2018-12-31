package GUI;

import Controlador.Controlador;
import Modelo.ChatPrivado;
import Modelo.Mensagem;
import Modelo.Utilizador;
import Controlador.Servidor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class ChatView extends JFrame implements Observer {

    private JLabel panel_talk;
    private JList panel_ficheiros;
    private JTextArea zonaChat;
    private JButton btn_enviar;
    private JButton btn_transferir;
    private Controlador c;
    private String guardaChat;
    private String user, userDestino;
    private ChatPrivado chatPrivado;

    //String user, String userDestino, Controlador c,String mensagem
    public ChatView(String user, String userDestino, Controlador c, ChatPrivado chatPrivado) {
        super(user + " - Estás a falar com " + userDestino);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.user = user;
        this.userDestino = userDestino;
        this.c = c;
        this.chatPrivado = chatPrivado;

        setLayout(null);
        setSize(1024,670);

        getContentPane().setBackground(new java.awt.Color(135, 206, 250));

        btn_enviar = new JButton("Enviar");
        btn_transferir = new JButton("Tranferir");
        zonaChat = new JTextArea();
        panel_talk = new JLabel();
        panel_ficheiros = new JList();

        panel_ficheiros.setBounds(50, 50, 400, 200);
        panel_ficheiros.setBackground(Color.WHITE);
        panel_ficheiros.setOpaque(true);

        panel_talk.setBounds(550, 50, 300, 500);
        panel_talk.setBackground(Color.WHITE);
        panel_talk.setVerticalAlignment(SwingConstants.BOTTOM);
        //panel_talk.setText(mensagem);
        panel_talk.setOpaque(true);

        zonaChat.setBounds(50, 350, 400, 200);
        btn_enviar.setBounds(50,560,75,25);
        btn_transferir.setBounds(50, 260, 100,25);
        atualizaMensagens();
        add(zonaChat);
        add(panel_talk);
        add(btn_enviar);
        add(btn_transferir);
        add(panel_ficheiros);
        //setAcaoNoFechoDaJanela(c);
        btn_enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
/*                String s = null;
                String [] arr = null;
                String date = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());
                String s1 = null;*/

                c.enviarMensagemChatPrivado(getChatPrivado(), zonaChat.getText(), getUsername());
                zonaChat.setText("");
            }
        });

        setVisible(true);
    }

    public ChatPrivado getChatPrivado(){
        return chatPrivado;
    }

    private void setAcaoNoFechoDaJanela(Controlador c){
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Faz alguma coisa antes de fechar a janela
                //c.janelaDeChatVaiFechar((ChatView) this, chatPrivado);
                System.exit(0);
            }
        });
    }

    private void atualizaMensagens() {
        String str = "<html>";

        for(Mensagem msg : chatPrivado.getMensagens()){
            str += "[" + msg.getData();
            str += "] ";
            str += msg.getUsernameRemetente() + " - ";
            str += msg.getMensagem();
            str += "<br/>";
        }

        str += "</html>";

        panel_talk.setText(str);

        revalidate();
        repaint();

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

    public void setPanel(String t){
        JLabel jlabel = new JLabel(t);
        panel_talk.add(jlabel);
    }

    public String getUserDestino(){
        return userDestino;
    }

    public String getUsername(){
        return user;
    }

    public JButton getBtn_Enviar(){return btn_enviar;}


    @Override
    public void update(Observable o, Object arg) {
        Servidor server = (Servidor) arg;
        chatPrivado = server.getChatPrivado(user, userDestino);
        atualizaMensagens();
    }
}
