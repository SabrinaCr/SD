/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorfx;

/**
 *
 * @author Aluno
 */
public class MensagemTela 
{
    private String ip, mensagem;
    private String nome, porta;

    public MensagemTela(String nome, String mens) {
        this.nome = nome;
        this.mensagem = mens;
    }
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMens() {
        return mensagem;
    }

    public void setMens(String mens) {
        this.mensagem = mens;
    }
    
    
}
