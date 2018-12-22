package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MonitoringView extends JFrame{
    int DIM_X = 950;
    int DIM_Y = 670;
    JPanel panel_centro;
    JLabel label_listaDeUtilizadores;

    public MonitoringView() {
        super("Sistema de Monitorização");
        this.setResizable(false);

        //window settings
        setLayout(new BorderLayout());
        setSize(DIM_X, DIM_Y);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel_centro = new JPanel();
        configuraPainelCentro(null);

        add(panel_centro, BorderLayout.CENTER);
    }

    public void configuraPainelCentro(List<String> lista) {
        panel_centro.removeAll();
        label_listaDeUtilizadores = new JLabel("");
        String txt = "<html>";
        txt += "<h1>Utilizadores Ativos</h1><br/>";

        if(lista != null){
            for(String username : lista){
                txt += username + "<br/>";
            }
        }

        txt += "</html>";

        label_listaDeUtilizadores.setText(txt);
        panel_centro.add(label_listaDeUtilizadores);
        revalidate();
        repaint();

    }

}
