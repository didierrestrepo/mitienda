package com.themakers.mitienda.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.themakers.mitienda.domain.enumeration.Genero;

/**
 * A DTO for the {@link com.themakers.mitienda.domain.Usuario} entity.
 */
public class UsuarioDTO implements Serializable {

    private Long id;

    private String segundoNombre;

    private String segundoApellido;

    @NotNull
    @Size(max = 15)
    private String numDocumento;

    @NotNull
    private String celular;

    private String direccion;

    private Genero genero;


    private Long userId;

    private String userLogin;

    private Long cuidadId;

    private String cuidadNombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getCuidadId() {
        return cuidadId;
    }

    public void setCuidadId(Long elementoListaId) {
        this.cuidadId = elementoListaId;
    }

    public String getCuidadNombre() {
        return cuidadNombre;
    }

    public void setCuidadNombre(String elementoListaNombre) {
        this.cuidadNombre = elementoListaNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) o;
        if (usuarioDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuarioDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
            "id=" + getId() +
            ", segundoNombre='" + getSegundoNombre() + "'" +
            ", segundoApellido='" + getSegundoApellido() + "'" +
            ", numDocumento='" + getNumDocumento() + "'" +
            ", celular='" + getCelular() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", genero='" + getGenero() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", cuidadId=" + getCuidadId() +
            ", cuidadNombre='" + getCuidadNombre() + "'" +
            "}";
    }
}
