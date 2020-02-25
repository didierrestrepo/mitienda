package com.themakers.mitienda.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.themakers.mitienda.domain.ElementoLista} entity.
 */
public class ElementoListaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String nombre;

    @NotNull
    @Size(max = 5)
    private String codigo;

    @Lob
    private byte[] propiedades;

    private String propiedadesContentType;

    private Long tipoListaId;

    private String tipoListaNombre;

    private Long padreId;

    private String padreNombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public byte[] getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(byte[] propiedades) {
        this.propiedades = propiedades;
    }

    public String getPropiedadesContentType() {
        return propiedadesContentType;
    }

    public void setPropiedadesContentType(String propiedadesContentType) {
        this.propiedadesContentType = propiedadesContentType;
    }

    public Long getTipoListaId() {
        return tipoListaId;
    }

    public void setTipoListaId(Long tipoListaId) {
        this.tipoListaId = tipoListaId;
    }

    public String getTipoListaNombre() {
        return tipoListaNombre;
    }

    public void setTipoListaNombre(String tipoListaNombre) {
        this.tipoListaNombre = tipoListaNombre;
    }

    public Long getPadreId() {
        return padreId;
    }

    public void setPadreId(Long elementoListaId) {
        this.padreId = elementoListaId;
    }

    public String getPadreNombre() {
        return padreNombre;
    }

    public void setPadreNombre(String elementoListaNombre) {
        this.padreNombre = elementoListaNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ElementoListaDTO elementoListaDTO = (ElementoListaDTO) o;
        if (elementoListaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), elementoListaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ElementoListaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", propiedades='" + getPropiedades() + "'" +
            ", tipoListaId=" + getTipoListaId() +
            ", tipoListaNombre='" + getTipoListaNombre() + "'" +
            ", padreId=" + getPadreId() +
            ", padreNombre='" + getPadreNombre() + "'" +
            "}";
    }
}
