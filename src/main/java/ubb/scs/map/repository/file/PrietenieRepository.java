package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;

public class PrietenieRepository extends AbstractFileRepository<Long, Prietenie>{
    public PrietenieRepository(Validator<Prietenie> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Prietenie createEntity(String line) {
        String[] splited = line.split(";");
        Prietenie p = new Prietenie(Long.parseLong(splited[1]), Long.parseLong(splited[2]));
        p.setId(Long.parseLong(splited[0]));
        return p;
    }

    @Override
    public String saveEntity(Prietenie entity) {
        String s = entity.getId() + ";" + entity.getUser1Id() +
                ";" + entity.getUser2Id();
        return s;
    }
}
