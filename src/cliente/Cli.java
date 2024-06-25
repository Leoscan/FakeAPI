package cliente;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cli {

    public static void main(String[] args) throws ClassNotFoundException {
        String host = "localhost";
        int port = 7000;
        String filePath = "insert.json"; // Atualize com o caminho para o arquivo .json a ser enviado

        try {
        	Socket socket = new Socket(host, port);
            FileInputStream fileIn = new FileInputStream(filePath);
        	OutputStream out = socket.getOutputStream();
        	Scanner  in = new Scanner(socket.getInputStream());
        	
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("Arquivo JSON enviado com sucesso");
            out.flush();
            socket.shutdownOutput();
            
            new Thread(() -> {
                while (in.hasNextLine()) {
                    String linha = in.nextLine();
                    System.out.println(linha);
                }
            }).start();
        } catch (IOException e) {
            System.err.println("Erro ao enviar arquivo: " + e.getLocalizedMessage());
        }
    }
}
