package persistence.bootstrap;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TestEntityClass {
    @Id
    private Long id;
}
