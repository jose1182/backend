package com.netjob.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netjob.app.domain.Categoria} entity.
 */
public class CategoriaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @Lob
    private byte[] imagen;

    private String imagenContentType;

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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return imagenContentType;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaDTO)) {
            return false;
        }

        CategoriaDTO categoriaDTO = (CategoriaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", imagen='" + getImagen() + "'" +
            "}";
    }
}
