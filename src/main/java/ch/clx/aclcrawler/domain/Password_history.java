package ch.clx.aclcrawler.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Password_history.
 */
@Entity
@Table(name = "password_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Password_history implements Serializable {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "password_history_id", nullable = false)
    private Long password_history_id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private ZonedDateTime create_date;

    @ManyToOne
    private Users users;

    public Long getPassword_history_id() {
        return password_history_id;
    }

    public void setPassword_history_id(Long password_history_id) {
        this.password_history_id = password_history_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZonedDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(ZonedDateTime create_date) {
        this.create_date = create_date;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Password_history password_history = (Password_history) o;

        if ( ! Objects.equals(password_history_id, password_history.password_history_id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password_history_id);
    }

    @Override
    public String toString() {
        return "Password_history{" +
            " password_history_id='" + password_history_id + "'" +
            ", user_id='" + user_id + "'" +
            ", password='" + password + "'" +
            ", create_date='" + create_date + "'" +
            '}';
    }
}
