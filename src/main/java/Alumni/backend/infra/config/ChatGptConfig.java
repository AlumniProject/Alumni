package Alumni.backend.infra.config;

public interface ChatGptConfig {
    String HEADER_GPT = "Authorization";
    String GPT_PREFIX = "Bearer ";
    String MODEL = "gpt-3.5-turbo-0613";
    Integer MAX_TOKEN = 300;
    Double TEMPERATURE = 0.6;
    String MEDIA_TYPE = "application/json; charset=UTF-8";
    String URL = "https://api.openai.com/v1/chat/completions";
}
