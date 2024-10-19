package ubb.scs.map.domain;

import java.util.Objects;

public class Prietenie {
    private Utilizator user1;
    private Utilizator user2;

    public Prietenie(Utilizator user1, Utilizator user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public Utilizator getUser1() {
        return user1;
    }

    public Utilizator getUser2() {
        return user2;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prietenie)) return false;
        Prietenie that = (Prietenie) o;
        Utilizator user1 = that.getUser1();
        Utilizator user2 = that.getUser2();
        return (this.user1.equals(user1) && this.user2.equals(user2))
                || (this.user1.equals(user2) && this.user2.equals(user1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1.getId(), user2.getId());
    }
}
