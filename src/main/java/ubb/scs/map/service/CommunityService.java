package ubb.scs.map.service;

import ubb.scs.map.domain.FriendshipGraph;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommunityService {
    private Repository<Long, Utilizator> repo_user;
    private Repository<Long, Prietenie> repo_friendship;

    public CommunityService(Repository<Long, Utilizator> repo_user, Repository<Long, Prietenie> repo_friendship) {
        this.repo_user = repo_user;
        this.repo_friendship = repo_friendship;
    }

    public int numberOfCommunities() {
        FriendshipGraph friendshipGraph = new FriendshipGraph(repo_friendship.findAll());
        return friendshipGraph.numberOfCommunities();
    }

    public List<String> biggestCommunity() {
        FriendshipGraph friendshipGraph = new FriendshipGraph(repo_friendship.findAll());
        var list = friendshipGraph.largestConnectedComponent();
        List<String> users = new ArrayList<>();
        for(Long id: list) {
            Optional<Utilizator> u = repo_user.findOne(id);
            u.ifPresent(utilizator -> users.add(utilizator.getFirstName() + " " + utilizator.getLastName()));
        }

        return users;
    }
}
