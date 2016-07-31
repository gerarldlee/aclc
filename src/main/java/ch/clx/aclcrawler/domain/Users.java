package ch.clx.aclcrawler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ch.clx.aclcrawler.domain.enumeration.UserStatus;

/**
 * A Users.
 */
@Entity
@Table(name = "users")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Users implements Serializable {

    @Id
    @NotNull
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    @NotNull
    @Pattern(regexp = "^([a-zA-Z ]+)$")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Pattern(regexp = "^([a-zA-Z ]+)$")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @NotNull
    @Column(name = "create_by", nullable = false)
    private Long create_by;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private ZonedDateTime create_date;

    @Column(name = "modify_by")
    private Long modify_by;

    @Column(name = "modify_date", nullable = false)
    private ZonedDateTime modify_date;

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Password_history> password_historys = new HashSet<>();

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Long getCreate_by() {
        return create_by;
    }

    public void setCreate_by(Long create_by) {
        this.create_by = create_by;
    }

    public ZonedDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(ZonedDateTime create_date) {
        this.create_date = create_date;
    }

    public Long getModify_by() {
        return modify_by;
    }

    public void setModify_by(Long modify_by) {
        this.modify_by = modify_by;
    }

    public ZonedDateTime getModify_date() {
        return modify_date;
    }

    public void setModify_date(ZonedDateTime modify_date) {
        this.modify_date = modify_date;
    }

    public Set<Password_history> getPassword_historys() {
        return password_historys;
    }

    public void setPassword_historys(Set<Password_history> password_historys) {
        this.password_historys = password_historys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Users users = (Users) o;

        if ( ! Objects.equals(user_id, users.getUser_id())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user_id);
    }

    @Override
    public String toString() {
        return "Users{" +
            " user_id='" + user_id + "'" +
            ", name='" + name + "'" +
            ", surname='" + surname + "'" +
            ", email='" + email + "'" +
            ", password='" + password + "'" +
            ", status='" + status + "'" +
            ", create_by='" + create_by + "'" +
            ", create_date='" + create_date + "'" +
            ", modify_by='" + modify_by + "'" +
            ", modify_date='" + modify_date + "'" +
            '}';
    }
}
