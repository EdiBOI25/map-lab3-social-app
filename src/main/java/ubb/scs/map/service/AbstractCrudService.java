package ubb.scs.map.service;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.repository.Repository;

public abstract class AbstractCrudService<ID, E extends Entity<ID>> implements CrudService<ID, E> {
    protected Repository<ID, E> repository;

    public AbstractCrudService(Repository<ID, E> repository) {
        this.repository = repository;
    }

    public E add(E entity){
        return repository.save(entity);
    }

    public E delete(ID id){
        return repository.delete(id);
    }

    public E findOne(ID id) {
        return repository.findOne(id);
    }

    public Iterable<E> findAll() {
        return repository.findAll();
    }
}
