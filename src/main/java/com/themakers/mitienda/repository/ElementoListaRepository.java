package com.themakers.mitienda.repository;

import com.themakers.mitienda.domain.ElementoLista;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ElementoLista entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElementoListaRepository extends JpaRepository<ElementoLista, Long>, JpaSpecificationExecutor<ElementoLista> {

}
