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
@Table(name = "tipousuariomenuopcion")
public class Tipousuariomenuopcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "acceso")
    private boolean acceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "barraherramienta")
    private boolean barraherramienta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @JoinColumn(name = "idmenuopcion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Menuopcion idmenuopcion;
    @JoinColumn(name = "idtipousuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tipousuario idtipousuario;

    public Tipousuariomenuopcion() {
    }

    public Tipousuariomenuopcion(Integer id) {
        this.id = id;
    }

    public Tipousuariomenuopcion(Integer id, boolean acceso, boolean barraherramienta, boolean estado) {
        this.id = id;
        this.acceso = acceso;
        this.barraherramienta = barraherramienta;
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
        if (!(object instanceof Tipousuariomenuopcion)) {
            return false;
        }
        Tipousuariomenuopcion other = (Tipousuariomenuopcion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Tipousuariomenuopcion[ id=" + id + " ]";
    }
    
}
