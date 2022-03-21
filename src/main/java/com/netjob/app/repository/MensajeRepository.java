package com.netjob.app.repository;

import com.netjob.app.domain.Mensaje;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mensaje entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long>, JpaSpecificationExecutor<Mensaje> {}
