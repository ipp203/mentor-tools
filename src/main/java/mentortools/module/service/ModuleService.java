package mentortools.module.service;

import mentortools.EntityNotFoundException;
import mentortools.module.model.Module;
import mentortools.module.model.dto.AddModuleCommand;
import mentortools.module.model.dto.CreateModuleCommand;
import mentortools.module.model.dto.ModuleDto;
import mentortools.module.repository.ModuleRepository;
import mentortools.syllabus.model.Syllabus;
import mentortools.syllabus.model.SyllabusNotContainsModuleException;
import mentortools.syllabus.model.dto.SyllabusWithModulesDto;
import mentortools.syllabus.repository.SyllabusRepositoryOperation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleService {
    private final ModelMapper modelMapper;
    private final ModuleRepository moduleRepository;
    private final SyllabusRepositoryOperation syllabusRepositoryOperation;

    public ModuleService(ModelMapper modelMapper, ModuleRepository moduleRepository, SyllabusRepositoryOperation syllabusRepositoryOperation) {
        this.modelMapper = modelMapper;
        this.moduleRepository = moduleRepository;
        this.syllabusRepositoryOperation = syllabusRepositoryOperation;
    }

    public ModuleDto getModuleById(long id) {
        Module module = findById(id);
        return modelMapper.map(module, ModuleDto.class);
    }

    public List<ModuleDto> listModules() {
        return moduleRepository.findAll().stream()
                .map(m -> modelMapper.map(m, ModuleDto.class))
                .collect(Collectors.toList());
    }


    public ModuleDto saveModule(CreateModuleCommand command) {
        return modelMapper.map(
                moduleRepository.save(new Module(command.getTitle(), command.getURL())), ModuleDto.class);
    }

    @Transactional
    public ModuleDto changeModule(long id, CreateModuleCommand command) {
        Module module = findById(id);
        module.setTitle(command.getTitle());
        module.setURL(command.getURL());
        return modelMapper.map(module, ModuleDto.class);
    }

    public void deleteModule(long id) {
        Module module = findById(id);
        moduleRepository.delete(module);
    }

    @Transactional
    public SyllabusWithModulesDto addModuleToSyllabus(long syllabusId, AddModuleCommand command) {
        Syllabus syllabus = syllabusRepositoryOperation.findSyllabusById(syllabusId);
        Module module = findById(command.getModuleId());
        syllabus.addModule(module);
        return modelMapper.map(syllabus, SyllabusWithModulesDto.class);
    }

    @Transactional
    public SyllabusWithModulesDto removeModuleFromSyllabus(long syllabusId, AddModuleCommand command) {
        Syllabus syllabus = syllabusRepositoryOperation.findSyllabusById(syllabusId);
        Module module = findById(command.getModuleId());
        removeModuleFromSyllabus(syllabus, module);
        return modelMapper.map(syllabus, SyllabusWithModulesDto.class);
    }

    private Module findById(long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("modules/not_found"),
                        "Module not found",
                        "Module not found with id: " + id));
    }

    public void removeModuleFromSyllabus(Syllabus syllabus, Module module) {
        if (syllabus.getModules() == null || !syllabus.getModules().contains(module)) {
            throw new SyllabusNotContainsModuleException(syllabus, module);
        }
        syllabus.getModules().remove(module);
    }
}
