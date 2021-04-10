/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Mensagem;
import view.ClienteFrame;

/**
 *
 * @author Michael
 */
public class Recebedor implements Runnable {

    private ObjectInputStream entrada;
    private Controler controler;
    private boolean sair;

    public Recebedor(Socket socket, Controler controler) {
        this.controler = controler;
        sair = false;
        try {
            this.entrada = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Mensagem mensagem = null;
        try {
            while (!sair && (mensagem = (Mensagem) entrada.readObject()) != null) {
                Mensagem.Acao acao = mensagem.getAcao();
                switch (acao) {
                    case CONECTAR:
                        controler.connected(mensagem);
                        break;
                    case DESCONECTAR:
                        sair = true;
                        controler.desconectar();
                    case ENVIAR:
                    case SUSSURRAR:
                        System.out.println("::: " + mensagem.getConteudo() + " :::");
                        controler.receberMensagem(mensagem);
                        break;
                    case USUARIOS_ONLINE:
                        controler.atualizaOnlines(mensagem);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException ex) {
            System.err.println("Problemas com o servidor");
            JOptionPane.showMessageDialog(null, "Problemas com o servidor\n" + ex.getMessage());
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }
    }

    public void close() {
        sair = true;
    }
}
