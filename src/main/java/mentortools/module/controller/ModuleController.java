package mentortools.module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mentortools.module.model.dto.AddModuleCommand;
import mentortools.module.model.dto.CreateModuleCommand;
import mentortools.module.model.dto.ModuleDto;
import mentortools.module.service.ModuleService;
import mentortools.syllabus.model.dto.SyllabusWithModulesDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Module administration")
public class ModuleController {

    private final ModuleService service;

    public ModuleController(ModuleService service) {
        this.service = service;
    }

    @GetMapping("/api/modules/{id}")
    @Operation(summary = "Get module by id")
    public ModuleDto getModuleById(@PathVariable("id")long id){
        return service.getModuleById(id);
    }

    @GetMapping("/api/modules")
    @Operation(summary = "List modules")
    public List<ModuleDto> listModules(){
        return service.listModules();
    }

    @PostMapping("/api/modules")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create module")
    public ModuleDto createModule(@Valid @RequestBody CreateModuleCommand command){
        return service.saveModule(command);
    }

    @PutMapping("/api/modules/{id}")
    @Operation(summary = "Update module by id")
    public ModuleDto updateModule(@PathVariable("id")long id, @Valid @RequestBody CreateModuleCommand command){
        return service.changeModule(id, command);
    }

    @DeleteMapping("/api/modules/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete module by id")
    public void deleteModule(@PathVariable("id") long id){
        service.deleteModule(id);
    }

    @PutMapping("/api/syllabuses/{id}/modules")
    @Operation(summary = "Add module to syllabus by ids")
    public SyllabusWithModulesDto addModuleToSyllabus(@PathVariable("id") long syllabusId, @Valid @RequestBody AddModuleCommand command){
       return service.addModuleToSyllabus(syllabusId, command);
    }

    @DeleteMapping("/api/syllabuses/{id}/modules")
    @Operation(summary = "Remove module from syllabus by ids")
    public SyllabusWithModulesDto removeModuleFromSyllabus(@PathVariable("id") long syllabusId, @Valid @RequestBody AddModuleCommand command){
       return service.removeModuleFromSyllabus(syllabusId, command);
    }
}
