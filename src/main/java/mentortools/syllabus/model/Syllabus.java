package mentortools.syllabus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mentortools.module.model.Module;
import mentortools.trainingclass.model.TrainingClass;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "syllabuses")
public class Syllabus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(mappedBy = "syllabus")
    @EqualsAndHashCode.Exclude
    private Set<TrainingClass> trainingClasses;

    @OneToMany
    private Set<Module> modules;

    public Syllabus(String name) {
        this.name = name;
    }

    public void addTrainingClass(TrainingClass trainingClass){
        trainingClass.setSyllabus(this);
        if (trainingClasses == null){
            trainingClasses = new HashSet<>();
        }
        trainingClasses.add(trainingClass);
    }

    public void deleteFromTrainingClasses() {
        trainingClasses.forEach(tc->tc.setSyllabus(null));
    }

    public void addModule(Module module){
        if(modules == null){
            modules = new HashSet<>();
        }
        modules.add(module);
    }
}
