package za.co.ubuntuhealth.patient.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
public class Patient {

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;


    private String phoneNumber;

    private String email;



    protected Patient() {
    }

        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }


        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
