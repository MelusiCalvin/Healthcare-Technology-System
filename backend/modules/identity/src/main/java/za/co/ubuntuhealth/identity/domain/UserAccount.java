package za.co.ubuntuhealth.identity.domain;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_account", schema = "identity")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 120)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 120)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true, length = 120)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "sex", nullable = false, length = 10)
    private String sex;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", schema = "identity", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false, length = 40)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    protected UserAccount() {}

    public UserAccount(String username, String email, String sex, String passwordHash, Set<UserRole> roles) {
        this.username = username;
        this.email = email;
        this.sex = sex;
        this.passwordHash = passwordHash;
        this.roles = new HashSet<>(roles);
        this.active = true;
        this.createdAt = OffsetDateTime.now();
    }

    public UserAccount(String firstname, String lastname, String username, String email, String passwordHash, Set<UserRole> roles) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = new HashSet<>(roles);
        this.active = true;
        this.createdAt = OffsetDateTime.now();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getSex() { return sex; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isActive() { return active; }
    public Set<UserRole> getRoles() { return Set.copyOf(roles); }
    public Instant getLastLoginAt() { return lastLoginAt; }

    public void recordSuccessfulLogin(Instant loginAt) {
        this.lastLoginAt = loginAt;
    }
}
