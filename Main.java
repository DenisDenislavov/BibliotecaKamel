package Bibilioteca;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Biblioteca");
        EntityManager em = emf.createEntityManager();

        UsuarioDAO usuarioDAO = new UsuarioDAO(em);
        LibroDAO libroDAO = new LibroDAO(em);
        EjemplarDAO ejemplarDAO = new EjemplarDAO(em);
        PrestamoDAO prestamoDAO = new PrestamoDAO(em);

        try {
            // CRUD
            Usuario usuario1 = new Usuario("12345678A", "Denis Denis", "denis.denis@gmail.com", "pass1234", Usuario.TipoUsuario.NORMAL);
            Usuario usuario2 = new Usuario("87654321B", "Pedro Pedro", denis.denis@gmail.com", "pass1234", Usuario.TipoUsuario.NORMAL);
            usuarioDAO.crearUsuario(usuario1);
            usuarioDAO.crearUsuario(usuario2);

            Libro libro1 = new Libro("9781234567890", "El Quijote", "Miguel de Cervantes");
            Libro libro2 = new Libro("9789876543210", "Cien Años de Soledad", "Gabriel García Márquez");
            libroDAO.crearLibro(libro1);
            libroDAO.crearLibro(libro2);

            Ejemplar ejemplar1 = new Ejemplar(libro1, Ejemplar.EstadoEjemplar.DISPONIBLE);
            Ejemplar ejemplar2 = new Ejemplar(libro1, Ejemplar.EstadoEjemplar.DISPONIBLE);
            Ejemplar ejemplar3 = new Ejemplar(libro1, Ejemplar.EstadoEjemplar.PRESTADO);
            ejemplarDAO.crearEjemplar(ejemplar1);
            ejemplarDAO.crearEjemplar(ejemplar2);
            ejemplarDAO.crearEjemplar(ejemplar3);

            System.out.println("Registrando un préstamo...");
            Prestamo prestamo1 = new Prestamo(usuario1, ejemplar1);
            prestamoDAO.registrarPrestamo(prestamo1);

            // Registros / Más de 3 préstamos para un usuario
            try {
                Prestamo prestamo2 = new Prestamo(usuario1, ejemplar2);
                Prestamo prestamo3 = new Prestamo(usuario1, ejemplar3);
                prestamoDAO.registrarPrestamo(prestamo2);
                prestamoDAO.registrarPrestamo(prestamo3);

                Prestamo prestamo4 = new Prestamo(usuario1, ejemplar1);
                prestamoDAO.registrarPrestamo(prestamo4); 
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("Registrando una devolución...");
            prestamoDAO.registrarDevolucion(prestamo1.getId(), LocalDate.now().plusDays(20));

            Usuario usuarioConPenalizacion = usuarioDAO.buscarPorId(usuario1.getId());
            if (usuarioConPenalizacion.getPenalizacionHasta() != null) {
                System.out.println("Usuario penalizado hasta: " + usuarioConPenalizacion.getPenalizacionHasta());
            }

            System.out.println("Listando préstamos activos del usuario:");
            for (Prestamo p : prestamoDAO.listarPrestamosActivos(usuario1)) {
                System.out.println("Préstamo ID: " + p.getId() + ", Ejemplar ID: " + p.getEjemplar().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
