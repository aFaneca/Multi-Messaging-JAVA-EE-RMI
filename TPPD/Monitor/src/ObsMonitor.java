import GUI.MonitoringView;
import Modelo.Constantes;
import RMI.ObsListenerInterface;
import RMI.ServicoObsInterface;
import javafx.beans.Observable;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ObsMonitor extends UnicastRemoteObject implements ObsListenerInterface {
    protected static MonitoringView view = new MonitoringView();

    protected ObsMonitor() throws RemoteException {}

    public static void main(String args[]){
        String ipServer;
        if(args.length != 1){
            System.out.println("Sintaxe: java ObsMonitor {IP DO SERVIDOR}");
            return;
        }
        ipServer = args[0];

        try{
            String registo = "rmi://" + ipServer + "/" + Constantes.RMI_NOME_SERVICO;
            Remote remoteService = Naming.lookup(registo);
            ServicoObsInterface sistemaObs = (ServicoObsInterface) remoteService;

            //


            view.setVisible(true);
            // Cria um novo monitor e regista-o  como um listener
            ObsMonitor monitor = new ObsMonitor();
            sistemaObs.addObsListener(monitor);



        }catch (NotBoundException e){
            System.out.println ("No sensors available");
        }catch (RemoteException e) {
            System.out.println ("RMI Error - " + e);
            e.printStackTrace();
        }catch (Exception e){
            System.out.println ("Error - " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void usersListChanged(List<String> utilizadoresAtivos) throws RemoteException {
        view.configuraPainelCentro(utilizadoresAtivos);
        System.out.println(utilizadoresAtivos);
    }
}
