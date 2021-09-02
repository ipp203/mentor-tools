package mentortools.syllabus.service;

import mentortools.syllabus.model.Syllabus;
import mentortools.syllabus.model.dto.AssignSyllabusToTrainingClassCommand;
import mentortools.syllabus.model.dto.CreateSyllabusCommand;
import mentortools.syllabus.model.dto.SyllabusDto;
import mentortools.syllabus.repository.SyllabusRepository;
import mentortools.trainingclass.model.TrainingClass;
import mentortools.trainingclass.model.dto.TrainingClassWithSyllabusDto;
import mentortools.trainingclass.repository.TrainingClassRepositoryOperation;
import mentortools.trainingclass.model.TrainingClassAlreadyHasSyllabusException;
import mentortools.trainingclass.model.TrainingClassHasNoSyllabusException;
import mentortools.syllabus.repository.SyllabusRepositoryOperation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyllabusService {

    private final SyllabusRepository syllabusRepository;
    private final TrainingClassRepositoryOperation trainingClassRepositoryOperation;
    private final SyllabusRepositoryOperation syllabusRepositoryOperation;
    private final ModelMapper modelMapper;

    public SyllabusService(SyllabusRepository syllabusRepository, TrainingClassRepositoryOperation trainingClassRepositoryOperation, SyllabusRepositoryOperation syllabusRepositoryOperation, ModelMapper modelMapper) {
        this.syllabusRepository = syllabusRepository;
        this.trainingClassRepositoryOperation = trainingClassRepositoryOperation;
        this.syllabusRepositoryOperation = syllabusRepositoryOperation;
        this.modelMapper = modelMapper;
    }

    public List<SyllabusDto> listSyllabuses() {
        return syllabusRepository.findAll().stream()
                .map(s -> modelMapper.map(s, SyllabusDto.class))
                .collect(Collectors.toList());
    }

    public SyllabusDto getSyllabusById(long id) {
        return modelMapper.map(syllabusRepositoryOperation.findSyllabusById(id), SyllabusDto.class);
    }

    @Transactional
    public SyllabusDto saveSyllabus(CreateSyllabusCommand command) {
        return modelMapper.map(
                syllabusRepository.save(new Syllabus(command.getName())), SyllabusDto.class);
    }

    @Transactional
    public void deleteSyllabus(long id) {
        Syllabus syllabus = syllabusRepositoryOperation.findSyllabusById(id);
        syllabus.deleteFromTrainingClasses();
        syllabusRepository.delete(syllabus);
    }

    @Transactional
    public TrainingClassWithSyllabusDto assignSyllabus(long trainingId, AssignSyllabusToTrainingClassCommand command) {
        TrainingClass trainingClass = trainingClassRepositoryOperation.getTrainingClassById(trainingId);
        if (trainingClass.getSyllabus() != null) {
            throw new TrainingClassAlreadyHasSyllabusException(trainingClass);
        }
        Syllabus syllabus = syllabusRepositoryOperation.findSyllabusById(command.getSyllabusId());
        syllabus.addTrainingClass(trainingClass);
        return modelMapper.map(trainingClass, TrainingClassWithSyllabusDto.class);
    }

    @Transactional
    public TrainingClassWithSyllabusDto updateSyllabus(long trainingId, AssignSyllabusToTrainingClassCommand command) {
        TrainingClass trainingClass = trainingClassRepositoryOperation.getTrainingClassById(trainingId);
        if (trainingClass.getSyllabus() == null) {
            throw new TrainingClassHasNoSyllabusException(trainingClass);
        }
        Syllabus syllabus = syllabusRepositoryOperation.findSyllabusById(command.getSyllabusId());
        syllabus.addTrainingClass(trainingClass);
        return modelMapper.map(trainingClass, TrainingClassWithSyllabusDto.class);
    }



    public TrainingClassWithSyllabusDto getTrainigClassWithSyllabus(long id) {
        TrainingClass trainingClass = trainingClassRepositoryOperation.getTrainingClassById(id);
        return modelMapper.map(trainingClass, TrainingClassWithSyllabusDto.class);
    }
}
