package mentortools.lesson.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mentortools.module.model.Module;

import java.util.Set;


@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String url;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "lessons")
    private Set<Module> modules;

    public Lesson(String title, String url) {
        this.title = title;
        this.url = url;
    }
}
