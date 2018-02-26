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
import lombok.Data;

/**
 *
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "usuarioacceso")
public class Usuarioacceso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @JoinColumn(name = "idtipousuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tipousuario idtipousuario;
    @JoinColumn(name = "idusuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario idusuario;

    public Usuarioacceso() {
    }

    public Usuarioacceso(Integer id) {
        this.id = id;
    }

    public Usuarioacceso(Integer id, boolean estado) {
        this.id = id;
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuarioacceso)) {
            return false;
        }
        Usuarioacceso other = (Usuarioacceso) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Usuarioacceso[ id=" + id + " ]";
    }
    
}
