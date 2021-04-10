      /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Set;
import model.Mensagem;
import model.Mensagem.Acao;
import view.ClienteFrame;

/**
 *
 * @author Michael
 */
public class Controler extends ClienteService {

    private Recebedor recebedor;
    private ClienteFrame clienteFrame;
    private static Controler instance;
    private boolean conectou;

    public static Controler getInstance() {
        if (instance == null) {
            instance = new Controler();
        }
        return instance;
    }

    private Controler() {
        super();
    }

    public void connected(Mensagem message) {
        conectou = true;
        clienteFrame.conectou(conectou);
    }

    public void desconectar(Mensagem mensagem) {
        if (conectou) {
            enviar(mensagem);
        }
    }

    public void desconectar() {
        super.close();
    }

    public void receberMensagem(Mensagem mensagem) {
        if (mensagem.getAcao() == Acao.ENVIAR) {
            clienteFrame.recebeMensagem(mensagem);
        } else {
            clienteFrame.recebeSussurro(mensagem);
        }
    }

    public void atualizaOnlines(Mensagem mensagem) {
        System.out.println(mensagem.getPessoasOnline().toString());
        Set<String> usuarios = mensagem.getPessoasOnline();
        usuarios.remove(mensagem.getOrigem());
        String[] nomes = (String[]) usuarios.toArray(new String[usuarios.size()]);
        clienteFrame.atualizaListaOnline(nomes);
    }

    public void conectar(Mensagem mensagem) {
        conecta();
        recebedor = (new Recebedor(getConexao(), this));
        new Thread(recebedor).start();
        enviar(mensagem);
    }

    public void inicia() {
        clienteFrame = new ClienteFrame();
        clienteFrame.setVisible(true);
    }
}
