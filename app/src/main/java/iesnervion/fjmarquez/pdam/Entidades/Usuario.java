package iesnervion.fjmarquez.pdam.Entidades;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

/**
 * Clase destinada a la creacion de objetos para almacenar toda la informacion de un usuario y facilitar su persistencia
 * en la base de datos.
 */
public class Usuario {

    /* ATRIBUTOS */
    private String Uid;
    private String Nombre;
    private String Apellidos;
    private String Email;
    private Integer Altura;
    private Double Peso;
    private int Edad;

    /* CONSTRUCTORES */
    public Usuario(String uid, String nombre, String apellidos, String email, Integer altura, Double peso, Integer edad) {
        this.Uid = uid;
        this.Nombre = nombre;
        this.Apellidos = apellidos;
        this.Email = email;
        this.Altura = altura;
        this.Peso = peso;
        this.Edad = edad;
    }

    public Usuario(FirebaseUser usuario, String nombre, String apellidos, Integer altura, Double  peso, Integer edad){
        this.Uid = usuario.getUid();
        this.Nombre = nombre;
        this.Apellidos = apellidos;
        this.Email = usuario.getEmail();
        this.Altura = altura;
        this.Peso = peso;
        this.Edad = edad;
    }

    public Usuario(){

    }

    /* GETTERS */
    public String getUid() {
        return this.Uid;
    }

    public String getNombre() {
        return this.Nombre;
    }

    public String getEmail() {
        return this.Email;
    }

    public Integer getAltura() {
        return this.Altura;
    }

    public Double getPeso() {
        return this.Peso;
    }

    public int getEdad() {
        return this.Edad;
    }

    /* SETTERS */
    public void setUid(String uid) {
        this.Uid = uid;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public void setAltura(Integer altura) {
        this.Altura = altura;
    }

    public void setPeso(Double peso) {
        this.Peso = peso;
    }

    public void setEdad(int edad) {
        this.Edad = edad;
    }
}
