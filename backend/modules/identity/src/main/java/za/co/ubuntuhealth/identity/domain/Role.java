package za.co.ubuntuhealth.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(schema = "iam", name = "role")
public class Role {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    protected Role() {
    }

    public UUID id() {
        return id;
    }

    public String code() {
        return code;
    }
}
