package Bibilioteca;

import jakarta.persistence.EntityManager;

import java.util.List;

public class LibroDAO {
    private final EntityManager entityManager;

    public LibroDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void crearLibro(Libro libro) {
        entityManager.getTransaction().begin();
        entityManager.persist(libro);
        entityManager.getTransaction().commit();
    }

    public Libro buscarPorIsbn(String isbn) {
        return entityManager.find(Libro.class, isbn);
    }

    public List<Libro> listarLibros() {
        return entityManager.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
    }

    public void eliminarLibro(String isbn) {
        entityManager.getTransaction().begin();
        Libro libro = buscarPorIsbn(isbn);
        if (libro != null) {
            entityManager.remove(libro);
        }
        entityManager.getTransaction().commit();
    }
}
