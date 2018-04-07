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
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "venta")
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v")})
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "serie")
    private String serie;
    @Basic(optional = false)
    @Column(name = "correlativo")
    private Integer correlativo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "rucempresa")
    private String rucempresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "tipooperacion")
    private String tipooperacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaemision")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaemision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "domfiscal")
    private String domfiscal;
    @JoinColumn(name = "idtipodocumento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tipodocumento idtipodocumento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "doccliente")
    private String doccliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombrecliente")
    private String nombrecliente;
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "idmoneda", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Moneda idmoneda;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descglobal")
    private double descglobal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sumcargos")
    private double sumcargos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totaldesc")
    private double totaldesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalsinigv")
    private double totalsinigv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igv")
    private double igv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valoropeinaf")
    private double valoropeinaf;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valopeexo")
    private double valopeexo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isc")
    private double isc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sumaotrostrib")
    private double sumaotrostrib;
    @Basic(optional = false)
    @NotNull
    @Column(name = "importetotal")
    private double importetotal;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 90)
    @Column(name = "usuariosave")
    private String usuariosave;
    @Size(max = 90)
    @Column(name = "usuarioupdate")
    private String usuarioupdate;
    @Size(max = 3)
    @Column(name = "anulado")
    private String anulado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idventa")
    private List<Ventadet> ventadetList;

    public Venta() {
    }

    public Venta(Long id) {
        this.id = id;
    }

    public Venta(Long id, String serie, Integer correlativo, String rucempresa, String tipooperacion, Date fechaemision, String domfiscal, Tipodocumento tipodocumento, String doccliente, String nombrecliente, Moneda codmoneda, double descglobal, double sumcargos, double totaldesc, double totalsinigv, double igv, double valoropeinaf, double valopeexo, double isc, double sumaotrostrib, double importetotal) {
        this.id = id;
        this.serie = serie;
        this.correlativo = correlativo;
        this.rucempresa = rucempresa;
        this.tipooperacion = tipooperacion;
        this.fechaemision = fechaemision;
        this.domfiscal = domfiscal;
        this.idtipodocumento = tipodocumento;
        this.doccliente = doccliente;
        this.nombrecliente = nombrecliente;
        this.idmoneda = codmoneda;
        this.descglobal = descglobal;
        this.sumcargos = sumcargos;
        this.totaldesc = totaldesc;
        this.totalsinigv = totalsinigv;
        this.igv = igv;
        this.valoropeinaf = valoropeinaf;
        this.valopeexo = valopeexo;
        this.isc = isc;
        this.sumaotrostrib = sumaotrostrib;
        this.importetotal = importetotal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Venta[ id=" + id + " ]";
    }
    
}
