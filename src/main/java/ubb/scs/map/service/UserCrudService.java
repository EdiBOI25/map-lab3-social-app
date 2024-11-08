package ubb.scs.map.service;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.file.UtilizatorRepository;

public class UserCrudService extends AbstractCrudService<Long, Utilizator> {
    public UserCrudService(Repository<Long, Utilizator> repository) {
        super(repository);
    }

    public Utilizator add(String first_name, String last_name) {
        Utilizator u = new Utilizator(first_name, last_name);
        u.setId(1L);
        return super.add(u);
    }
}
