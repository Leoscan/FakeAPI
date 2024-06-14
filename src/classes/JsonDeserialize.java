package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONObject;

public class JsonDeserialize {
    private JSONObject jsonObject;
    
    public JsonDeserialize(String fileName) {
        this.jsonObject = new JSONObject(readFile(fileName));
    }
	
	public String getAction() {
        return jsonObject.getString("acao");
	}
	
	public String getTable() {
        return jsonObject.getString("tabela");
	}
	
	public String getCondition() {
        return jsonObject.getString("condicao");
    }
	
	public JSONObject getData() {
        return jsonObject.getJSONObject("dados");
    }


    private String readFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line; 
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}
