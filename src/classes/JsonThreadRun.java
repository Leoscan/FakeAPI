package classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;

public class JsonThreadRun {
    Socket conexao; 
    String nome;
    
    public JsonThreadRun(Socket conexao, String nome) {
        this.conexao = conexao;
        this.nome = nome;
    }

    public synchronized void ReceberArquivosJSON(BDManager bdConn) {
        PrintWriter writer = null;
        try (InputStream in = conexao.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            writer = new PrintWriter(conexao.getOutputStream());
            EscreveArquivo(reader);
            System.out.println("Arquivo recebido com sucesso");
            realizaAcao(bdConn, writer);
        } catch (IOException e) {
            System.err.println("Erro ao receber arquivo: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (!conexao.isClosed()) {
                    conexao.close();
                }
            } catch (IOException e) {
                System.err.println("Erro ao fechar o socket: " + e.getMessage());
            }
        }
    }

    
    public void EscreveArquivo(BufferedReader reader) {
        try (FileOutputStream fileOut = new FileOutputStream("arquivo_recebido_de_" + nome + ".json")) {
            String linha;
            StringBuilder stringBuilder = new StringBuilder();
            while ((linha = reader.readLine()) != null) {
                stringBuilder.append(linha).append("\n");
            }
            fileOut.write(stringBuilder.toString().getBytes());
        } catch (FileNotFoundException e) {
            System.err.println("Erro: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    public void realizaAcao(BDManager bdConn, PrintWriter writer) {
        JsonDeserialize j1 = new JsonDeserialize("arquivo_recebido_de_" + nome + ".json");
        switch (j1.getAction()) {
        case "create":
            bdConn.createRecord(j1.getTable(), j1.getData());
            break;
        case "update":
            bdConn.updateRecord(j1.getTable(), j1.getData(), j1.getCondition());
            break;
        case "delete":
            bdConn.deleteRecord(j1.getTable(), j1.getCondition());
            break;
        case "select":
            JSONObject rs = bdConn.selectRecord(j1.getTable(), j1.getCondition());
            System.out.println(rs.toString());
            MsgOut(rs.toString(), writer);
            break;
        default:
            System.out.println("erro ao realizar ação");
            break;
        }
    }
    
    public synchronized void MsgOut(String msg, PrintWriter writer) {
        writer.println(msg);
        writer.flush();
    }
}
