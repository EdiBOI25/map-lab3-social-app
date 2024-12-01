package ubb.scs.map.repository.database;


import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class PrietenieDbRepository implements Repository<Long, Prietenie> {
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Prietenie> findOne(Long aLong) {
        String sql = "SELECT * FROM friendship WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long user1_id = resultSet.getLong("user1_id");
                Long user2_id = resultSet.getLong("user2_id");
                LocalDate date = resultSet.getDate("friends_from").toLocalDate();
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
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendship");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                long user1_id = resultSet.getLong("user1_id");
                long user2_id = resultSet.getLong("user2_id");
                LocalDate date = resultSet.getDate("friends_from").toLocalDate();
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
             PreparedStatement statement = connection.prepareStatement("INSERT INTO friendship (user1_id, user2_id, friends_from) VALUES (?, ?, ?)");
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
             PreparedStatement statement = connection.prepareStatement("DELETE FROM friendship WHERE id = ?");
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

    private List<Prietenie> findAllOnPage(Connection connection, Pageable pageable, long user_id) throws SQLException {
        List<Prietenie> friendshipsOnPage = new ArrayList<>();
        String sql = "select * from friendship where user1_id = ? or user2_id = ?";
        sql += " limit ? offset ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, user_id);
            statement.setInt(3, pageable.getPageSize());
            statement.setInt(4, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    long user1_id = resultSet.getLong("user1_id");
                    long user2_id = resultSet.getLong("user2_id");
                    LocalDate date = resultSet.getDate("friends_from").toLocalDate();
                    Prietenie p = new Prietenie(user1_id, user2_id, date);
                    p.setId(id);
                    friendshipsOnPage.add(p);
                }
            }
        }

        return friendshipsOnPage;
    }

    public Page<Prietenie> findAllOnPage(Pageable pageable, long user_id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            List<Prietenie> friendshipsOnPage;
            friendshipsOnPage = findAllOnPage(connection, pageable, user_id);
            if (friendshipsOnPage.isEmpty()) {
                return new Page<>(Collections.emptyList(), 0);
            }
            return new Page<>(friendshipsOnPage, friendshipsOnPage.size());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int friendsCount(long user_id) {
        String sql = "select count(*) from friendship where user1_id = ? or user2_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
