package RMI;

import Modelo.Utilizador;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServicoObsInterface extends Remote {
    public List<String> getUtilizadoresAtivos() throws RemoteException;
    public void addObsListener(ObsListenerInterface obs) throws RemoteException;
    public void removeObsListener(ObsListenerInterface obs) throws RemoteException;
}
