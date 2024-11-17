package ubb.scs.map.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Prietenie extends Entity<Long> {
    private long user1_id;
    private long user2_id;
    private LocalDate friendsFrom;

    public Prietenie(long user1_id, long user2_id, LocalDate friendsFrom) {
        this.user1_id = user1_id;
        this.user2_id = user2_id;
        this.friendsFrom = friendsFrom;
    }

    public long getUser1Id() {
        return user1_id;
    }

    public long getUser2Id() {
        return user2_id;
    }

    public LocalDate getDate() {
        return friendsFrom;
    }

    public void setDate(LocalDate date) {
        friendsFrom = date;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "user1=" + user1_id +
                ", user2=" + user2_id +
                ", friendsFrom=" + friendsFrom +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prietenie)) return false;
        return user1_id == ((Prietenie) o).user1_id && user2_id == ((Prietenie) o).user2_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1_id, user2_id);
    }
}
