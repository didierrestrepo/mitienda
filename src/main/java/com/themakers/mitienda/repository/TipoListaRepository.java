package com.themakers.mitienda.repository;

import com.themakers.mitienda.domain.TipoLista;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoLista entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoListaRepository extends JpaRepository<TipoLista, Long>, JpaSpecificationExecutor<TipoLista> {

}
