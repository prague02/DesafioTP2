package fatecpg.ConsomeGemini;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsomeGemini {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=API";
    private static final Pattern RESPOSTA_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"([^\"]+)\"");
    private static final String LOG_FILE = "chat_log.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Chatbot Gemini iniciado. Faça sua pergunta ou 'fatec_sair' para sair.");

        while (true) {
            System.out.print("Você: ");
            String pergunta = scanner.nextLine();
            if (pergunta.equalsIgnoreCase("fatec_sair")) {
                System.out.println("Chatbot encerrado.");
                break;
            }

            try {
                String resposta = fazerPergunta(pergunta);
                System.out.println("Gemini: " + resposta);
                salvarNoLog("Usuário", pergunta);
                salvarNoLog("Gemini", resposta);
            } catch (IOException | InterruptedException e) {
                System.err.println("Erro ao acessar a API. Tente novamente mais tarde.");
            }
        }

        scanner.close();
    }

    public static String fazerPergunta(String pergunta) throws IOException, InterruptedException {
        String jsonRequest = gerarJsonRequest(pergunta);
        HttpResponse<String> respostaJson = enviarRequisicao(jsonRequest);
        return extrairResposta(respostaJson.body());
    }

    private static String gerarJsonRequest(String pergunta) {
        return pergunta;
    }

    private static HttpResponse<String> enviarRequisicao(String jsonRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String extrairResposta(String respostaJson) {
        Matcher matcher = RESPOSTA_PATTERN.matcher(respostaJson);
        return matcher.find() ? matcher.group(1) : "Não foi possível encontrar uma resposta";
    }

    private static void salvarNoLog(String autor, String mensagem) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true); PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println("[" + timestamp + "] " + autor + ": " + mensagem);
        } catch (IOException e) {
            System.out.println("Erro ao salvar no log: " + e.getMessage());
        }
    }
}
