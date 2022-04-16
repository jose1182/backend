package com.netjob.app.repository;

import com.netjob.app.domain.Servicio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Servicio entity.
 */
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long>, JpaSpecificationExecutor<Servicio> {
    @Query(
        value = "select distinct servicio from Servicio servicio left join fetch servicio.categorias",
        countQuery = "select count(distinct servicio) from Servicio servicio"
    )
    Page<Servicio> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct servicio from Servicio servicio left join fetch servicio.categorias")
    List<Servicio> findAllWithEagerRelationships();

    @Query("select servicio from Servicio servicio left join fetch servicio.categorias where servicio.id =:id")
    Optional<Servicio> findOneWithEagerRelationships(@Param("id") Long id);

    Page<Servicio> findAllByUsuario_id(Long userId, Pageable pageable);

    Page<Servicio> findAllByCategorias_id(Long id, Pageable pageable);

    Page<Servicio> findByDestacadoTrue(Pageable pageable);
}
