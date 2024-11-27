package ubb.scs.map.service_v2;


import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.utils.events.ChangeEventType;
import ubb.scs.map.utils.events.MessageEntityChangeEvent;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.observer.Observable;
import ubb.scs.map.utils.observer.Observer;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService implements Observable<MessageEntityChangeEvent> {
    private Repository<Long, Message> repo_messages;
    private Repository<Long, Utilizator> repo_users;
    private List<Observer<MessageEntityChangeEvent>> observers=new ArrayList<>();

    public MessageService(Repository<Long, Message> repo_messages,
                          Repository<Long, Utilizator> repo_users) {
        this.repo_messages = repo_messages;
        this.repo_users = repo_users;
    }

    public Message addMessageToDB(Message message) {
        if (repo_messages.save(message).isEmpty()){
            MessageEntityChangeEvent event = new MessageEntityChangeEvent(ChangeEventType.ADD, message);
            notifyObservers(event);
            System.out.println("added message to db " + message);
            return null;
        }
        System.out.println("failed to add message to db " + message);
        return message;
    }

    public Message deleteMessageFromDB(Long id) {
        Optional<Message> m = repo_messages.delete(id);
        if(m.isPresent()){
            MessageEntityChangeEvent event = new MessageEntityChangeEvent(ChangeEventType.DELETE, m.get());
            notifyObservers(event);
            System.out.println("deleted message from db " + m.get());
            return m.get();
        }
        System.out.println("failed to delete message from db " + id);
        return null;
    }

    public Message sendMessage(Message message) {
        if(addMessageToDB(message) == null) {
            MessageEntityChangeEvent event = new MessageEntityChangeEvent(ChangeEventType.ADD, message);
            notifyObservers(event);
            return null;
        }
        return message;
    }

    public Message deleteMessage(Long id){
        return deleteMessageFromDB(id);
    }

    public Iterable<Message> getAll(){
        return repo_messages.findAll();
    }

    public Utilizator getUserById(Long id) {
        Optional<Utilizator> user = repo_users.findOne(id);
        return user.orElse(null);
    }

    // get the messages in the order they were sent
    public Iterable<Message> getMessagesOfTwoUsers(Long user1_id, Long user2_id){
        Iterable<Message> messages = this.getAll();
        List<Message> result = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        return result.stream()
                .filter(x -> x.getUserFromId() == user1_id && x.getUserToId() == user2_id)
                .sorted(Comparator.comparing(Message::getDate))
                .toList();
    }



    @Override
    public void addObserver(Observer<MessageEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<MessageEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }

//    // TODO: asta nush daca e nevoie
//    public Utilizator updateUtilizator(Utilizator u) {
//        Optional<Utilizator> oldUser=repo.findOne(u.getId());
//        if(oldUser.isPresent()) {
//            Optional<Utilizator> newUser=repo.update(u);
//            if (newUser.isEmpty())
//                notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.UPDATE, u, oldUser.get()));
//            return newUser.orElse(null);
//        }
//        return oldUser.orElse(null);
//    }


}
