/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "inventario")
public class Inventario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "mes")
    private Short mes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private short anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecvencim")
    @Temporal(TemporalType.DATE)
    private Date fecvencim;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "stockinicial")
    private BigDecimal stockinicial;
    @Column(name = "entrada")
    private BigDecimal entrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "salida")
    private BigDecimal salida;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "idalmacen", referencedColumnName = "id")
    @ManyToOne
    private Almacen idalmacen;
    @JoinColumn(name = "idproducto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto idproducto;

    public Inventario() {
    }

    public Inventario(Integer id) {
        this.id = id;
    }

    public Inventario(Integer id, short anio, Date fecvencim, BigDecimal stockinicial, BigDecimal salida) {
        this.id = id;
        this.anio = anio;
        this.fecvencim = fecvencim;
        this.stockinicial = stockinicial;
        this.salida = salida;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Inventario)) {
            return false;
        }
        Inventario other = (Inventario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Inventario[ id=" + id + " ]";
    }
    
}
