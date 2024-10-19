package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;

public class PrietenieRepository extends AbstractFileRepository<Long, Prietenie>{
    UtilizatorRepository utilizatorRepository;

    public PrietenieRepository(Validator<Prietenie> validator, String fileName, UtilizatorRepository utilizatorRepository) {
        super(validator, fileName);
        this.utilizatorRepository = utilizatorRepository;
    }

    @Override
    public Prietenie createEntity(String line) {
        String[] splited = line.split(";");
        Utilizator user1 = utilizatorRepository.findOne(Long.parseLong(splited[1]));
        Utilizator user2 = utilizatorRepository.findOne(Long.parseLong(splited[2]));
        Prietenie p = new Prietenie(user1, user2);
        p.setId(Long.parseLong(splited[0]));
        return p;
    }

    @Override
    public String saveEntity(Prietenie entity) {
        String s = entity.getId() + ";" + entity.getUser1().getId() +
                ";" + entity.getUser2().getId();
        return s;
    }
}
