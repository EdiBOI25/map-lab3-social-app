package ubb.scs.map.repository.database;


import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageDbRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;

    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Message> findOne(Long aLong) {
        String sql = "SELECT * FROM messages WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                long user_from_id = resultSet.getLong("user_from_id");
                long user_to_id = resultSet.getLong("user_to_id");
                String text = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                long reply_id = resultSet.getLong("reply_to_id");
                Message message;
                if (resultSet.wasNull())
                    message = new Message(user_from_id, user_to_id, text, date);
                else
                    message = new Message(user_from_id, user_to_id, text, date, reply_id);
                message.setId(id);
                return Optional.of(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

//    @Override
//    public Utilizator findOne(Long aLong) {
//        return null;
//    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                long user_from_id = resultSet.getLong("user_from_id");
                long user_to_id = resultSet.getLong("user_to_id");
                String text = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                long reply_id = resultSet.getLong("reply_to_id");
                Message message;
                if (resultSet.wasNull())
                    message = new Message(user_from_id, user_to_id, text, date);
                else
                    message = new Message(user_from_id, user_to_id, text, date, reply_id);
                message.setId(id);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO messages (user_from_id, user_to_id, message, date, reply_to_id) VALUES (?, ?)");
             ) {
            statement.setLong(1, entity.getUserFromId());
            statement.setLong(2, entity.getUserToId());
            statement.setString(3, entity.getMessageText());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            long reply_id = entity.getMessageReplyToId();
            if (reply_id == -1)
                statement.setNull(5, Types.BIGINT);
            else
                statement.setLong(5, reply_id);
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
    public Optional<Message> delete(Long aLong) {
        Optional<Message> message = findOne(aLong);
        if (message.isEmpty())
            return Optional.empty();

        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
        ) {
            statement.setLong(1, aLong);
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0)
            return message;
        else
            return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message message) {
        if(message == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(message);
        String sql = "update messages set user_from_id = ?, user_to_id = ?, message = ?, date = ?, reply_to_id = ? where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,message.getUserFromId());
            ps.setLong(2,message.getUserToId());
            ps.setString(3,message.getMessageText());
            ps.setTimestamp(4,Timestamp.valueOf(message.getDate()));
            long reply_id = message.getMessageReplyToId();
            if (reply_id == -1)
                ps.setNull(5, Types.BIGINT);
            else
                ps.setLong(5, reply_id);
            ps.setLong(6,message.getId());
            if( ps.executeUpdate() > 0 )
                return Optional.empty();
            return Optional.ofNullable(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
