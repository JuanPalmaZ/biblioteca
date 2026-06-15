package duoc.venom.libreria20.exception;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * @RestControllerAdvice es un "Interceptor Global".
 * Imaginalo como un guardia de seguridad que rodea a todos tus Controllers.
 * Si ocurre un error en cualquier parte, este guardia atrapa la excepción 
 * y decide qué respuesta (JSON) devolverle al cliente para que la App no "explote".
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        // Log para confirmar en consola que el sistema de seguridad está activo.
        System.out.println("✅ El Supervisor Global de Errores se ha activado correctamente.");
    }

    /* ========================================================================
     * 1. ERRORES DE VALIDACIÓN (Los que vienen de tus Records con @Valid)
     * ======================================================================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {

        /*
         * ProblemDetail es el estándar moderno (Spring Boot 3+) para reportar errores.
         * Crea un objeto JSON organizado con el estado (400 Bad Request) y un mensaje.
         */
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Los datos enviados no cumplen con las reglas de validación.");

        problem.setTitle("Error de Validación");
        problem.setProperty("timestamp", Instant.now()); // Registra el momento exacto del error

        /*
         * PASO CLAVE: Convertimos la lista de errores interna de Spring en un Mapa fácil de leer.
         * Por ejemplo: { "isbn": "ISBN no puede ser vacío", "titulo": "Título es obligatorio" }
         */
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Dato inválido"
                ));

        // Metemos ese mapa de errores dentro del reporte final.
        problem.setProperty("errors", errors);

        return problem;
    }

    /* ========================================================================
     * 2. ERRORES DE SINTAXIS JSON (JSON mal escrito)
     * ======================================================================== */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseError(HttpMessageNotReadableException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición (JSON) está mal formado o tiene errores de sintaxis.");

        problem.setTitle("Error de Lectura JSON");
        problem.setProperty("timestamp", Instant.now());
        // Agregamos el detalle técnico del error (ej: falta una coma o una llave)
        problem.setProperty("detalle_tecnico", ex.getMostSpecificCause().getMessage());

        return problem;
    }

    /* ========================================================================
     * 3. RECURSO NO ENCONTRADO (Error 404 personalizado)
     * ======================================================================== */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {

        // Usamos HttpStatus.NOT_FOUND porque el libro simplemente no existe en la BD.
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());

        problem.setTitle("Libro No Encontrado");
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    /* ========================================================================
     * 4. ERROR GENÉRICO (La red de seguridad final)
     * ======================================================================== */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {

        /*
         * Si ocurre un error que no previmos (ej: se desconectó la base de datos),
         * este método evita que el usuario vea código interno feo y le da un 
         * mensaje controlado (500 Internal Server Error).
         */
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ha ocurrido un error inesperado en el servidor.");

        problem.setTitle("Error Interno");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("tipo_excepcion", ex.getClass().getSimpleName());

        return problem;
    }
}
