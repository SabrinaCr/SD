/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.net.Socket;

public class ClienteTela {
   private String nome;
   private String IP;
   private int porta;
   private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ClienteTela(String nome, String IP, int porta, Socket socket) {
        this.nome = nome;
        this.IP = IP;
        this.porta = porta;
        this.socket = socket;
    }

    public ClienteTela(String nome, String IP, int porta) {
        this.nome = nome;
        this.IP = IP;
        this.porta = porta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }
    
}
