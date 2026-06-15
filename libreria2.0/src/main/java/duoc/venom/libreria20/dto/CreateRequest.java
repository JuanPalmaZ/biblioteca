package duoc.venom.libreria20.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/*
 * 'record' es una estructura especial de Java (ideal para DTOs). 
 * Detrás de escena, Java genera automáticamente por ti:
 * - Los atributos (como variables privadas y finales).
 * - Un constructor con todos los campos.
 * - Los métodos "getters" (en los records se llaman igual que la variable, ej: isbn()).
 * IMPORTANTE: Los records son inmutables. Una vez creados, sus datos no pueden cambiar 
 * (por eso no tienen "setters").
 */
public record CreateRequest(
        /*
         * @NotBlank es una regla de validación estricta para textos.
         * Valida tres cosas al mismo tiempo:
         * 1. Que el texto no sea nulo (null).
         * 2. Que el texto no esté vacío ("").
         * 3. Que el texto no contenga únicamente espacios en blanco ("   ").
         * El 'message' es el texto de error que se enviará si esta regla no se cumple.
         */
        @NotBlank(message = "ISBN no puede ser vacío")
        String isbn,
        @NotBlank(message = "Título no puede ser vacío")
        String titulo,
        @NotBlank(message = "Editorial no puede ser vacía")
        String editorial,
        /*
         * @PositiveOrZero es una regla de validación para números.
         * Asegura que el valor ingresado sea un número positivo o cero (0, 1, 2, 3...).
         * Previene que alguien envíe un año de publicación negativo (ej: -1990).
         */
        @PositiveOrZero(message = "Año de publicación no puede ser negativo")
        int fechaPublicacion,
        @NotBlank(message = "Autor no puede ser vacío")
        String autor
        ) {

    /* * Las llaves de cierre van vacías porque toda la configuración y declaración 
     * de variables de un 'record' se hace dentro de los paréntesis principales. 
     */
}
