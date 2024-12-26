package Bibilioteca;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class UsuarioDAO {
    private final EntityManager entityManager;

    public UsuarioDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void crearUsuario(Usuario usuario) {
        entityManager.getTransaction().begin();
        entityManager.persist(usuario);
        entityManager.getTransaction().commit();
    }

    public Usuario buscarPorId(int id) {
        return entityManager.find(Usuario.class, id);
    }

    public Usuario buscarPorDni(String dni) {
        TypedQuery<Usuario> query = entityManager.createQuery(
                "SELECT u FROM Usuario u WHERE u.dni = :dni", Usuario.class
        );
        query.setParameter("dni", dni);
        return query.getSingleResult();
    }

    public List<Usuario> listarUsuarios() {
        return entityManager.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public void actualizarUsuario(Usuario usuario) {
        entityManager.getTransaction().begin();
        entityManager.merge(usuario);
        entityManager.getTransaction().commit();
    }

    public void eliminarUsuario(int id) {
        entityManager.getTransaction().begin();
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            entityManager.remove(usuario);
        }
        entityManager.getTransaction().commit();
    }

    public boolean tienePenalizacionActiva(Usuario usuario) {
        return usuario.getPenalizacionHasta() != null && usuario.getPenalizacionHasta().isAfter(LocalDate.now());
    }
}
