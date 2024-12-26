package Bibilioteca;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Libro")
public class Libro {
    @Id
    @Column(length = 13)
    private String isbn;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private List<Ejemplar> ejemplares = new ArrayList<>();

    public Libro(String isbn, String titulo, String autor) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
    }

    public Libro() {

    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<Ejemplar> getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(List<Ejemplar> ejemplares) {
        this.ejemplares = ejemplares;
    }
}
