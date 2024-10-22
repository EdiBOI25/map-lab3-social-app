package ubb.scs.map.domain;

import java.util.Objects;

public class Prietenie extends Entity<Long> {
    private long userId1;
    private long userId2;

    public Prietenie(long userId1, long userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    public long getUser1Id() {
        return userId1;
    }

    public long getUser2Id() {
        return userId2;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "user1=" + userId1 +
                ", user2=" + userId2 +
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
