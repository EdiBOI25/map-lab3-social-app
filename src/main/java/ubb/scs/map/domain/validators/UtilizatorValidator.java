package ubb.scs.map.domain.validators;


import ubb.scs.map.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //TODO: implement method validate
        if(entity.getFirstName().isEmpty())
            throw new ValidationException("Utilizatorul nu este valid (nu are prenume)");
        if(entity.getLastName().isEmpty())
            throw new ValidationException("Utilizatorul nu este valid (nu are nume de familie");
    }
}
