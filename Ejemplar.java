package Bibilioteca;

import jakarta.persistence.*;

@Entity
@Table(name = "Ejemplar")
public class Ejemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "isbn", nullable = false)
    private Libro libro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEjemplar estado;

    @OneToOne(mappedBy = "ejemplar", cascade = CascadeType.ALL)
    private Prestamo prestamo;

    public Ejemplar(Libro libro, EstadoEjemplar estado) {
        this.libro = libro;
        this.estado = estado;
    }

    public Ejemplar() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public EstadoEjemplar getEstado() {
        return estado;
    }

    public void setEstado(EstadoEjemplar estado) {
        this.estado = estado;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public enum EstadoEjemplar {
        DISPONIBLE,
        PRESTADO,
        DAÑADO
    }

}
