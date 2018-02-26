/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "persona")
public class Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tipopersona")
    private Character tipopersona;
    @Size(max = 30)
    @Column(name = "apellidos")
    private String apellidos;
    @Size(max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "nombrecompleto")
    private String nombrecompleto;
    @Size(max = 11)
    @Column(name = "numdocumento")
    private String numdocumento;
    @Size(max = 100)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 200)
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "coordenadas")
    private Double coordenadas;
    @JoinColumn(name = "idpais", referencedColumnName = "id")
    @ManyToOne
    private Pais idpais;
    @JoinColumn(name = "idtipodocumento", referencedColumnName = "id")
    @ManyToOne
    private Tipodocumento idtipodocumento;
    @JoinColumn(name = "idubigeo", referencedColumnName = "id")
    @ManyToOne
    private Ubigeo idubigeo;

    public Persona() {
    }

    public Persona(Integer id) {
        this.id = id;
    }

    public Persona(Integer id, String nombrecompleto) {
        this.id = id;
        this.nombrecompleto = nombrecompleto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Persona[ id=" + id + " ]";
    }
    
}