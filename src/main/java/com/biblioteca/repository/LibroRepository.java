package com.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biblioteca.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findAllByOrderByTituloAsc();

    List<Libro> findByEjemplaresDisponiblesGreaterThanOrderByTituloAsc(int minimo);

    Optional<Libro> findByCodigo(String codigo);

    @Query("""
            select l from Libro l
            where (lower(l.titulo) like :patron
                   or lower(l.autor) like :patron
                   or lower(coalesce(l.categoria, '')) like :patron)
              and l.ejemplaresDisponibles >= :minimoDisponibles
            order by l.titulo
            """)
    List<Libro> buscar(@Param("patron") String patron, @Param("minimoDisponibles") int minimoDisponibles);

    @Query("select distinct l.categoria from Libro l where l.categoria is not null order by l.categoria")
    List<String> listarCategorias();
}
