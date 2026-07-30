package vn.edu.ptit.int1433.training.contract.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataService extends Remote {
    int[] request(String token, String qCode) throws RemoteException;
    boolean submit(String token, String qCode, int[][] triples) throws RemoteException;
}
