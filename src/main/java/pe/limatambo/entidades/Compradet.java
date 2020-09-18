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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author mario
 */
@Data
@Entity
@Table(name = "compradet")
@NamedQueries({
    @NamedQuery(name = "Compradet.findAll", query = "SELECT c FROM Compradet c")})
public class Compradet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "afectacionigv")
    private String afectacionigv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private double cantidad;
    @Size(max = 20)
    @Column(name = "codproductosunat")
    private String codproductosunat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descuentototal")
    private double descuentototal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descuentounitario")
    private double descuentounitario;
    @Column(name = "estado")
    private Boolean estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igvitem")
    private double igvitem;
    @Size(max = 255)
    @Column(name = "imeichip")
    private String imeichip;
    @Size(max = 255)
    @Column(name = "imeiequipo")
    private String imeiequipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscitem")
    private double iscitem;
    @Size(max = 255)
    @Column(name = "numero_chip")
    private String numeroChip;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciototal")
    private double preciototal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciototalsinigv")
    private double preciototalsinigv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciounitario")
    private double preciounitario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "tiposistemaisc")
    private String tiposistemaisc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorunitariosinigv")
    private double valorunitariosinigv;
    @JoinColumn(name = "idcompra", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Compra idcompra;
    @JoinColumn(name = "idproducto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto idproducto;
    @JoinColumn(name = "idunidadmedida", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Unidadmedida idunidadmedida;

    public Compradet() {
    }

    public Compradet(Long id) {
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compradet)) {
            return false;
        }
        Compradet other = (Compradet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Compradet[ id=" + id + " ]";
    }

}
