package GUI;

import Controlador.Servidor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ClienteView extends JFrame implements Observer {
    JPanel panel_chat, panel_listaDeUsers;
    int x = 300, y = 250;

    public ClienteView() {
        super();

        this.setTitle("PD");
        this.setResizable(false);

        // window settings - main JFrame
        setLayout(new BorderLayout());
        setSize(950,670);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(135, 206, 250));

        // window settings - panel_listaDeUsers
        panel_listaDeUsers = new JPanel();
        panel_listaDeUsers.setBackground(new Color(0,0,0));
        configuraListaDeUsersPanel(null);

        // window settings - panel_chat
        panel_chat = new JPanel();
        panel_chat.setBackground(new Color(255,255,255));
        configuraChatPanel();


        this.add(panel_listaDeUsers, BorderLayout.EAST);
        this.add(panel_chat, BorderLayout.SOUTH);

        //this.setVisible(true);
    }

    private void configuraListaDeUsersPanel(ArrayList<String> lista) {
        panel_listaDeUsers.removeAll();
        if(lista == null) lista = new ArrayList<String>();
        panel_listaDeUsers.add(new JLabel("Users:"));
        String msg = "";

        for(String username : lista){
            msg += username + "\n";
        }

        panel_listaDeUsers.add(new JLabel(msg));
        revalidate();
        repaint();
    }

    private void configuraChatPanel(){
        panel_chat.add(new JLabel("CHAT"));

    }

    @Override
    public void update(Observable o, Object arg) {
        Servidor s = (Servidor) arg;
        configuraListaDeUsersPanel((ArrayList) s.getListaDeUsernames());


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


}
