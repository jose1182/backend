package com.netjob.app.repository;

import com.netjob.app.domain.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mensaje entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long>, JpaSpecificationExecutor<Mensaje> {
    Page<Mensaje> findAllByEmisor_id(Long emisorId, Pageable pageable);
    Page<Mensaje> findAllByReceptor_id(Long receptorId, Pageable pageable);
    Page<Mensaje> findAllByConversacion_id(Long id, Pageable pageable);
}
