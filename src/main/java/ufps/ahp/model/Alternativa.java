package ufps.ahp.model;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ufps.ahp.security.model.Usuario;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santi
 */
@Entity
@Table(name = "alternativa")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Alternativa.findAll", query = "SELECT a FROM Alternativa a"),
        @NamedQuery(name = "Alternativa.findByIdAlternativa", query = "SELECT a FROM Alternativa a WHERE a.idAlternativa = :idAlternativa")})
public class Alternativa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_alternativa")
    private Integer idAlternativa;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "problema", referencedColumnName = "id_problema")
    @ManyToOne
    private Problema problema;
    @JoinColumn(name = "usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alternativa1")
    private Collection<PuntuacionAlternativaCriterio> alternativa1CriterioCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alternativa2")
    private Collection<PuntuacionAlternativaCriterio> alternativa2CriterioCollection;

    public Alternativa() {
    }

    public Alternativa(Integer idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    public Alternativa(Integer idAlternativa, String descripcion) {
        this.idAlternativa = idAlternativa;
        this.descripcion = descripcion;
    }

    public Integer getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(Integer idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Problema getProblema() {
        return problema;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAlternativa != null ? idAlternativa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alternativa)) {
            return false;
        }
        Alternativa other = (Alternativa) object;
        if ((this.idAlternativa == null && other.idAlternativa != null) || (this.idAlternativa != null && !this.idAlternativa.equals(other.idAlternativa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.demo.rest.Alternativa[ idAlternativa=" + idAlternativa + " ]";
    }

}
