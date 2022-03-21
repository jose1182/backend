package com.netjob.app.repository;

import com.netjob.app.domain.Conversacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Conversacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, Long>, JpaSpecificationExecutor<Conversacion> {}
