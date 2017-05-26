/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorfx;

import cliente.ClienteTela;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author AZUS
 */
public class ListnerNovoCliente implements Runnable {
    
    private TelaServidorController tela;
    private ServerSocket servidor;
    
    private ArrayList<ClienteTela> clientes = new ArrayList<ClienteTela>();
    
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ListnerNovoCliente(TelaServidorController tela, ServerSocket servidor) {
        this.tela = tela;
        this.servidor = servidor;
    }
    
    public void run() {
        
        String nome, ip, porta;
        StringBuilder flag = new StringBuilder("true");
        
        try
        {
            while(tela.isAtivo())
            {
                Socket novoCliente = servidor.accept();

                input = new ObjectInputStream(novoCliente.getInputStream());

                // 
                String protocolo = input.readObject().toString();
                String aux[];
                String v[] = protocolo.split("#");
                aux = v[2].split("IPCLIENT=");
                ip = aux[1];

                aux = v[3].split("NOME=");
                nome = aux[1];

                aux = v[4].split("PORTA=");
                porta = aux[1];
                porta = porta.substring(0, porta.length()-1);

                System.out.println("Novo Cliente: " + ip + " " + nome + "\n");

                // protocolo de Novo Cliente
                String protocoloNewUser = protocolo.replace("CONNECT", "NEWUSER");

                // envia protocolo de novo cliente aos demais clientes
                for (ClienteTela c : clientes) {
                    ObjectOutputStream out = new ObjectOutputStream(c.getSocket().getOutputStream());
                    out.writeObject(protocoloNewUser);
                }

                // protocolo ao novo usuário - lista de online
                String protocoloAccept = "<#@ACCEPT#IPCLIENT="+ ip
                + "#NOME="+ nome +""
                + "#PORTA="+ porta;
                for (ClienteTela c : clientes) {
                    protocoloAccept += "#IPCLIENT=" + c.getIP()
                                    + "#NOME=" + c.getNome()
                                    + "#PORTA=" + c.getPorta();
                }
                protocoloAccept +=">";
                
                output = new ObjectOutputStream(novoCliente.getOutputStream());
                output.writeObject(protocoloAccept);

                // novo cliente
                ClienteTela c = new ClienteTela(nome, ip, Integer.parseInt(porta), novoCliente);

                // adicona novo cliente na lista do servidor
                clientes.add(c);
                tela.getTabelaConectados().getItems().add(c);
                
                //Platform.runLater(()->{tela.getTabelaConectados().getItems().add(c);});


                // lança uma trhread para tratar exclusivamente esse cliente
//                ListnerLogoutCliente th = new ListnerLogoutCliente(tela, c, clientes, output, input, flag);
//                th.run();
//                Thread newThrd = new Thread(th);
//                newThrd.start(); // inicia a thread

            }
        
        }catch(IOException e){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText(e.getMessage());
                a.showAndWait();
                
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ListnerNovoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
