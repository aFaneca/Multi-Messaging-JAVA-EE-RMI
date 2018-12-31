package RMI;

import Controlador.Server;
import Modelo.Constantes;
import Modelo.Utilizador;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServicoObsInterfaceServer extends UnicastRemoteObject implements ServicoObsInterface {
    private volatile Server server;
    private List<ObsListenerInterface> listeners;
    private boolean terminar;

    public ServicoObsInterfaceServer(Server server) throws RemoteException {
        this.server = server;
        this.listeners = new ArrayList<>();
        this.terminar = false;
        iniciarServico();
    }

    @Override
    public synchronized List<String> getUtilizadoresAtivos() throws RemoteException {
        return server.getUsernamesAtivos();
    }

    @Override
    public synchronized void addObsListener(ObsListenerInterface obs) throws RemoteException {
        System.out.println("Adding listener - " + obs);
        listeners.add(obs);
        notificaListeners();
    }

    @Override
    public synchronized void removeObsListener(ObsListenerInterface obs) throws RemoteException {
        System.out.println("Removing listener - " + obs);
        listeners.remove(obs);
    }

    public synchronized void notificaListeners(){
        for(int i = 0; i < listeners.size(); i++){
            ObsListenerInterface obs = listeners.get(i);

            try{
                obs.usersListChanged(getUtilizadoresAtivos());
            }catch(RemoteException e){
                listeners.remove(obs); i--;
            }
        }
    }

    public void iniciarServico(){
        // Only required for dynamic class loading
        //System.setSecurityManager ( new RMISecurityManager() );

        try {
            //System.setProperty("java.rmi.server.hostname","127.0.0.1");
            // Start the registry on well-known port 1099.
            try{
                LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            }catch(Exception e){
                System.out.println("Registry is already in use on port " + Registry.REGISTRY_PORT);
                LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
            }
            String registo = "rmi://127.0.0.1/" + Constantes.RMI_NOME_SERVICO;
            Naming.rebind(registo, this);
            System.out.println("Serviço " + Constantes.RMI_NOME_SERVICO + " está a correr...");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /*while(!terminar){

        }*/


    }

    public boolean isTerminar() {
        return terminar;
    }

    public void setTerminar(boolean terminar) {
        this.terminar = terminar;
    }
}
