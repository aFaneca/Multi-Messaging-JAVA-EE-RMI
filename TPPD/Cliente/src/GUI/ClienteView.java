package GUI;

import Controlador.Controlador;
import Controlador.Servidor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ClienteView extends JFrame implements Observer {
    private static final String TITULO_DA_APP = "Aplicação de Chat";
    private static final int DIM_X = 950, DIM_Y = 670;

    JPanel panel_footer, panel_listaDeUsers, panel_header, panel_chat;
    JList list_utilizadores;
    JLabel label_titulo;
    JButton btn_sair;

    public ClienteView() {

        super();

        this.setTitle("PD");
        this.setResizable(false);

        // window settings - main JFrame
        setLayout(new BorderLayout());
        setSize(DIM_X,DIM_Y);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(150,180,140));

        // window settings - panel_listaDeUsers
        panel_listaDeUsers = new JPanel();
        panel_listaDeUsers.setBackground(new Color(0,105,92));
        configuraListaDeUsersPanel(null);

        // window settings - panel_footer
        panel_footer = new JPanel();
        panel_footer.setBackground(new Color(255,255,255));
        configuraFooterPanel();

        // window settings - panel_header
        panel_header = new JPanel(new BorderLayout(20,20));
        configuraHeaderPanel();

        // window settings - panel_chat
        panel_chat = new JPanel(new BorderLayout());
        panel_chat.setBackground(new Color(0,150,136));
        configuraChatPanel();

        this.add(panel_listaDeUsers, BorderLayout.EAST);
        this.add(panel_footer, BorderLayout.SOUTH);
        this.add(panel_header, BorderLayout.NORTH);
        this.add(panel_chat, BorderLayout.CENTER);
        //this.setVisible(true);


    }

    public void setAcaoNoFechoDaJanela(Controlador c){
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Faz alguma coisa antes de fechar a janela
                c.janelaVaiFechar();
                System.exit(0);
            }
        });
    }

    private void configuraChatPanel() {
        JLabel chat = new JLabel("CHAT");
        chat.setHorizontalAlignment(JLabel.CENTER);
        chat.setVerticalAlignment(JLabel.CENTER);
        panel_chat.add(chat, BorderLayout.CENTER);
    }

    private void configuraHeaderPanel() {
        label_titulo = new JLabel(TITULO_DA_APP);
        label_titulo.setVerticalAlignment(JLabel.CENTER);
        label_titulo.setHorizontalAlignment(JLabel.CENTER);

        btn_sair = new JButton("SAIR");

        panel_header.add(label_titulo, BorderLayout.CENTER);
        panel_header.add(btn_sair, BorderLayout.LINE_END);
    }

    private void configuraListaDeUsersPanel(ArrayList<String> lista) {
        panel_listaDeUsers.removeAll();
        if(lista != null)
            list_utilizadores = new JList(lista.toArray());
        else
            list_utilizadores = new JList();
        list_utilizadores.setVisibleRowCount(10);
        list_utilizadores.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panel_listaDeUsers.add(new JScrollPane(list_utilizadores));

        revalidate();
        repaint();
    }

    private void configuraFooterPanel(){
        panel_footer.add(new JLabel("Desenvolvido por António Faneca & Ricardo Marques"));

    }

    @Override
    public void update(Observable o, Object arg) {
        Servidor s = (Servidor) arg;
        configuraListaDeUsersPanel((ArrayList) s.getListaDeUsernames());


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

    public JButton getBtn_sair() {
        return btn_sair;
    }
}
