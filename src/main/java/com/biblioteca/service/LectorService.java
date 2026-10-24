package com.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.model.Lector;
import com.biblioteca.repository.LectorRepository;
import com.biblioteca.repository.PrestamoRepository;

@Service
@Transactional
public class LectorService {

    private final LectorRepository lectorRepository;
    private final PrestamoRepository prestamoRepository;

    public LectorService(LectorRepository lectorRepository, PrestamoRepository prestamoRepository) {
        this.lectorRepository = lectorRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Transactional(readOnly = true)
    public List<Lector> listar() {
        return lectorRepository.findAllByOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public Lector buscar(Long id) {
        return lectorRepository.findById(id)
                .orElseThrow(() -> new AppException("El lector solicitado no existe"));
    }

    public void guardar(Lector lector) {
        Optional<Lector> repetido = lectorRepository.findByCorreo(lector.getCorreo());
        if (repetido.isPresent() && !repetido.get().getId().equals(lector.getId())) {
            throw new AppException("Ya existe un lector con el correo " + lector.getCorreo());
        }
        lectorRepository.save(lector);
    }

    public void eliminar(Long id) {
        Lector lector = buscar(id);
        if (prestamoRepository.existsByLectorId(id)) {
            throw new AppException("No se puede eliminar a " + lector.getNombre() + " porque tiene préstamos registrados");
        }
        lectorRepository.delete(lector);
    }
}
