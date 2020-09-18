/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author mario
 */
@Data
@Entity
@Table(name = "compra")
@NamedQueries({
    @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c")})
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 3)
    @Column(name = "anulado")
    private String anulado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "correlativo")
    private int correlativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descglobal")
    private double descglobal;
    @Size(max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "docproveedor")
    private String docproveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "domfiscal")
    private String domfiscal;
    @Column(name = "estado")
    private Boolean estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaemision")
    @Temporal(TemporalType.DATE)
    private Date fechaemision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igv")
    private double igv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "importetotal")
    private double importetotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isc")
    private double isc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombreproveedor")
    private String nombreproveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "rucempresa")
    private String rucempresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "serie")
    private String serie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sumaotrostrib")
    private double sumaotrostrib;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sumcargos")
    private double sumcargos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "tipooperacion")
    private String tipooperacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totaldesc")
    private double totaldesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalsinigv")
    private double totalsinigv;
    @Size(max = 90)
    @Column(name = "usuariosave")
    private String usuariosave;
    @Size(max = 90)
    @Column(name = "usuarioupdate")
    private String usuarioupdate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valopeexo")
    private double valopeexo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valoropeinaf")
    private double valoropeinaf;
    @JoinColumn(name = "idmoneda", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Moneda idmoneda;
    @JoinColumn(name = "idtipodocumento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tipodocumento idtipodocumento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcompra")
    private List<Compradet> compradetList;

    public Compra() {
    }

    public Compra(Long id) {
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
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Compra[ id=" + id + " ]";
    }

}
