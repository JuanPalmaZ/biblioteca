package duoc.venom.libreria20.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient pokeApiClient() {
        return RestClient.builder()
                .baseUrl("https://pokeapi.co/api/v2")
                .build();
    }

    @Bean
    public RestClient rickApiClient() {
        return RestClient.builder()
                .baseUrl("https://rickandmortyapi.com/api")
                .build();
    }
}