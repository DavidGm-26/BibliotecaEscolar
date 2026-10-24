package com.biblioteca.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;

@Service
@Transactional
public class LibroService {

    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public LibroService(LibroRepository libroRepository, PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Transactional(readOnly = true)
    public List<Libro> listar() {
        return libroRepository.findAllByOrderByTituloAsc();
    }

    @Transactional(readOnly = true)
    public List<Libro> listarDisponibles() {
        return libroRepository.findByEjemplaresDisponiblesGreaterThanOrderByTituloAsc(0);
    }

    @Transactional(readOnly = true)
    public List<Libro> buscar(String texto, String categoria, boolean soloDisponibles) {
        String patron = "%" + texto.trim().toLowerCase(Locale.ROOT) + "%";
        return libroRepository.buscar(patron, soloDisponibles ? 1 : 0).stream()
                .filter(libro -> categoria.isBlank() || categoria.equals(libro.getCategoria()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> listarCategorias() {
        return libroRepository.listarCategorias();
    }

    @Transactional(readOnly = true)
    public Libro buscar(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new AppException("El libro solicitado no existe"));
    }

    public void guardar(Libro formulario) {
        validarCodigoUnico(formulario);
        formulario.setCategoria(normalizar(formulario.getCategoria()));
        if (formulario.getId() == null) {
            formulario.setEjemplaresDisponibles(formulario.getEjemplares());
            libroRepository.save(formulario);
            return;
        }
        Libro existente = buscar(formulario.getId());
        int prestados = existente.getEjemplares() - existente.getEjemplaresDisponibles();
        if (formulario.getEjemplares() < prestados) {
            throw new AppException("Hay " + prestados + " ejemplares prestados; no se puede reducir por debajo de esa cantidad");
        }
        existente.setCodigo(formulario.getCodigo());
        existente.setTitulo(formulario.getTitulo());
        existente.setAutor(formulario.getAutor());
        existente.setCategoria(formulario.getCategoria());
        existente.setEjemplares(formulario.getEjemplares());
        existente.setEjemplaresDisponibles(formulario.getEjemplares() - prestados);
    }

    public void eliminar(Long id) {
        Libro libro = buscar(id);
        if (prestamoRepository.existsByLibroId(id)) {
            throw new AppException("No se puede eliminar «" + libro.getTitulo() + "» porque tiene préstamos registrados");
        }
        libroRepository.delete(libro);
    }

    private String normalizar(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    private void validarCodigoUnico(Libro libro) {
        Optional<Libro> repetido = libroRepository.findByCodigo(libro.getCodigo());
        if (repetido.isPresent() && !repetido.get().getId().equals(libro.getId())) {
            throw new AppException("Ya existe un libro con el código " + libro.getCodigo());
        }
    }
}
