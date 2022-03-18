package com.netjob.app.repository;

import com.netjob.app.domain.Valoracion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Valoracion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long>, JpaSpecificationExecutor<Valoracion> {}
