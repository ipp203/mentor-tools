package mentortools.module.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mentortools.lesson.model.Lesson;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String url;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    private Set<Lesson> lessons;

    public Module(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public void addLesson(Lesson lesson){
        if(lessons==null){
            lessons= new HashSet<>();
        }
        if(lesson.getModules() == null){
            lesson.setModules(new HashSet<>());
        }
        lesson.getModules().add(this);
        lessons.add(lesson);
    }
}
