package ubb.scs.map.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message extends Entity<Long> {
    long user_from_id;
    long user_to_id;
    String message;
    LocalDateTime date;
    long message_reply_to_id;

    public Message(long user_from_id, long user_to_id, String message, LocalDateTime date, long message_reply_to_id) {
        this.user_from_id = user_from_id;
        this.user_to_id = user_to_id;
        this.message = message;
        this.date = date;
        this.message_reply_to_id = message_reply_to_id;
    }

    public Message(long user_from_id, long user_to_id, String message, LocalDateTime date) {
        this(user_from_id, user_to_id, message, date, -1L);
    }

    public long getUserFromId() {
        return user_from_id;
    }

    public long getUserToId() {
        return user_to_id;
    }

    public String getMessageText() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public long getMessageReplyToId() {
        return message_reply_to_id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user_from_id=" + user_from_id +
                ", user_to_id=" + user_to_id +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", message_reply_to_id=" + message_reply_to_id +
                '}';
    }
}
