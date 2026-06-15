package duoc.venom.libreria20.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import duoc.venom.libreria20.dto.CreateRequest;
import duoc.venom.libreria20.dto.PokemonResponse;
import duoc.venom.libreria20.dto.RickResponse;
import duoc.venom.libreria20.dto.UpdateRequest;
import duoc.venom.libreria20.model.Libro;
import duoc.venom.libreria20.service.LibroService;
import duoc.venom.libreria20.exception.ResourceNotFoundException;

import jakarta.validation.Valid;

/**
 * @RestController: Mezcla @Controller y @ResponseBody. Le dice a Spring que
 * esta clase es el punto de entrada para llamadas HTTP y que
 * las respuestas se enviarán directamente en formato JSON.
 * @RequestMapping("/api/v1"): Define la "dirección base" de este controlador.
 */
@RestController
@RequestMapping("/api/v1")
public class LibroController {

    private final LibroService libroService;
    private final RestClient pokeApiClient;
    private final RestClient rickApiClient;

    /*
     * CONSTRUCTOR: Spring Boot detecta los nombres 'pokeApiClient' y 'rickApiClient'
     * y les inyecta automáticamente los Beans configurados en tu RestClientConfig.
     */
    public LibroController(LibroService libroService, RestClient pokeApiClient, RestClient rickApiClient) {
        this.libroService = libroService;
        this.pokeApiClient = pokeApiClient;
        this.rickApiClient = rickApiClient;
    }

    /*
     * ========================================================================
     * OPERACIONES CRUD (Create, Read, Update, Delete)
     * ========================================================================
     */
    
    /**
     * @GetMapping: Responde a peticiones GET (Listar todo). Retornamos un
     * ResponseEntity para tener control total sobre el código de estado (200 OK).
     */
    @GetMapping
    public ResponseEntity<List<Libro>> listarLibros() {
        List<Libro> libros = libroService.getLibros();
        return ResponseEntity.ok(libros);
    }

    /**
     * @PostMapping: Para crear nuevos recursos.
     * @Valid: Activa las reglas que pusiste en tu Record (ej: @NotBlank). Si
     * falla, ni siquiera entra al método y devuelve un error 400 automáticamente.
     * @RequestBody: Toma el JSON que envía el cliente y lo convierte en un objeto 'CreateRequest'.
     */
    @PostMapping
    public ResponseEntity<Libro> agregarLibro(@Valid @RequestBody CreateRequest request) {
        Libro nuevoLibro = libroService.saveLibro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
    }

    /**
     * @GetMapping("/{id}"): El {id} es una variable que viene en la URL.
     * @PathVariable: Extrae ese valor de la URL y lo mete en la variable 'id' del método.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarLibro(@PathVariable int id) {
        Libro libro = libroService.getLibroId(id);

        if (libro == null) {
            throw new ResourceNotFoundException("No encontrado");
        }

        return ResponseEntity.ok(libro);
    }

    /**
     * @PutMapping("/{id}"): Para actualizaciones totales. Recibimos el ID por
     * URL y los nuevos datos por el Body.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable int id, @Valid @RequestBody UpdateRequest request) {
         Libro actualizado = libroService.updateLibro(id, request);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    /**
     * @DeleteMapping("/{id}"): Para borrar registros. Retornamos 204 No Content
     * porque la operación fue exitosa pero ya no hay nada que mostrar.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable int id) {
        libroService.deleteLibro(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * ========================================================================
     * OTROS SERVICIOS Y CONSUMO DE APIS EXTERNAS
     * ========================================================================
     */
    
    /**
     * Devuelve un número simple con el conteo de libros.
     */
    @GetMapping("/total")
    public ResponseEntity<Integer> totalLibros() {
        return ResponseEntity.ok(libroService.totalLibros());
    }

    @GetMapping("/editorial/{editorial}")
    public ResponseEntity<List<Libro>> getPorEditorial(@PathVariable String editorial) {
        List<Libro> libros = libroService.obtenerPorEditorial(editorial);

        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(libros);
    }

    @GetMapping("/editorial")
    public List<Libro> getporEditorial2(@RequestParam String editorial) {
        return libroService.obtenerPorEditorial(editorial);
    }

    /**
     * Consumo de API externa (PokeAPI). Usamos RestClient de forma síncrona,
     * ideal para trabajar con los Hilos Virtuales de tu configuración.
     * Ejemplo: /api/v1/pokeapi/pikachu
     */
    @GetMapping("/pokeapi/{nombre}")
    public ResponseEntity<PokemonResponse> consultarPokemon(@PathVariable String nombre) {
        PokemonResponse pokemon = pokeApiClient.get()
                .uri("/pokemon-species/{nombre}", nombre.toLowerCase().trim())
                .retrieve()
                .body(PokemonResponse.class);

        return ResponseEntity.ok(pokemon);
    }

    /**
     * Consumo de API externa (Rick and Morty).
     * @PathVariable: Extrae el valor dinámico "{id}" directamente desde la URL.
     */
    @GetMapping("/RickApi/{id}")
    public ResponseEntity<RickResponse> consultarRicky(@PathVariable int id) {
        RickResponse rick = rickApiClient.get()
                .uri("/character/{id}", id)
                .retrieve()
                .body(RickResponse.class);

        return ResponseEntity.ok(rick);
    }
}