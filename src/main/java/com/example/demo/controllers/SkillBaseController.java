package com.example.demo.controllers;

import com.example.demo.entities.SkillBase;
import com.example.demo.services.SkillBaseService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillBaseController {

    private final SkillBaseService skillBaseService;

    public SkillBaseController(SkillBaseService skillBaseService) {
        this.skillBaseService = skillBaseService;
    }

    @GetMapping
    public ResponseEntity<List<SkillBase>> findAll() {
        List<SkillBase>  skillBases = skillBaseService.findAll();
        return ResponseEntity.ok().body(skillBases);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillBase> findById(@PathVariable Long id) {
        SkillBase skillBase = skillBaseService.findById(id);
        return ResponseEntity.ok().body(skillBase);
    }

    @PostMapping("/create")
    public ResponseEntity<SkillBase> insert(@RequestBody SkillBase obj) {
        SkillBase skillBase = skillBaseService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(skillBase.getId()).toUri();
        return ResponseEntity.created(uri).body(skillBase);
    }
}
