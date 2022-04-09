package com.netjob.app.repository;

import com.netjob.app.domain.Favorito;
import com.netjob.app.domain.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Favorito entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long>, JpaSpecificationExecutor<Favorito> {
    Page<Favorito> findAllByUsuario_id(Long id, Pageable page);
}
