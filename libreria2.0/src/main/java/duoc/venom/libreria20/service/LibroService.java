package duoc.venom.libreria20.service;

import java.util.List;

import org.springframework.stereotype.Service;

import duoc.venom.libreria20.dto.CreateRequest;
import duoc.venom.libreria20.dto.UpdateRequest;
import duoc.venom.libreria20.mapper.LibbroMapper;
import duoc.venom.libreria20.model.Libro;
import duoc.venom.libreria20.repository.LibroRepo;

/*
 * @Service le indica a Spring Boot que esta clase contiene la "Lógica de Negocio".
 * Es el cerebro de la operación: recibe peticiones, toma decisiones, usa el Mapper 
 * para traducir datos y finalmente le da órdenes al Repository.
 */
@Service
public class LibroService {

    /*
     * Usamos 'private final' en lugar de @Autowired. 
     * Esto obliga a que el Service reciba el Repository al momento de nacer (en el constructor).
     * Es la mejor práctica actual porque hace que el código sea más seguro y rápido.
     */
    private final LibroRepo libroRepository;

    public LibroService(LibroRepo libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> getLibros() {
        return libroRepository.findAll();
    }

    public Libro getLibroId(int id) {
        return libroRepository.findById(id).orElse(null);
    }

    /* ========================================================================
     * MÉTODOS DE ESCRITURA (Usando tus DTOs y Mapper)
     * ======================================================================== */

    /**
     * Lógica para CREAR un libro (POST).
     */
    
    
    public Libro saveLibro(CreateRequest request) {
        /*
         * PASO 1: El Controller nos envía un 'CreateRequest' (datos ya validados).
         * Usamos tu herramienta LibbroMapper.toModel() para traducir ese DTO 
         * en un objeto 'Libro' que la base de datos sí pueda entender.
         * El Mapper le asignará temporalmente un ID = 0.
         */
        Libro nuevoLibro = LibbroMapper.toModel(request);

        /*
         * PASO 2: Le entregamos la entidad ya armada al Repository.
         * El .save() lo insertará en la tabla y la base de datos le generará su ID real.
         */
        return libroRepository.save(nuevoLibro);
    }

    /**
     * Lógica para ACTUALIZAR un libro (PUT).
     */
    public Libro updateLibro(int id, UpdateRequest request) {
        /*
         * PASO 1: Seguridad primero. Verificamos si el libro realmente existe 
         * en la base de datos antes de intentar actualizarlo.
         * Si no existe, devolvemos null (o podrías lanzar un error).
         */
        if (!libroRepository.existsById(id)) {
            return null; 
        }
        
        /*
         * PASO 2: Como sabemos que sí existe, usamos el segundo método de tu Mapper.
         * Le pasamos el ID real de la URL y los datos nuevos del UpdateRequest.
         * El Mapper arma la entidad exacta que queremos sobreescribir.
         */
        Libro libroActualizado = LibbroMapper.toModel(id, request);

        /*
         * PASO 3: Al usar .save() con un objeto que YA TIENE un ID que existe,
         * Spring Boot entiende que no debe crear uno nuevo, sino hacer un UPDATE.
         */
        return libroRepository.save(libroActualizado);
    }

    public List<Libro> obtenerPorEditorial(String editorial) {
        return libroRepository.findByEditorial(editorial);
    }

    /* ========================================================================
     * MÉTODOS DE BORRADO Y CÁLCULO
     * ======================================================================== */

    public String deleteLibro(int id) {
        libroRepository.deleteById(id);
        return "Libro eliminado";
    }

    public int totalLibros() {
        return (int) libroRepository.count();
    }
}