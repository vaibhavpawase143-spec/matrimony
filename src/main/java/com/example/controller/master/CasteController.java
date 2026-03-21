package com.example.controller.master;

import com.example.model.Caste;
import com.example.service.CasteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/caste")
@CrossOrigin("*")
public class CasteController {


    @Autowired
    private CasteService service;

    @GetMapping
    public List<Caste> getAll() {
        return service.getAll();
    }

    @GetMapping("/active")
    public List<Caste> getActive() {
        return service.getActive();
    }

    @GetMapping("/{id}")
    public Caste getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Caste create(@RequestBody Caste caste) {
        return service.create(caste);
    }

    @PutMapping("/{id}")
    public Caste update(@PathVariable Long id, @RequestBody Caste caste) {
        return service.update(id, caste);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Caste deleted successfully";
    }

    @GetMapping("/religion/{religionId}")
    public List<Caste> getByReligion(@PathVariable Long religionId) {
        return service.getByReligion(religionId);
    }

    @GetMapping("/religion/{religionId}/active")
    public List<Caste> getActiveByReligion(@PathVariable Long religionId) {
        return service.getActiveByReligion(religionId);
    }

    @GetMapping("/admin/{adminId}")
    public List<Caste> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    @GetMapping("/search")
    public List<Caste> search(@RequestParam String keyword) {
        return service.search(keyword);
    }


}
