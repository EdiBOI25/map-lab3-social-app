package ubb.scs.map.repository.memory;


import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    protected Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        return entities.keySet().stream()
                .filter(key -> key.equals(id))
                .findFirst()
                .map(entities::get);
    }

    @Override
    public Iterable<E> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public Optional<E> save(E entity) throws ValidationException {

        /**
         *
         * @param entity
         *         entity must be not null
         * @return null- if the given entity is saved
         *         otherwise returns the entity (id already exists)
         * @throws ValidationException
         *            if the entity is not valid
         * @throws IllegalArgumentException
         *             if the given entity is null.     *
         */

        if(entity==null)
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL");
        validator.validate(entity);
        if(entities.containsKey(entity.getId()))
            return Optional.of(entity);
        entities.put(entity.getId(),entity);
        return Optional.empty();


    }

    @Override
    public Optional<E> delete(ID id) {
        return entities.keySet().stream()
                .filter(key -> key.equals(id))
                .findFirst()
                .map(key -> {
                    E entity = entities.get(key);
                    entities.remove(key);
                    return entity;
                });
    }

    @Override
    public Optional<E> update(E entity) {
        return Optional.empty();
    }
}
