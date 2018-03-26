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
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "ventadet")
@NamedQueries({
    @NamedQuery(name = "Ventadet.findAll", query = "SELECT v FROM Ventadet v")})
public class Ventadet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private double cantidad;
    @Size(max = 20)
    @Column(name = "codproductosunat")
    private String codproductosunat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciounitario")
    private double preciounitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descuentounitario")
    private double descuentounitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descuentototal")
    private double descuentototal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igvitem")
    private double igvitem;
    @Basic(optional = false)
    @NotNull
    @Column(name = "afectacionigv")
    private String afectacionigv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscitem")
    private double iscitem;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "tiposistemaisc")
    private String tiposistemaisc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorunitariosinigv")
    private double valorunitariosinigv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciototalsinigv")
    private double preciototalsinigv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preciototal")
    private double preciototal;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "idproducto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto idproducto;
    @Column(name = "idventa", nullable = false)
    private Long idventa;
    @JoinColumn(name = "idunidadmedida", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Unidadmedida idunidadmedida;

    public Ventadet() {
    }

    public Ventadet(Long id) {
        this.id = id;
    }

    public Ventadet(Long id, double cantidad, double preciounitario, double descuentototal, double igvitem, String afectacionigv, double iscitem, String tiposistemaisc, double valorunitariosinigv, double preciototal) {
        this.id = id;
        this.cantidad = cantidad;
        this.preciounitario = preciounitario;
        this.descuentototal = descuentototal;
        this.igvitem = igvitem;
        this.afectacionigv = afectacionigv;
        this.iscitem = iscitem;
        this.tiposistemaisc = tiposistemaisc;
        this.valorunitariosinigv = valorunitariosinigv;
        this.preciototal = preciototal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Ventadet)) {
            return false;
        }
        Ventadet other = (Ventadet) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Ventadet[ id=" + id + " ]";
    }
    
}
