package Bibilioteca;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class PrestamoDAO {
    private final EntityManager entityManager;

    public PrestamoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void registrarPrestamo(Prestamo prestamo) {
        Usuario usuario = prestamo.getUsuario();
        Ejemplar ejemplar = prestamo.getEjemplar();

        if (usuario.getPenalizacionHasta() != null && usuario.getPenalizacionHasta().isAfter(LocalDate.now())) {
            throw new IllegalStateException("El usuario tiene una penalización activa.");
        }

        if (contarPrestamosActivos(usuario) >= 3) {
            throw new IllegalStateException("El usuario ya tiene 3 préstamos activos.");
        }

        if (!ejemplar.getEstado().equals(Ejemplar.EstadoEjemplar.DISPONIBLE)) {
            throw new IllegalStateException("El ejemplar no está disponible.");
        }

        ejemplar.setEstado(Ejemplar.EstadoEjemplar.PRESTADO);
        prestamo.setFechaInicio(LocalDate.now());
        entityManager.getTransaction().begin();
        entityManager.persist(prestamo);
        entityManager.merge(ejemplar);
        entityManager.getTransaction().commit();
    }

    public List<Prestamo> listarPrestamosActivos(Usuario usuario) {
        TypedQuery<Prestamo> query = entityManager.createQuery(
                "SELECT p FROM Prestamo p WHERE p.usuario = :usuario AND p.fechaDevolucion IS NULL", Prestamo.class
        );
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }

    public long contarPrestamosActivos(Usuario usuario) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Prestamo p WHERE p.usuario = :usuario AND p.fechaDevolucion IS NULL", Long.class
        );
        query.setParameter("usuario", usuario);
        return query.getSingleResult();
    }

    public void registrarDevolucion(int prestamoId, LocalDate fechaDevolucion) {
        entityManager.getTransaction().begin();
        try {
            Prestamo prestamo = entityManager.find(Prestamo.class, prestamoId);
            if (prestamo == null) {
                throw new IllegalArgumentException("Préstamo no encontrado con ID: " + prestamoId);
            }

            // Verificar si ya se devolvió
            if (prestamo.getFechaDevolucion() != null) {
                throw new IllegalStateException("Este préstamo ya ha sido devuelto.");
            }

            prestamo.setFechaDevolucion(fechaDevolucion);

            Ejemplar ejemplar = prestamo.getEjemplar();
            ejemplar.setEstado(Ejemplar.EstadoEjemplar.DISPONIBLE);
            entityManager.merge(ejemplar);

            if (fechaDevolucion.isAfter(prestamo.getFechaInicio().plusDays(15))) {
                Usuario usuario = prestamo.getUsuario();
                long diasRetraso = fechaDevolucion.toEpochDay() - prestamo.getFechaInicio().plusDays(15).toEpochDay();
                int diasPenalizacion = (int) diasRetraso * 15;
                usuario.setPenalizacionHasta(LocalDate.now().plusDays(diasPenalizacion));
                entityManager.merge(usuario);
            }

            entityManager.merge(prestamo);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

}
