package Bibilioteca;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

    private LocalDate penalizacionHasta;

    public Usuario(String dni, String nombre, String email, String password, TipoUsuario tipo) {
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.tipo = tipo;
    }

    public Usuario() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public LocalDate getPenalizacionHasta() {
        return penalizacionHasta;
    }

    public void setPenalizacionHasta(LocalDate penalizacionHasta) {
        this.penalizacionHasta = penalizacionHasta;
    }

    public enum TipoUsuario {
        NORMAL,
        ADMINISTRADOR
    }

}