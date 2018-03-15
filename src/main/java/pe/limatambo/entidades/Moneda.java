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
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

/**
 *
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "moneda")
public class Moneda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 10)
    @Column(name = "definicion")
    private String definicion;
    @Size(max = 5)
    @Column(name = "simbolo")
    private String simbolo;
    @Size(max = 5)
    @NonNull
    @Column(name = "simbolosunat")
    private String simbolosunat;

    public Moneda() {
    }

    public Moneda(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Moneda)) {
            return false;
        }
        Moneda other = (Moneda) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Moneda[ id=" + id + " ]";
    }
    
}
