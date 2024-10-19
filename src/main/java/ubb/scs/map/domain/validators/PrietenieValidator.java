package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie p) throws ValidationException {
        Utilizator user1 = p.getUser1();
        Utilizator user2 = p.getUser2();
        UtilizatorValidator user_validator = new UtilizatorValidator();
        if (user1 == null || user2 == null)
            throw new ValidationException("Prietenia nu este valida (user1 sau user2 sunt null)");
        if(user1.equals(user2))
            throw new ValidationException("Prietenia nu este valida (user1 == user2)");
        try{
            user_validator.validate(user1);
            user_validator.validate(user2);
        } catch (ValidationException e){
            throw new ValidationException("Prietenia nu este valida (user1 sau user2 nu sunt valizi)");
        }
    }
}
