/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.ahp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ufps.ahp.security.model.Usuario;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author santi
 */
@Entity
@Table(name = "problema")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
@NamedQueries({
        @NamedQuery(name = "Problema.findAll", query = "SELECT p FROM Problema p"),
        @NamedQuery(name = "Problema.findByIdProblema", query = "SELECT p FROM Problema p WHERE p.idProblema = :idProblema"),
        @NamedQuery(name = "Problema.findByFechaCreacion", query = "SELECT p FROM Problema p WHERE p.fechaCreacion = :fechaCreacion"),
        @NamedQuery(name = "Problema.findByFechaFinalizacion", query = "SELECT p FROM Problema p WHERE p.fechaFinalizacion = :fechaFinalizacion")})
public class Problema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_problema")
    private int idProblema;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date fechaCreacion;
    @Column(name = "fecha_finalizacion")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date fechaFinalizacion;
    @JoinColumn(name = "usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario usuario;
    @OneToMany(mappedBy = "problema")
    private Collection<Criterio> criterioCollection;
    @OneToMany(mappedBy = "problema")
    private Collection<Alternativa> alternativaCollection;

    @Column(name ="estado")
    private Boolean estado;

    @Column(name = "token")
    private String token;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "problema")
    private Collection<DecisorProblema> decisorProblemas;



    public Problema() {
    }

    public Problema(int idProblema, String descripcion, Date fechaCreacion, Date fechaFinalizacion, Usuario usuario) {
        this.idProblema = idProblema;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.usuario = usuario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Collection<DecisorProblema> decisorProblemas() {
        return decisorProblemas;
    }

    public void setDecisorProblemas(Collection<DecisorProblema> decisorProblemas) {
        this.decisorProblemas = decisorProblemas;
    }

    public Collection<Criterio> criterioCollection() {
        return criterioCollection;
    }

    public void setCriterioCollection(Collection<Criterio> criterioCollection) {
        this.criterioCollection = criterioCollection;
    }

    public Collection<Alternativa> alternativaCollection() {
        return alternativaCollection;
    }

    public void setAlternativaCollection(Collection<Alternativa> alternativaCollection) {
        this.alternativaCollection = alternativaCollection;
    }

    public Problema(int idProblema) {
        this.idProblema = idProblema;
    }

    public int getIdProblema() {
        return idProblema;
    }

    public void setIdProblema(int idProblema) {
        this.idProblema = idProblema;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problema problema = (Problema) o;
        return idProblema == problema.idProblema;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProblema);
    }

    @Override
    public String toString() {
        return "Problema[ idProblema=" + idProblema + " ]";
    }

}
