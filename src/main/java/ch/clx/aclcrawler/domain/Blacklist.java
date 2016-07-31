package ch.clx.aclcrawler.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Blacklist.
 */
@Entity
@Table(name = "blacklist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Blacklist implements Serializable {

    @Id
    @NotNull
    @Column(name = "blacklist_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long blacklist_id;

    @Column(name = "partial_qualified_name")
    private String partial_qualified_name;

    public Long getBlacklist_id() {
        return blacklist_id;
    }

    public void setBlacklist_id(Long blacklist_id) {
        this.blacklist_id = blacklist_id;
    }

    public String getPartial_qualified_name() {
        return partial_qualified_name;
    }

    public void setPartial_qualified_name(String partial_qualified_name) {
        this.partial_qualified_name = partial_qualified_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Blacklist blacklist = (Blacklist) o;

        if ( ! Objects.equals(blacklist_id, blacklist.blacklist_id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(blacklist_id);
    }

    @Override
    public String toString() {
        return "Blacklist{" +
            " blacklist_id='" + blacklist_id + "'" +
            ", partial_qualified_name='" + partial_qualified_name + "'" +
            '}';
    }
}
