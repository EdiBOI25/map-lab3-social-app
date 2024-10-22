package ubb.scs.map.service;

import ubb.scs.map.domain.Entity;

public interface CrudService<ID, E extends Entity<ID>> {
    E add(E entity);
    E delete(ID id);
    E findOne(ID id);
    Iterable<E> findAll();
}
