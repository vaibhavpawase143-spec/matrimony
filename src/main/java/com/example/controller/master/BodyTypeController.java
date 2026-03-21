package com.example.controller.master;

import com.example.model.BodyType;
import com.example.service.BodyTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/body-type")
@CrossOrigin("*")
public class BodyTypeController {


    @Autowired
    private BodyTypeService service;

    // ✅ Get all
    @GetMapping
    public List<BodyType> getAll() {
        return service.getAll();
    }

    // ✅ Get active
    @GetMapping("/active")
    public List<BodyType> getActive() {
        return service.getActive();
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public BodyType getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ Create
    @PostMapping
    public BodyType create(@RequestBody BodyType bodyType) {
        return service.create(bodyType);
    }

    // ✅ Update
    @PutMapping("/{id}")
    public BodyType update(@PathVariable Long id, @RequestBody BodyType bodyType) {
        return service.update(id, bodyType);
    }

    // ✅ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Body type deleted successfully";
    }

    // ✅ Get by admin
    @GetMapping("/admin/{adminId}")
    public List<BodyType> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // ✅ Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public List<BodyType> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }


}
