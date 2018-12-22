package RMI;

import Modelo.Utilizador;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ObsListenerInterface extends Remote {
    public void usersListChanged(List<String> utilizadoresAtivos) throws RemoteException;
}
