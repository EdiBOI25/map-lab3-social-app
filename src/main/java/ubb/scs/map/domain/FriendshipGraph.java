package ubb.scs.map.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FriendshipGraph {
    private int n;
    private TreeMap<Long, ArrayList<Long>> adj_list = new TreeMap<>();
    public FriendshipGraph(Iterable<Prietenie> friendships){
        n = 0;
        try{
//            for(Prietenie f : friendships) {
//                n++;
//            }
            friendships.forEach(f -> n++);
        } catch (Exception e) {
            throw new IllegalArgumentException("Nu exista prietenii");
        }

//        for(Prietenie f: friendships){
//            if(!adj_list.containsKey(f.getUser1Id())){
//                adj_list.put(f.getUser1Id(), new ArrayList<>());
//            }
//            adj_list.get(f.getUser1Id()).add(f.getUser2Id());
//
//            if(!adj_list.containsKey(f.getUser2Id())){
//                adj_list.put(f.getUser2Id(), new ArrayList<>());
//            }
//            adj_list.get(f.getUser2Id()).add(f.getUser1Id());
//        }
        friendships.forEach(f -> {
            if(!adj_list.containsKey(f.getUser1Id())){
                adj_list.put(f.getUser1Id(), new ArrayList<>());
            }
            adj_list.get(f.getUser1Id()).add(f.getUser2Id());

            if(!adj_list.containsKey(f.getUser2Id())){
                adj_list.put(f.getUser2Id(), new ArrayList<>());
            }
            adj_list.get(f.getUser2Id()).add(f.getUser1Id());
        });
    }

    private void DFS(Long node, TreeMap<Long, Boolean> visited){
        visited.replace(node, true);
//        for(Long neighbour: adj_list.get(node)){
//            if(!visited.get(neighbour)){
//                DFS(neighbour, visited);
//            }
//        }
        adj_list.get(node).forEach(neighbour -> {
            if(!visited.get(neighbour)){
                DFS(neighbour, visited);
            }
        });
    }

    public int numberOfCommunities() {
        TreeMap<Long, Boolean> visited = new TreeMap<>();
        AtomicInteger communities = new AtomicInteger();

//        for(Long key: adj_list.keySet()){
//            visited.put(key, false);
//        }
        adj_list.keySet().forEach(key -> visited.put(key, false));

//        for(Long id1: adj_list.keySet()){
//            if (!visited.get(id1)) {
//                DFS(id1, visited);
//                communities.getAndIncrement();
//            }
//        }
        adj_list.keySet().forEach(id1 -> {
            if (!visited.get(id1)) {
                DFS(id1, visited);
                communities.getAndIncrement();
            }
        });
        return communities.get();
    }

    // DFS care tine minte nodurile
    private void DFS(Long node, TreeMap<Long, Boolean> visited, List<Long> component) {
        visited.replace(node, true);
        component.add(node);
//        for (Long neighbour : adj_list.get(node)) {
//            if (!visited.get(neighbour)) {
//                DFS(neighbour, visited, component);
//            }
//        }
        adj_list.get(node).forEach(neighbour -> {
            if (!visited.get(neighbour)) {
                DFS(neighbour, visited, component);
            }
        });
    }

    public List<Long> largestConnectedComponent() {
        TreeMap<Long, Boolean> visited = new TreeMap<>();
        List<Long> largestComponent = new ArrayList<>();

//        for (Long key : adj_list.keySet()) {
//            visited.put(key, false);
//        }
        adj_list.keySet().forEach(key -> visited.put(key, false));

        for (Long id1 : adj_list.keySet()) {
            if (!visited.get(id1)) {
                List<Long> currentComponent = new ArrayList<>();
                DFS(id1, visited, currentComponent);
                if (currentComponent.size() > largestComponent.size()) {
                    largestComponent = currentComponent;
                }
            }
        }
        return largestComponent;
    }


}
