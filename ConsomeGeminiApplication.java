package fatecpg.ConsomeGemini;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsomeGeminiApplication implements CommandLineRunner {

    @Autowired
    private fatecpg.ConsomeGemini.ConsomeGemini ConsomeGemini;

    public static void main(String[] args) {
        SpringApplication.run(ConsomeGeminiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        ConsomeGemini.iniciarChat();
    }
}
