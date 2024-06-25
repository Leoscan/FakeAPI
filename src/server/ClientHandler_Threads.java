package server;

import java.net.Socket;
import classes.BDManager;
import classes.JsonThreadRun;

public class ClientHandler_Threads implements Runnable {
    private String nome;
    private Socket conexao;
    private JsonThreadRun rt;
    private BDManager bdConn;

    public ClientHandler_Threads(Socket _conexao, String _nome, BDManager bdConn) {
        this.conexao = _conexao;
        this.nome = _nome;
        this.bdConn = bdConn;
        rt = new JsonThreadRun(conexao, nome);
    }

    @Override
    public synchronized void run() {
    	rt.ReceberArquivosJSON(bdConn);
    }
}
