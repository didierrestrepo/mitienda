package com.themakers.mitienda.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ElementoLista.
 */
@Entity
@Table(name = "elemento_lista")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ElementoLista implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 5)
    @Column(name = "codigo", length = 5, nullable = false)
    private String codigo;

    @Lob
    @Column(name = "propiedades")
    private byte[] propiedades;

    @Column(name = "propiedades_content_type")
    private String propiedadesContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("elementoListas")
    private TipoLista tipoLista;

    @ManyToOne
    @JsonIgnoreProperties("elementoListas")
    private ElementoLista padre;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public ElementoLista nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public ElementoLista codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public byte[] getPropiedades() {
        return propiedades;
    }

    public ElementoLista propiedades(byte[] propiedades) {
        this.propiedades = propiedades;
        return this;
    }

    public void setPropiedades(byte[] propiedades) {
        this.propiedades = propiedades;
    }

    public String getPropiedadesContentType() {
        return propiedadesContentType;
    }

    public ElementoLista propiedadesContentType(String propiedadesContentType) {
        this.propiedadesContentType = propiedadesContentType;
        return this;
    }

    public void setPropiedadesContentType(String propiedadesContentType) {
        this.propiedadesContentType = propiedadesContentType;
    }

    public TipoLista getTipoLista() {
        return tipoLista;
    }

    public ElementoLista tipoLista(TipoLista tipoLista) {
        this.tipoLista = tipoLista;
        return this;
    }

    public void setTipoLista(TipoLista tipoLista) {
        this.tipoLista = tipoLista;
    }

    public ElementoLista getPadre() {
        return padre;
    }

    public ElementoLista padre(ElementoLista elementoLista) {
        this.padre = elementoLista;
        return this;
    }

    public void setPadre(ElementoLista elementoLista) {
        this.padre = elementoLista;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElementoLista)) {
            return false;
        }
        return id != null && id.equals(((ElementoLista) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ElementoLista{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", propiedades='" + getPropiedades() + "'" +
            ", propiedadesContentType='" + getPropiedadesContentType() + "'" +
            "}";
    }
}
