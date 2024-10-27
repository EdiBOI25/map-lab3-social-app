package ubb.scs.map.service;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.repository.Repository;

import java.util.Optional;

public abstract class AbstractCrudService<ID, E extends Entity<ID>> implements CrudService<ID, E> {
    protected Repository<ID, E> repository;

    public AbstractCrudService(Repository<ID, E> repository) {
        this.repository = repository;
    }

    public E add(E entity){
        Optional<E> result = repository.save(entity);
        return result.orElse(null);
    }

    public E delete(ID id){
        Optional<E> result = repository.delete(id);
        return result.orElse(null);
    }

    public E findOne(ID id) {
        Optional<E> result = repository.findOne(id);
        return result.orElse(null);
    }

    public Iterable<E> findAll() {
        return repository.findAll();
    }
}
