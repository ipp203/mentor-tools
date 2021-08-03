package mentortools.trainingclass.service;

import mentortools.trainingclass.dto.CreateTrainingClassCommand;
import mentortools.trainingclass.dto.TrainingClassDto;
import mentortools.trainingclass.dto.UpdateTrainingClassCommand;
import mentortools.trainingclass.model.TrainingClass;
import mentortools.trainingclass.repository.TrainingClassRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingClassService {

    private final TrainingClassRepository repository;
    private final ModelMapper modelMapper;

    public TrainingClassService(TrainingClassRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public List<TrainingClassDto> listClasses() {
        return repository.findAll().stream()
                .map(c->modelMapper.map(c,TrainingClassDto.class))
                .collect(Collectors.toList());
    }

    public TrainingClassDto getClassById(long id) {
        TrainingClass trainingClass = getTrainingClassById(id);
        return modelMapper.map(trainingClass,TrainingClassDto.class);
    }

    @Transactional
    public TrainingClassDto saveClass(CreateTrainingClassCommand command) {
        TrainingClass trainingClass = new TrainingClass(command.getName(),command.getStartDate());
        repository.save(trainingClass);
        return modelMapper.map(trainingClass,TrainingClassDto.class);
    }

    @Transactional
    public TrainingClassDto updateClass(long id, UpdateTrainingClassCommand command) {
        TrainingClass trainingClass = getTrainingClassById(id);

        trainingClass.setName(command.getName());
        trainingClass.setDates(command.getDates());

        return modelMapper.map(trainingClass,TrainingClassDto.class);
    }

    @Transactional
    public void deleteClass(long id) {
        repository.delete(getTrainingClassById(id));
    }

    private TrainingClass getTrainingClassById(long id){
        return repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Training class not found, id: " + id));
    }
}
