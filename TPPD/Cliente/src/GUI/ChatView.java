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
    private JTextArea zonaChat;
    private JButton btn_enviar;
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
        setSize(950,670);

        btn_enviar = new JButton("Enviar");
        zonaChat = new JTextArea();
        panel_talk = new JLabel();

        panel_talk.setBounds(500, 50, 300, 500);
        panel_talk.setBackground(Color.WHITE);
        panel_talk.setVerticalAlignment(SwingConstants.BOTTOM);
        //panel_talk.setText(mensagem);
        panel_talk.setOpaque(true);

        zonaChat.setBounds(50, 350, 400, 200);
        btn_enviar.setBounds(50,560,75,25);
        atualizaMensagens();
        add(zonaChat);
        add(panel_talk);
        add(btn_enviar);
        //setAcaoNoFechoDaJanela(c);
        btn_enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
/*                String s = null;
                String [] arr = null;
                String date = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());
                String s1 = null;*/

                c.enviarMensagemChatPrivado(getChatPrivado(), zonaChat.getText());
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


    public JButton getBtn_Enviar(){return btn_enviar;}





    @Override
    public void update(Observable o, Object arg) {
        Servidor server = (Servidor) arg;
        chatPrivado = server.getChatPrivado(user, userDestino);
        atualizaMensagens();
    }
}
