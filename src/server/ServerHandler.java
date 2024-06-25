package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import classes.BDManager;

public class ServerHandler {
    private BDManager bdConn;
    private ExecutorService executor;

    public ServerHandler(BDManager bdConn) {
        this.bdConn = bdConn;
        this.executor = Executors.newCachedThreadPool(); 
    }

    public void CreateServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado na porta " + port);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                ClientHandler_Threads clientHandler = new ClientHandler_Threads(clientSocket, "Thread"+clientSocket.getPort(), bdConn);

                executor.submit(clientHandler);
            }
        }catch (Exception e) {
			// TODO: handle exception
		}
    }
}

