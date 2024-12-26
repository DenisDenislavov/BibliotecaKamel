package Bibilioteca;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class EjemplarDAO {
    private final EntityManager entityManager;

    public EjemplarDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void crearEjemplar(Ejemplar ejemplar) {
        entityManager.getTransaction().begin();
        entityManager.persist(ejemplar);
        entityManager.getTransaction().commit();
    }

    public Ejemplar buscarPorId(int id) {
        return entityManager.find(Ejemplar.class, id);
    }

    public List<Ejemplar> listarEjemplaresPorLibro(String isbn) {
        TypedQuery<Ejemplar> query = entityManager.createQuery(
                "SELECT e FROM Ejemplar e WHERE e.libro.isbn = :isbn", Ejemplar.class
        );
        query.setParameter("isbn", isbn);
        return query.getResultList();
    }

    public long contarEjemplaresDisponibles(String isbn) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(e) FROM Ejemplar e WHERE e.libro.isbn = :isbn AND e.estado = :estado", Long.class
        );
        query.setParameter("isbn", isbn);
        query.setParameter("estado", Ejemplar.EstadoEjemplar.DISPONIBLE);
        return query.getSingleResult();
    }
}
