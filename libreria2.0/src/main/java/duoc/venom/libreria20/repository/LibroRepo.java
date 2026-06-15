package duoc.venom.libreria20.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duoc.venom.libreria20.model.Libro;
@Repository
public interface LibroRepo extends JpaRepository <Libro,Integer> {
    List<Libro> findByEditorial(String editorial);
    default int totalLibros() {
        return (int) this.count(); // ← "this" se refiere a la instancia del repository
    }

}
