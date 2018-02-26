/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "tarifario")
public class Tarifario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "tipo")
    private Character tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puntos")
    private BigDecimal puntos;
    @Column(name = "idcobertura")
    private Integer idcobertura;
    @Column(name = "idservicio")
    private Integer idservicio;
    @Column(name = "estado")
    private Boolean estado;

    public Tarifario() {
    }

    public Tarifario(Integer id) {
        this.id = id;
    }

    public Tarifario(Integer id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tarifario)) {
            return false;
        }
        Tarifario other = (Tarifario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Tarifario[ id=" + id + " ]";
    }
    
}
