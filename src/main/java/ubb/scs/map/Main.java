package ubb.scs.map;

import ubb.scs.map.console_ui.ConsoleUI;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.file.PrietenieRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.service.*;

import java.util.ArrayList;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static ArrayList<Utilizator> generateUsers() {
        ArrayList<Utilizator> users = new ArrayList<>();
        for(int i = 1; i <= 10; ++i) {
            Utilizator u = new Utilizator("FirstName" + i, "LastName" + i);
            u.setId((long) i);
            users.add(u);
        }

        return users;
    }

    static ArrayList<Prietenie> generateFriends() {
        ArrayList<Prietenie> friendships = new ArrayList<>();
        friendships.add(new Prietenie(1L, 2L));
        friendships.add(new Prietenie(1L, 3L));
        friendships.add(new Prietenie(2L, 3L));
//        friendships.add(new Prietenie(1L, 4L));
        friendships.add(new Prietenie(4L, 5L));
        friendships.add(new Prietenie(6L, 7L));
        friendships.add(new Prietenie(7L, 8L));
        friendships.add(new Prietenie(7L, 9L));

        final long[] id = {1};
        friendships.forEach(f -> {
            f.setId(id[0]);
            id[0]++;
        });

        return friendships;
    }

    public static void main(String[] args) {
        Repository<Long, Utilizator> repo_users = new UtilizatorRepository(
                new UtilizatorValidator(),
                "./data/utilizatori.txt"
        );
        Repository<Long, Prietenie> repo_friendships = new PrietenieRepository(
                new PrietenieValidator(repo_users),
                "./data/prietenii.txt"
        );

        var users = generateUsers();
        for(Utilizator u: users) {
            repo_users.save(u);
        }
        var friendships = generateFriends();
        for(Prietenie p: friendships) {
            repo_friendships.save(p);
        }

        CrudService<Long, Utilizator> user_service = new UserCrudService(repo_users);
        CrudService<Long, Prietenie> friendship_service = new FriendshipCrudService(repo_friendships);
        CommunityService community_service = new CommunityService(repo_users, repo_friendships);

        ConsoleUI consoleUI = new ConsoleUI(user_service, friendship_service, community_service);
        consoleUI.run();




//        Repository<Long, Utilizator> repo = new InMemoryRepository<Long, Utilizator>(new UtilizatorValidator());
//        Repository<Long, Utilizator> repoFile = new UtilizatorRepository(new UtilizatorValidator(), "./data/utilizatori.txt");
//        Utilizator u1 = new Utilizator("IONUT", "a");
//        Utilizator u2 = new Utilizator("Mihai", "b");
//        Utilizator u3 = null;
//        u1.setId(1L);
//        u2.setId(2L);
//        try {
//            repoFile.save(u1);
//            repoFile.save(u2);
//            repoFile.save(u3);
//        }catch(IllegalArgumentException e)
//        {
//            System.out.println(e.getMessage());
//        }catch(ValidationException e)
//        {
//            System.out.println(e.getMessage());
//        }
//        System.out.println();

    }
}