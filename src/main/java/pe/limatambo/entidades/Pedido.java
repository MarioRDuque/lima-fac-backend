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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author MarioMario
 */
@Data
@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fechapedido")
    @Temporal(TemporalType.DATE)
    private Date fechapedido;
    @Column(name = "fechaentrega")
    @Temporal(TemporalType.DATE)
    private Date fechaentrega;
    @Column(name = "fechalimite")
    @Temporal(TemporalType.DATE)
    private Date fechalimite;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coordenadas")
    private Double coordenadas;
    @Size(max = 200)
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 90)
    @Column(name = "usuariosave")
    private String usuariosave;
    @Size(max = 90)
    @Column(name = "usuarioupdate")
    private String usuarioupdate;
    @JoinColumn(name = "idcliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente idcliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idpedido", fetch=FetchType.EAGER)
    private List<Detallepedido> detallePedidoList;

    public Pedido() {
    }

    public Pedido(Integer id) {
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
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pe.limatambo.entidades.Pedido[ id=" + id + " ]";
    }
    
}
