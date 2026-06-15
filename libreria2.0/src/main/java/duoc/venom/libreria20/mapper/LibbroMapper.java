package duoc.venom.libreria20.mapper;

import duoc.venom.libreria20.dto.CreateRequest;
import duoc.venom.libreria20.dto.UpdateRequest;
import duoc.venom.libreria20.model.Libro;

/*
 * Al ser una interfaz con métodos 'static', funciona como una caja de herramientas.
 * Puedes llamar a estos métodos desde cualquier parte de tu código directamente usando
 * LibbroMapper.toModel(...), sin necesidad de crear un objeto de esta interfaz.
 */
public interface LibbroMapper {

    /**
     * Convierte CreateRequest (el DTO) a Libro (el Modelo) para una petición POST (Crear).
     * * @param request El objeto que contiene los datos validados que envió el usuario.
     * @return Un nuevo objeto de tipo Libro listo para ser enviado al Repository.
     */
    public static Libro toModel(CreateRequest request) {
        
        /*
         * Aquí construyes y devuelves una nueva instancia de tu entidad Libro.
         * Tomas los datos que vienen en el 'request' (usando sus métodos) y los pasas al constructor.
         */
        return new Libro(
                0, // ID temporal. Como es un registro nuevo, la BD generará el ID real después.
                request.isbn(), 
                request.titulo(), 
                request.editorial(), 
                request.fechaPublicacion(),
                request.autor()
        );
    }


    /**
     * Convierte UpdateRequest a Libro para una petición PUT (Actualizar).
     * La diferencia aquí es que YA CONOCEMOS el ID del libro que queremos modificar.
     * * @param id El número de identificación que viene de la URL (path parameter).
     * @param request El DTO con los nuevos datos que van a reemplazar a los antiguos.
     * @return El objeto Libro actualizado, listo para sobreescribir el existente en la BD.
     */
    public static Libro toModel(int id, UpdateRequest request) {
        
        return new Libro(
                id, // Aquí SÍ usamos el ID real, para que JPA/Hibernate sepa qué fila actualizar.
                request.isbn(), 
                request.titulo(), 
                request.editorial(), 
                request.fechaPublicacion(),
                request.autor()
        );
    }
}