package duoc.venom.libreria20.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter // Genera solo los Getters
@Setter // Genera solo los Setters
@NoArgsConstructor // Genera el constructor vacío (requerido por JPA)
@AllArgsConstructor // Genera el constructor con todos los atributos
/*
 * Le dice a Hibernate que esta clase es un modelo que debe transformarse en una
 * tabla de base de datos.
 */
@Entity
@Table(name = "libros") /* Define específicamente que la tabla se llamará "libros". */
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*
     * @Id y @GeneratedValue: Marcan el campo id como la llave primaria y le indican
     * a la base de datos que ella debe generar el número automáticamente
     */
    @Column(name = "id")
    /*
     * Configura las restricciones de cada columna (nombre, si permite nulos y el
     * largo máximo del texto).
     */
    private int id;
    @Column(name = "isbn", nullable = false, length = 50)
    private String isbn;
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;
    @Column(name = "editorial", nullable = false, length = 100)
    private String editorial;
    @Column(name = "fecha_publicacion", nullable = false)
    private int fechaPublicacion;
    @Column(name = "autor", nullable = false, length = 150)
    private String autor;


}
