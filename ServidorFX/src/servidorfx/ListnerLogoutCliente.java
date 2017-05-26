/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorfx;

import cliente.ClienteTela;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author AZUS
 */
public class ListnerLogoutCliente implements Runnable {
    
    private TelaServidorController tela;
    private ArrayList<ClienteTela> clientes;
    private Socket clienteLogout;
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    StringBuilder flag;
    
    ListnerLogoutCliente(TelaServidorController tela, ClienteTela clienteRemove, ArrayList<ClienteTela> clientes, ObjectOutputStream output, ObjectInputStream input, StringBuilder flag)
    {   
        this.flag = flag;
        this.clienteLogout = clienteRemove.getSocket();
        this.tela = tela;
        this.input = input;
        this.output = output;
        this.clientes = clientes;
    }
    
    public void run() 
    {   
        String action,ip, nome, porta;
        
        try
        {
            do
            {  
                String v[] = ((String)input.readObject()).split("#");
                
                action = v[0];
                ip = v[1];
                nome = v[2];
                porta = v[3];
                
                String protocoloLogout = "<@LOGOUT#IPCLIENT=" + clienteLogout.getInetAddress() + ">";
                for(ClienteTela c: clientes){
                    output = new ObjectOutputStream(c.getSocket().getOutputStream());
                    output.writeObject(protocoloLogout);
                }

                // remove cliente 
                tela.getTabelaConectados().getItems().remove(clienteLogout);
                
                flag.setLength(0);
                flag.append("false");

                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println("Erro: " + e);
                }
                
            } while (flag.equals("true"));
            
            // cliente desconectou, começa processo de retirar o ip do cliente da tabela
            int i = 0;
            ObservableList<ClienteTela> clientes = tela.getTabelaConectados().getItems();
            
            while(!clientes.get(i).getIP().equals(ip) && i < clientes.size())
                i++;
            
            clientes.remove(i);
            
            tela.getTabelaConectados().getItems().remove(i);
            
            // fecha as conexões
            output.close(); 
            input.close(); 
            clienteLogout.close();
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }   
    
}
