/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MarioMario
 */
@Embeddable
public class MenutipousuarioPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idtipousuario")
    private int idtipousuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idmenu")
    private int idmenu;

    public MenutipousuarioPK() {
    }

    public MenutipousuarioPK(int idtipousuario, int idmenu) {
        this.idtipousuario = idtipousuario;
        this.idmenu = idmenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idtipousuario;
        hash += (int) idmenu;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MenutipousuarioPK)) {
            return false;
        }
        MenutipousuarioPK other = (MenutipousuarioPK) object;
        if (this.idtipousuario != other.idtipousuario) {
            return false;
        }
        return this.idmenu == other.idmenu;
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.MenutipousuarioPK[ idtipousuario=" + idtipousuario + ", idmenu=" + idmenu + " ]";
    }
    
}
