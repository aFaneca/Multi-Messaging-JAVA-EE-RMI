package GUI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class ClienteFrame extends JFrame implements Observer {

    int x = 300;
    int y = 250;

    JLabel ops = new JLabel();
    JButton loginButton = new JButton("Login");
    Dimension opsDim = new Dimension(200,32);

    JLabel usernameInfo = new JLabel();
    JTextField username = new JTextField();

    JLabel titulo = new JLabel();

    JLabel passwordInfo = new JLabel();
    JTextField password = new JPasswordField();

    JTextField serverName = new JTextField();
    JSpinner serverPort = new JSpinner(new SpinnerNumberModel(9999, 1024, 65535, 1));

    Dimension loginDim = new Dimension(200, 32);

    JLabel textUsername = new JLabel();
    JLabel textPassword = new JLabel();
    JLabel textServers = new JLabel();
    JLabel textPorts = new JLabel();

    JLabel menuInicial = new JLabel();

    JLabel contactos = new JLabel();
    JList<String> contactosLista;

    JLabel caixaDeChat = new JLabel();
    JTextArea chat = new JTextArea();
    Dimension chatDim = new Dimension(400,200);

    JLabel msg = new JLabel();
    JButton msgButton = new JButton("Enviar");

    String user = null;
    String pass = null;
    String mensagem = null;


    public ClienteFrame(){

        super();
        this.setTitle("PD");
        this.setResizable(false);

        //window settings
        setLayout(null);
        setSize(950,670);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new java.awt.Color(135, 206, 250));

        titulo.setBounds(400, 100, 200, 200);
        titulo.setText("MAKE PD GREAT AGAIN!");


        textUsername.setBounds(x, y, 75, 40);
        textPassword.setBounds(x, y+40, 75, 40);
        textServers.setBounds(x, y+80, 75, 40);

        textUsername.setText("Username");
        textPassword.setText("Password");
        textServers.setText("Endereços");
        textPorts.setText("Ports");

        textUsername.setForeground(Color.white);
        textPassword.setForeground(Color.white);
        textServers.setForeground(Color.white);
        add(textUsername);
        add(textPassword);
        add(textServers);
        add(titulo);

        usernameInfo.setBounds(x + 75, y, 200, 40);
        usernameInfo.setLayout(new FlowLayout());
        username.setPreferredSize(loginDim);
        usernameInfo.add(username);
        add(usernameInfo);

        passwordInfo.setBounds(x + 75, y+40, 200, 40);
        passwordInfo.setLayout(new FlowLayout());
        password.setPreferredSize(loginDim);
        passwordInfo.add(password);
        add(passwordInfo);

        serverName.setBounds(x+75, y+84, 110, 35);
        serverName.setText("127.0.0.1");
        add(serverName);

        serverPort.setBounds(x + 190, y + 84, 85, 35);
        add(serverPort);


        ops.setBounds(x+75,y+120,200,40);
        ops.setLayout(new FlowLayout());
        loginButton.setPreferredSize(opsDim);
        ops.add(loginButton);
        add(ops);

    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addListener(ActionListener cont, JButton b){
        b.addActionListener(cont);
    }

    public String getMsg(){
        return mensagem;
    }


    public void loginAceite(){

        ops.setVisible(false);
        passwordInfo.setVisible(false);
        usernameInfo.setVisible(false);
        textPassword.setVisible(false);
        textUsername.setVisible(false);
        textServers.setVisible(false);
        titulo.setVisible(false);
        serverName.setVisible(false);
        serverPort.setVisible(false);

        DefaultListModel<String> utilizadoresAtivos = new DefaultListModel<>();
        utilizadoresAtivos.addElement("VicVega");
        utilizadoresAtivos.addElement("Maria Pinto");
        utilizadoresAtivos.addElement("Adolfo Hilário");
        utilizadoresAtivos.addElement("Mateus Salvador");
        utilizadoresAtivos.addElement("Vitor Urbano");
        utilizadoresAtivos.addElement("Maria Leprosa");
        utilizadoresAtivos.addElement("xXXPUSSYDESTROYER69XXx");
        utilizadoresAtivos.addElement("Twiisted");

        //create the list
        JList<String> uA = new JList<>(utilizadoresAtivos);

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
    }

    public String getPass(){
        return pass;
    }

    public String getUser(){
        return user;
    }

    public JButton getB_Login( ){
        user = username.getText();
        pass = password.getText();

        return loginButton;
    }

    public JButton getB_Enviar(){
        mensagem = chat.getText();
        return msgButton;
    }


    public String getServerName() {
        return this.serverName.getText();
    }

    public int getServerPort() {
        try {
            this.serverPort.commitEdit(); // para verificar se foram introduzidos dados manualmente e guarda-los
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (Integer) this.serverPort.getValue();
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