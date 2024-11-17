package ubb.scs.map.service;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.file.PrietenieRepository;

import java.time.LocalDate;

public class FriendshipCrudService extends AbstractCrudService<Long, Prietenie> {
    public FriendshipCrudService(Repository<Long, Prietenie> repository) {
        super(repository);
    }

    public Prietenie add(long id1, long id2) {
        Prietenie p = new Prietenie(id1, id2, LocalDate.now());
        p.setId(1L);
        return super.add(p);
    }
}
