package com.biblioteca.seguridad;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Optional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.biblioteca.repository.LectorRepository;

@Service
public class AutenticacionService {

    private static final byte[] SAL = "biblioteca-escolar".getBytes(StandardCharsets.UTF_8);
    private static final int ITERACIONES = 65536;
    private static final int BITS_CLAVE = 256;

    private final LectorRepository lectorRepository;
    private final String usuarioAdministrador;
    private final String claveAdministrador;
    private final String claveAlumno;

    public AutenticacionService(LectorRepository lectorRepository,
            @Value("${biblioteca.admin.usuario:admin}") String usuarioAdministrador,
            @Value("${biblioteca.admin.clave-hash:39cfa9c169f45e9c8d57b68bef15e64b8042a74478ce03bef6939542fafb46d9}") String claveAdministrador,
            @Value("${biblioteca.alumno.clave-hash:f7dee57a81ccf2bedf14fe7ee275293ae87433abf9857388737a330ff707b1d1}") String claveAlumno) {
        this.lectorRepository = lectorRepository;
        this.usuarioAdministrador = usuarioAdministrador;
        this.claveAdministrador = claveAdministrador;
        this.claveAlumno = claveAlumno;
    }

    public Optional<SesionUsuario> autenticar(Rol rol, String usuario, String clave) {
        String usuarioLimpio = usuario.trim();
        if (rol == Rol.ADMINISTRADOR) {
            boolean valido = usuarioAdministrador.equals(usuarioLimpio) && coincide(clave, claveAdministrador);
            return valido
                    ? Optional.of(new SesionUsuario("Administrador", Rol.ADMINISTRADOR, null))
                    : Optional.empty();
        }
        if (!coincide(clave, claveAlumno)) {
            return Optional.empty();
        }
        return lectorRepository.findByCorreo(usuarioLimpio)
                .map(lector -> new SesionUsuario(lector.getNombre(), Rol.ALUMNO, lector.getId()));
    }

    private boolean coincide(String clave, String hashEsperado) {
        try {
            PBEKeySpec especificacion = new PBEKeySpec(clave.toCharArray(), SAL, ITERACIONES, BITS_CLAVE);
            byte[] calculado = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(especificacion).getEncoded();
            return MessageDigest.isEqual(calculado, HexFormat.of().parseHex(hashEsperado));
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            return false;
        }
    }
}
