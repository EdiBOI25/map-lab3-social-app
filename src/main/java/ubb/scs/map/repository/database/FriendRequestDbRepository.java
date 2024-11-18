package ubb.scs.map.repository.database;


import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendRequestDbRepository implements Repository<Long, Prietenie> {
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    public FriendRequestDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Prietenie> findOne(Long aLong) {
        String sql = "SELECT * FROM friend_requests WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                long user1_id = resultSet.getLong("requester_id");
                long user2_id = resultSet.getLong("recipient_id");
                LocalDate date = resultSet.getDate("request_date").toLocalDate();
                Prietenie p = new Prietenie(user1_id, user2_id, date);
                p.setId(id);
                return Optional.of(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from friend_requests");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                long user1_id = resultSet.getLong("requester_id");
                long user2_id = resultSet.getLong("recipient_id");
                LocalDate date = resultSet.getDate("request_date").toLocalDate();
                Prietenie p = new Prietenie(user1_id, user2_id, date);
                p.setId(id);
                friendships.add(p);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO friend_requests (requester_id, recipient_id, request_date) VALUES (?, ?, ?)");
             ) {
            long user1_id = entity.getUser1Id();
            long user2_id = entity.getUser2Id();
            statement.setLong(1, user1_id);
            statement.setLong(2, user2_id);
            statement.setDate(3, Date.valueOf(entity.getDate()));
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0)
            return Optional.empty();
        else 
            return Optional.ofNullable(entity);
    }

    @Override
    public Optional<Prietenie> delete(Long aLong) {
        Optional<Prietenie> friendship = findOne(aLong);
        if (friendship.isEmpty())
            return Optional.empty();

        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM friend_requests WHERE id = ?");
        ) {
            statement.setLong(1, aLong);
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0)
            return friendship;
        else
            return Optional.empty();
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }
}
