package GUI;

import Modelo.Constantes;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ListaDeUtilizadoresRenderer extends DefaultListCellRenderer {
    private final Map<String, ImageIcon> imageMap;
    //Font font = new Font("helvitica", Font.BOLD, 24);

    public ListaDeUtilizadoresRenderer(HashMap lista) {
        imageMap = createImageMap(lista);
    }

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        label.setIcon(imageMap.get((String) label.getText()));
        label.setHorizontalTextPosition(JLabel.RIGHT);
        //label.setFont(font);
        return label;
    }

    private Map<String, ImageIcon> createImageMap(Map lista) {
        Map<String, ImageIcon> map = new HashMap<>();
        try {
            lista.forEach((username, status) -> {
                    ImageIcon icone = null;
                    if(status.equals("Ativo"))
                        icone = new ImageIcon(getClass().getResource("/icon_online.png"));
                    else
                        icone = new ImageIcon(getClass().getResource("/icon_offline.png"));


                    map.put((String) username, icone);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
}
