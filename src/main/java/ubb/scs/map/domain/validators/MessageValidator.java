package ubb.scs.map.domain.validators;


import ubb.scs.map.domain.Message;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        if (entity.getUserFromId() == entity.getUserToId())
            throw new ValidationException("Mesajul nu este valid (user_from_id este egal cu user_to_id)");
        if(entity.getMessageText().isEmpty())
            throw new ValidationException("Mesajul nu este valid (mesajul este gol)");
        if(entity.getDate() == null)
            throw new ValidationException("Data nu este valida (data este nula)");
    }
}
