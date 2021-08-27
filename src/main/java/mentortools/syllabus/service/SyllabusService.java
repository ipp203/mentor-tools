package mentortools.syllabus.service;

import mentortools.EntityNotFoundException;
import mentortools.syllabus.model.Syllabus;
import mentortools.syllabus.model.dto.AssignSyllabusToTrainingClassCommand;
import mentortools.syllabus.model.dto.CreateSyllabusCommand;
import mentortools.syllabus.model.dto.SyllabusDto;
import mentortools.syllabus.repository.SyllabusRepository;
import mentortools.trainingclass.model.TrainingClass;
import mentortools.trainingclass.model.dto.TrainingClassWithSyllabusDto;
import mentortools.trainingclass.repository.TrainingClassRepositoryOperation;
import mentortools.syllabus.TrainingClassAlreadyHasSyllabusException;
import mentortools.syllabus.TrainingClassHasNoSyllabusException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyllabusService {

    private final SyllabusRepository syllabusRepository;
    private final TrainingClassRepositoryOperation trainingClassRepositoryOperation;
    private final ModelMapper modelMapper;

    public SyllabusService(SyllabusRepository syllabusRepository, TrainingClassRepositoryOperation trainingClassRepositoryOperation, ModelMapper modelMapper) {
        this.syllabusRepository = syllabusRepository;
        this.trainingClassRepositoryOperation = trainingClassRepositoryOperation;
        this.modelMapper = modelMapper;
    }

    public List<SyllabusDto> listSyllabuses() {
        return syllabusRepository.findAll().stream()
                .map(s -> modelMapper.map(s, SyllabusDto.class))
                .collect(Collectors.toList());
    }

    public SyllabusDto getSyllabusById(long id) {
        return modelMapper.map(findById(id), SyllabusDto.class);
    }

    @Transactional
    public SyllabusDto saveSyllabus(CreateSyllabusCommand command) {
        return modelMapper.map(
                syllabusRepository.save(new Syllabus(command.getName())), SyllabusDto.class);
    }

    @Transactional
    public void deleteSyllabus(long id) {
        Syllabus syllabus = findById(id);
        syllabus.deleteFromTrainingClasses();
        syllabusRepository.delete(syllabus);
    }

    @Transactional
    public TrainingClassWithSyllabusDto assignSyllabus(long trainingId, AssignSyllabusToTrainingClassCommand command) {
        TrainingClass trainingClass = trainingClassRepositoryOperation.getTrainingClassById(trainingId);
        if (trainingClass.getSyllabus() != null) {
            throw new TrainingClassAlreadyHasSyllabusException(trainingClass);
        }
        Syllabus syllabus = findById(command.getSyllabusId());
        syllabus.addTrainingClass(trainingClass);
        return modelMapper.map(trainingClass, TrainingClassWithSyllabusDto.class);
    }

    @Transactional
    public TrainingClassWithSyllabusDto updateSyllabus(long trainingId, AssignSyllabusToTrainingClassCommand command) {
        TrainingClass trainingClass = trainingClassRepositoryOperation.getTrainingClassById(trainingId);
        if (trainingClass.getSyllabus() == null) {
            throw new TrainingClassHasNoSyllabusException(trainingClass);
        }
        Syllabus syllabus = findById(command.getSyllabusId());
        syllabus.addTrainingClass(trainingClass);
        return modelMapper.map(trainingClass, TrainingClassWithSyllabusDto.class);
    }

    private Syllabus findById(long id) {
        return syllabusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("syllabuses/syllabus-not-found"),
                        "Syllabus not found",
                        String.format("Syllabus with id %s not found", id)));
    }

    public TrainingClassWithSyllabusDto getTrainigClassWithSyllabus(long id) {
        TrainingClass trainingClass = trainingClassRepositoryOperation.getTrainingClassById(id);
        return modelMapper.map(trainingClass, TrainingClassWithSyllabusDto.class);
    }
}
