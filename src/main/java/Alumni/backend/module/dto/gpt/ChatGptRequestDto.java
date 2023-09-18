package Alumni.backend.module.dto.gpt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ChatGptRequestDto implements Serializable {

    private String model;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    private String content;

    public ChatGptRequestDto(String model, Integer maxTokens, Double temperature, String content) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.content = content;
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"model\": \"" + model + "\",\n" +
                "    \"stream\": false,\n" +
                "    \"max_tokens\": " + maxTokens + ",\n" +
                "    \"temperature\": " + temperature + ",\n" +
                "    \"messages\": [{\"role\": \"user\", \"content\": \"" + content + "\"}]\n" +
                "}";
    }
}
