/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientefx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.Label;

/**
 *
 * @author AZUS
 */
public class ListnerConectarCliente implements Runnable {
    private TelaClienteController tela;
    
    private Socket cliente;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    private String status;
    
    private ArrayList<ClienteTela> clientes;
    
    public ListnerConectarCliente (Socket cliente, String status, TelaClienteController tela)
    {   
        this.status = status;
        this.cliente = cliente;
        this.clientes = new ArrayList();
        this.tela = tela;
    }
    
    public void run() 
    {   
        String nome, ip, porta;
        
        try
        {
            this.output = new ObjectOutputStream(cliente.getOutputStream());
            this.input = new ObjectInputStream(cliente.getInputStream());
            
            do
            { 
                String protocolo = input.readObject().toString();
                String v[] = protocolo.split("#");
                String aux[];

                if(v[1].equals("@ACCEPT"))
                {
                    // atualiza tabela
                    // desmembra protocolo
                    for(int i = 2; i < v.length; i++)
                    {
                        aux = v[i++].split("IPCLIENT=");
                        ip = aux[1];

                        aux = v[i++].split("NOME=");
                        nome = aux[1];

                        aux = v[i].split("PORTA=");
                        porta = aux[1];
                        
                        if(porta.endsWith(">"))
                            porta = porta.substring(0, porta.length()-1);
                        
                        ClienteTela c;
                        c = new ClienteTela(nome, ip, Integer.parseInt(porta), new Socket(ip, Integer.parseInt(porta)));
                        
                        clientes.add(c);
                    }
                    
                    // atualizar tabela na tela
                    tela.getTabelaClientesOnline().getItems().addAll(clientes);
                }
                else if(v[1].equals("@MSG"))
                {
                    // mostra mensagem   
                }
                else if(v[1].equals("@LOGOUT"))
                {
                    // retira o cliente da tabela
                }
                else if(v[1].equals("@NEWUSER"))
                {
                    // adiciona novo usuário à lista
                    aux = v[2].split("IPCLIENT=");
                    ip = aux[1];

                    aux = v[3].split("NOME=");
                    nome = aux[1];

                    aux = v[4].split("PORTA=");
                    porta = aux[1];
                    porta = porta.substring(0, porta.length()-1);
                    
                    ClienteTela c;
                    c = new ClienteTela(nome, ip, Integer.parseInt(porta), new Socket(ip, Integer.parseInt(porta)));
                    tela.getTabelaClientesOnline().getItems().add(c);
                }
                
                try{Thread.sleep(1000);}catch(Exception e){}
                
            }while (status.equals("Conectado"));
            
            // fecha as conexões
            output.close(); 
            input.close(); 
            cliente.close();
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
