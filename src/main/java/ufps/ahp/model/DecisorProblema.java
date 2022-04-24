package ufps.ahp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "decisor_problema")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
@NamedQueries({
        @NamedQuery(name = "DecisorProblema.findAll", query = "SELECT p FROM DecisorProblema p"),
        @NamedQuery(name = "DecisorProblema.findByIdDecisorProblema", query = "SELECT p FROM DecisorProblema p WHERE p.idDecisorProblema = :idDecisorProblema"),
        })
public class DecisorProblema implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_decisor_problema")
    private int idDecisorProblema;

    @JoinColumn(name = "decisor", referencedColumnName = "id_decisor")
    @ManyToOne(optional = false)
    private Decisor decisor;

    @JoinColumn(name = "problema", referencedColumnName = "id_problema")
    @ManyToOne(optional = false)
    private Problema problema;

    public DecisorProblema() {
    }

    public DecisorProblema(int idDecisorProblema, Decisor decisor, Problema problema) {
        this.idDecisorProblema = idDecisorProblema;
        this.decisor = decisor;
        this.problema = problema;
    }
    public DecisorProblema(Decisor decisor, Problema problema) {
        this.decisor = decisor;
        this.problema = problema;
    }

    public int getIdDecisorProblema() {
        return idDecisorProblema;
    }

    public void setIdDecisorProblema(int idDecisorProblema) {
        this.idDecisorProblema = idDecisorProblema;
    }

    public Decisor getDecisor() {
        return decisor;
    }

    public void setDecisor(Decisor decisor) {
        this.decisor = decisor;
    }

    public Problema getProblema() {
        return problema;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }
}
