package ubb.scs.map.service_v2;



import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.utils.events.ChangeEventType;
import ubb.scs.map.utils.events.UtilizatorEntityChangeEvent;
import ubb.scs.map.utils.observer.Observable;
import ubb.scs.map.utils.observer.Observer;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieService implements Observable<UtilizatorEntityChangeEvent> {
    private Repository<Long, Prietenie> repo_friendship;
    private Repository<Long, Prietenie> repo_requests;
    private Repository<Long, Utilizator> repo_users;
    private List<Observer<UtilizatorEntityChangeEvent>> observers=new ArrayList<>();

    public PrietenieService(Repository<Long, Prietenie> repo_friendship,
                            Repository<Long, Prietenie> repo_requests,
                            Repository<Long, Utilizator> repo_users) {
        this.repo_friendship = repo_friendship;
        this.repo_requests = repo_requests;
        this.repo_users = repo_users;
    }

//    // TODO: sendRequest
//    //  adauga in repo_requests o cerere de prietenie
//    //  daca e deja, nu o adauga
//    //  daca e invers (adica 2 trimite lui 1 dar 1 deja ii trimisese lui 2), atunci adauga in repo_friendship si sterge din requests
//    public Prietenie addFriendRequest(Prietenie friend_request) {
//
//        if(repo.save(user).isEmpty()){
//            UtilizatorEntityChangeEvent event = new UtilizatorEntityChangeEvent(ChangeEventType.ADD, user);
//            notifyObservers(event);
//            return null;
//        }
//        return user;
//    }

//    // TODO: deleteFriendship + deleteRequest
//    public Utilizator deleteUtilizator(Long id){
//        Optional<Utilizator> user=repo.delete(id);
//        if (user.isPresent()) {
//            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, user.get()));
//            return user.get();
//        }
//        return null;
//    }
//
    public Iterable<Prietenie> getAll(){
        return repo_friendship.findAll();
    }

    public Utilizator getUserById(Long id) {
        Optional<Utilizator> user = repo_users.findOne(id);
        return user.orElse(null);
    }

    public Iterable<Prietenie> getFriendshipsOfUser(Long id) {
        Iterable<Prietenie> messages = this.getAll();
        List<Prietenie> friendships = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        return friendships.stream()
                .filter(x->x.getUser1Id() == id || x.getUser2Id() == id)
                .toList();
    }



    @Override
    public void addObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UtilizatorEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorEntityChangeEvent t) {

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
