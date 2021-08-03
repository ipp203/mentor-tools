package mentortools.student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String gitHubUserName;

    private String comment;

    public Student(String name, String email, String gitHubUserName, String comment) {
        this.name = name;
        this.email = email;
        this.gitHubUserName = gitHubUserName;
        this.comment = comment;
    }
}