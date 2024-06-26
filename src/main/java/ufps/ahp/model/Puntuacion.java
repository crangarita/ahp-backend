package ufps.ahp.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santi
 */
@Entity
@Table(name = "puntuacion")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Puntuacion.findAll", query = "SELECT p FROM Puntuacion p"),
        @NamedQuery(name = "Puntuacion.findByIdPuntuacion", query = "SELECT p FROM Puntuacion p WHERE p.idPuntuacion = :idPuntuacion"),
        @NamedQuery(name = "Puntuacion.findByIdProblemaAndIdDecisor", query = "SELECT p FROM Puntuacion p WHERE p.puntuacionCriterio.problema.idProblema = :idProblema AND p.decisor = :idDecisor"),
        @NamedQuery(name = "Puntuacion.findByValor", query = "SELECT p FROM Puntuacion p WHERE p.valor = :valor")})

public class Puntuacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_puntuacion")
    private Integer idPuntuacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private float valor;
    @JoinColumn(name = "decisor", referencedColumnName = "id_decisor")
    @ManyToOne(optional = false)
    private Decisor decisor;
    @JoinColumn(name = "puntuacion_criterio", referencedColumnName = "id_puntuacion_decisor")
    @ManyToOne(optional = false)
    private PuntuacionCriterio puntuacionCriterio;

    public Puntuacion() {
    }

    public Puntuacion(Integer idPuntuacion) {
        this.idPuntuacion = idPuntuacion;
    }

    public Puntuacion(Integer idPuntuacion, int valor) {
        this.idPuntuacion = idPuntuacion;
        this.valor = valor;
    }

    public Integer getIdPuntuacion() {
        return idPuntuacion;
    }

    public void setIdPuntuacion(Integer idPuntuacion) {
        this.idPuntuacion = idPuntuacion;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Decisor decisor() {
        return decisor;
    }

    public void setDecisor(Decisor decisor) {
        this.decisor = decisor;
    }

    public PuntuacionCriterio getPuntuacionCriterio() {
        return puntuacionCriterio;
    }

    public void setPuntuacionCriterio(PuntuacionCriterio puntuacionCriterio) {
        this.puntuacionCriterio = puntuacionCriterio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPuntuacion != null ? idPuntuacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Puntuacion)) {
            return false;
        }
        Puntuacion other = (Puntuacion) object;
        if ((this.idPuntuacion == null && other.idPuntuacion != null) || (this.idPuntuacion != null && !this.idPuntuacion.equals(other.idPuntuacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.demo.rest.Puntuacion[ idPuntuacion=" + idPuntuacion + " ]";
    }

}
