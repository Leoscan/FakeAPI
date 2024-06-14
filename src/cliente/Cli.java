package cliente;

import java.io.*;
import java.net.Socket;

public class Cli {

    public static void main(String[] args) throws ClassNotFoundException {
        String host = "localhost";
        int port = 7000;
        String filePath = "Select.json"; // Atualize com o caminho para o arquivo .json a ser enviado

        try {
        	Socket socket = new Socket(host, port);
            FileInputStream fileIn = new FileInputStream(filePath);
        	OutputStream out = socket.getOutputStream();
        	InputStream in = socket.getInputStream();
        	
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("Arquivo JSON enviado com sucesso");
            out.flush();
            socket.shutdownOutput();
            /*
            ObjectInputStream ois = new ObjectInputStream(in);
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            */
        } catch (IOException e) {
            System.err.println("Erro ao enviar arquivo: " + e.getMessage());
        }
    }
}
