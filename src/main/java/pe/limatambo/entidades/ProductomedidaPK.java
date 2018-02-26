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
import lombok.Data;

/**
 *
 * @author MarioMario
 */
@Data
@Embeddable
public class ProductomedidaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idproducto")
    private int idproducto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idunidadmedida")
    private int idunidadmedida;

    public ProductomedidaPK() {
    }

    public ProductomedidaPK(int idproducto, int idunidadmedida) {
        this.idproducto = idproducto;
        this.idunidadmedida = idunidadmedida;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idproducto;
        hash += (int) idunidadmedida;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductomedidaPK)) {
            return false;
        }
        ProductomedidaPK other = (ProductomedidaPK) object;
        if (this.idproducto != other.idproducto) {
            return false;
        }
        return this.idunidadmedida == other.idunidadmedida;
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.ProductomedidaPK[ idproducto=" + idproducto + ", idunidadmedida=" + idunidadmedida + " ]";
    }
    
}
