package com.themakers.mitienda.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.themakers.mitienda.domain.enumeration.Genero;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.themakers.mitienda.domain.Usuario} entity. This class is used
 * in {@link com.themakers.mitienda.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UsuarioCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Genero
     */
    public static class GeneroFilter extends Filter<Genero> {

        public GeneroFilter() {
        }

        public GeneroFilter(GeneroFilter filter) {
            super(filter);
        }

        @Override
        public GeneroFilter copy() {
            return new GeneroFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter segundoNombre;

    private StringFilter segundoApellido;

    private StringFilter numDocumento;

    private StringFilter celular;

    private StringFilter direccion;

    private GeneroFilter genero;

    private LongFilter userId;

    private LongFilter cuidadId;

    public UsuarioCriteria(){
    }

    public UsuarioCriteria(UsuarioCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.segundoNombre = other.segundoNombre == null ? null : other.segundoNombre.copy();
        this.segundoApellido = other.segundoApellido == null ? null : other.segundoApellido.copy();
        this.numDocumento = other.numDocumento == null ? null : other.numDocumento.copy();
        this.celular = other.celular == null ? null : other.celular.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.genero = other.genero == null ? null : other.genero.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.cuidadId = other.cuidadId == null ? null : other.cuidadId.copy();
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(StringFilter segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public StringFilter getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(StringFilter segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public StringFilter getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(StringFilter numDocumento) {
        this.numDocumento = numDocumento;
    }

    public StringFilter getCelular() {
        return celular;
    }

    public void setCelular(StringFilter celular) {
        this.celular = celular;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
    }

    public GeneroFilter getGenero() {
        return genero;
    }

    public void setGenero(GeneroFilter genero) {
        this.genero = genero;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCuidadId() {
        return cuidadId;
    }

    public void setCuidadId(LongFilter cuidadId) {
        this.cuidadId = cuidadId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(segundoNombre, that.segundoNombre) &&
            Objects.equals(segundoApellido, that.segundoApellido) &&
            Objects.equals(numDocumento, that.numDocumento) &&
            Objects.equals(celular, that.celular) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(genero, that.genero) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(cuidadId, that.cuidadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        segundoNombre,
        segundoApellido,
        numDocumento,
        celular,
        direccion,
        genero,
        userId,
        cuidadId
        );
    }

    @Override
    public String toString() {
        return "UsuarioCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (segundoNombre != null ? "segundoNombre=" + segundoNombre + ", " : "") +
                (segundoApellido != null ? "segundoApellido=" + segundoApellido + ", " : "") +
                (numDocumento != null ? "numDocumento=" + numDocumento + ", " : "") +
                (celular != null ? "celular=" + celular + ", " : "") +
                (direccion != null ? "direccion=" + direccion + ", " : "") +
                (genero != null ? "genero=" + genero + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (cuidadId != null ? "cuidadId=" + cuidadId + ", " : "") +
            "}";
    }

}
