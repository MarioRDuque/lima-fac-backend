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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "inventariototal")
public class Inventariototal implements Serializable {
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
    @Column(name = "feccompra")
    @Temporal(TemporalType.DATE)
    private Date feccompra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costopromedio")
    private BigDecimal costopromedio;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "precioventa")
    private BigDecimal precioventa;
    @Column(name = "preciokairos")
    private BigDecimal preciokairos;
    @JoinColumn(name = "idalmacen", referencedColumnName = "id")
    @ManyToOne
    private Almacen idalmacen;
    @JoinColumn(name = "idproducto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto idproducto;

    public Inventariototal() {
    }

    public Inventariototal(Integer id) {
        this.id = id;
    }

    public Inventariototal(Integer id, short anio, Date feccompra, BigDecimal stockinicial, BigDecimal salida, BigDecimal precioventa) {
        this.id = id;
        this.anio = anio;
        this.feccompra = feccompra;
        this.stockinicial = stockinicial;
        this.salida = salida;
        this.precioventa = precioventa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Inventariototal)) {
            return false;
        }
        Inventariototal other = (Inventariototal) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Inventariototal[ id=" + id + " ]";
    }
    
}
