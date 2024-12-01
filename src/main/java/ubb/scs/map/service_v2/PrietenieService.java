package ubb.scs.map.service_v2;



import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.database.PrietenieDbRepository;
import ubb.scs.map.utils.events.ChangeEventType;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.events.UtilizatorEntityChangeEvent;
import ubb.scs.map.utils.observer.Observable;
import ubb.scs.map.utils.observer.Observer;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieService implements Observable<PrietenieEntityChangeEvent> {
    private Repository<Long, Prietenie> repo_friendship;
    private Repository<Long, Prietenie> repo_requests;
    private Repository<Long, Utilizator> repo_users;
    private List<Observer<PrietenieEntityChangeEvent>> observers=new ArrayList<>();

    public PrietenieService(Repository<Long, Prietenie> repo_friendship,
                            Repository<Long, Prietenie> repo_requests,
                            Repository<Long, Utilizator> repo_users) {
        this.repo_friendship = repo_friendship;
        this.repo_requests = repo_requests;
        this.repo_users = repo_users;
    }

    public Prietenie addReuqestToDB(Prietenie friend_request) {
        if (repo_requests.save(friend_request).isEmpty()){
            PrietenieEntityChangeEvent event = new PrietenieEntityChangeEvent(ChangeEventType.ADD, friend_request);
            notifyObservers(event);
            System.out.println("added request" + friend_request);
            return null;
        }
        System.out.println("failed to add request" + friend_request);
        return friend_request;
    }

    public Prietenie deleteRequestFromDB(Long id) {
        Optional<Prietenie> f = repo_requests.delete(id);
        if(f.isPresent()){
            PrietenieEntityChangeEvent event = new PrietenieEntityChangeEvent(ChangeEventType.DELETE, f.get());
            notifyObservers(event);
            System.out.println("deleted request" + f.get());
            return f.get();
        }
        System.out.println("failed to delete request" + id);
        return null;
    }

    public Prietenie addFriendshipToDB(Prietenie friendship) {
        long user1_id = friendship.getUser1Id();
        long user2_id = friendship.getUser2Id();
        if (user1_id > user2_id) {
            long aux = user1_id;
            user1_id = user2_id;
            user2_id = aux;
        }
        Prietenie new_friendship = new Prietenie(user1_id, user2_id, LocalDate.now());
        new_friendship.setId(0L);
        if (repo_friendship.save(new_friendship).isEmpty()){
            PrietenieEntityChangeEvent event = new PrietenieEntityChangeEvent(ChangeEventType.ADD, new_friendship);
            notifyObservers(event);
            System.out.println("added friendship" + new_friendship);
            return null;
        }
        System.out.println("failed to add friendship" + friendship);
        return friendship;
    }

    public Prietenie deleteFriendshipFromDB(Long id) {
        Optional<Prietenie> f = repo_friendship.delete(id);
        if(f.isPresent()){
            PrietenieEntityChangeEvent event = new PrietenieEntityChangeEvent(ChangeEventType.DELETE, f.get());
            notifyObservers(event);
            System.out.println("deleted friendship" + f.get());
            return f.get();
        }
        System.out.println("failed to delete friendship" + id);
        return null;
    }

    public Prietenie addFriendRequest(Prietenie friend_request) {
        Iterable<Prietenie> all_requests = repo_requests.findAll();
        List<Prietenie> requests = StreamSupport.stream(all_requests.spliterator(), false)
                .collect(Collectors.toList());

        // daca cererea e deja trimisa
        if(requests.stream()
                .anyMatch(x->x.getUser1Id() == friend_request.getUser1Id()
                        && x.getUser2Id() == friend_request.getUser2Id())) {
            return friend_request;
        }

        // daca celalalt deja a trimis, se adauga prietenia noua
        Prietenie reversed_request = requests.stream()
                .filter(x->x.getUser1Id() == friend_request.getUser2Id()
                        && x.getUser2Id() == friend_request.getUser1Id())
                .findFirst()
                .orElse(null);

        if(reversed_request != null) {
            // adauga in repo_friendship si sterge inversa din requests
            if (addFriendshipToDB(friend_request) != null){
                return friend_request;
            }

            if(deleteRequestFromDB(reversed_request.getId()) == null){
                return friend_request;
            }
            return null;
        }

        if(addReuqestToDB(friend_request) == null){
            PrietenieEntityChangeEvent event = new PrietenieEntityChangeEvent(ChangeEventType.ADD, friend_request);
            notifyObservers(event);
            return null;
        }
        return friend_request;
    }

    public Prietenie deletePrietenie(Long id){
        return deleteFriendshipFromDB(id);
    }

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

    public Iterable<Utilizator> getUsersNotFriendsWith(Long id) {
        Iterable<Utilizator> users = repo_users.findAll();
        Iterable<Prietenie> friendships = this.getFriendshipsOfUser(id);
        List<Utilizator> result = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        List<Prietenie> friends = StreamSupport.stream(friendships.spliterator(), false)
                .collect(Collectors.toList());
        return result.stream()
                .filter(x -> !Objects.equals(x.getId(), id))
                .filter(x->friends.stream().noneMatch(y->y.getUser1Id() == x.getId() || y.getUser2Id() == x.getId()))
                .toList();
    }

    public Iterable<Prietenie> getIncomingRequestsForUser(Long id) {
        Iterable<Prietenie> friendships = repo_requests.findAll();
        List<Prietenie> result = StreamSupport.stream(friendships.spliterator(), false)
                .collect(Collectors.toList());
        return result.stream()
                .filter(x -> x.getUser2Id() == id)
                .toList();
    }

    public Page<Prietenie> findAllOnPage(Pageable pageable, long user_id) {
        PrietenieDbRepository repo_friendship = (PrietenieDbRepository) this.repo_friendship;
        return repo_friendship.findAllOnPage(pageable, user_id);
    }

    public int getNumberOfFriends(Long id) {
        PrietenieDbRepository repo_friendship = (PrietenieDbRepository) this.repo_friendship;
        return repo_friendship.friendsCount(id);
    }

    @Override
    public void addObserver(Observer<PrietenieEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<PrietenieEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(PrietenieEntityChangeEvent t) {

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
