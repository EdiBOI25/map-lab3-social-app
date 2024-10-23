package ubb.scs.map.service;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.file.PrietenieRepository;

public class FriendshipCrudService extends AbstractCrudService<Long, Prietenie> {
    public FriendshipCrudService(Repository<Long, Prietenie> repository) {
        super(repository);
    }

    public Prietenie add(long id1, long id2) {
        Prietenie p = new Prietenie(id1, id2);
        PrietenieRepository repo = (PrietenieRepository) super.repository;
        long id = repo.getAvaliableId();
        p.setId(id);
        return super.add(p);
    }
}
