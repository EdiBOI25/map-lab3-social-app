package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

public class PrietenieValidator implements Validator<Prietenie> {
    private Repository<Long, Utilizator> userRepo;

    public PrietenieValidator(Repository<Long, Utilizator> userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public void validate(Prietenie p) throws ValidationException {
        long user1Id = p.getUser1Id();
        long user2Id = p.getUser2Id();

        if(user1Id == user2Id)
            throw new ValidationException("Prietenia nu este valida (user1Id == user2Id)");

        Utilizator user1 = userRepo.findOne(user1Id);
        Utilizator user2 = userRepo.findOne(user2Id);
        if (user1 == null || user2 == null)
            throw new ValidationException("Prietenia nu este valida (user1 sau user2 nu exista)");
    }
}
