package co.igorski.user;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ApplicationUser {
    @Id
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
