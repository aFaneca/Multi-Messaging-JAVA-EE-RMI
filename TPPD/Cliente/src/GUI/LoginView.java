package GUI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class LoginView extends JFrame implements Observer {

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


    String user = null;
    String pass = null;


    public LoginView(){

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


    public void addListener(ActionListener cont, JButton b){
        b.addActionListener(cont);
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


    @Override
    public void update(Observable o, Object arg) {

    }
}