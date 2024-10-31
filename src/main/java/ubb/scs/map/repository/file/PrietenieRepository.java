package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.validators.Validator;

import java.util.Optional;

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

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
//        for(Prietenie p: entities.values()){
//            // verifica daca exista deja prietenia (1, 2) = (2, 1)
//            if ((p.getUser1Id() == entity.getUser1Id() && p.getUser2Id() == entity.getUser2Id()) ||
//                    (p.getUser1Id() == entity.getUser2Id() && p.getUser2Id() == entity.getUser1Id())) {
//                throw new IllegalArgumentException("Prietenia deja exista");
//            }
//        }
        entities.values().stream()
                .filter(p -> (p.getUser1Id() == entity.getUser1Id() && p.getUser2Id() == entity.getUser2Id()) ||
                        (p.getUser1Id() == entity.getUser2Id() && p.getUser2Id() == entity.getUser1Id())
                )
                .findAny()
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Prietenia deja exista");
                });

        return super.save(entity);
    }

    @Override
    public Long getAvaliableId() {
        var keys = entities.keySet();
        if (keys.isEmpty())
            return 1L;
        return keys.stream().max(Long::compareTo).get() + 1;
    }
}
