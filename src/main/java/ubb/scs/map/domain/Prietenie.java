package ubb.scs.map.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Prietenie extends Entity<Long> {
    private long userId1;
    private long userId2;
    private LocalDate friendsFrom;

    public Prietenie(long userId1, long userId2, LocalDate friendsFrom) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.friendsFrom = friendsFrom;
        if (userId1 > userId2) { // sa fie mereu in ordine crescatoare
            this.userId1 = userId2;
            this.userId2 = userId1;
        }
    }

    public long getUser1Id() {
        return userId1;
    }

    public long getUser2Id() {
        return userId2;
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
                "user1=" + userId1 +
                ", user2=" + userId2 +
                ", friendsFrom=" + friendsFrom +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prietenie)) return false;
        return userId1 == ((Prietenie) o).userId1 && userId2 == ((Prietenie) o).userId2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId1, userId2);
    }
}
