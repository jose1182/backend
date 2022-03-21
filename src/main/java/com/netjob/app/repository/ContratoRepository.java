package com.netjob.app.repository;

import com.netjob.app.domain.Contrato;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Contrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long>, JpaSpecificationExecutor<Contrato> {}
