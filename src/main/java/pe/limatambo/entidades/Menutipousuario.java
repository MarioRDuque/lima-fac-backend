/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "menutipousuario")
public class Menutipousuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MenutipousuarioPK menutipousuarioPK;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "idmenu", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Menu menu;

    public Menutipousuario() {
    }

    public Menutipousuario(MenutipousuarioPK menutipousuarioPK) {
        this.menutipousuarioPK = menutipousuarioPK;
    }

    public Menutipousuario(int idtipousuario, int idmenu) {
        this.menutipousuarioPK = new MenutipousuarioPK(idtipousuario, idmenu);
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (menutipousuarioPK != null ? menutipousuarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Menutipousuario)) {
            return false;
        }
        Menutipousuario other = (Menutipousuario) object;
        return !((this.menutipousuarioPK == null && other.menutipousuarioPK != null) || (this.menutipousuarioPK != null && !this.menutipousuarioPK.equals(other.menutipousuarioPK)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Menutipousuario[ menutipousuarioPK=" + menutipousuarioPK + " ]";
    }
    
}
