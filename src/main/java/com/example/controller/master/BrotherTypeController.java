package com.example.controller.master;

import com.example.model.BrotherType;
import com.example.service.BrotherTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/brother-type")
@CrossOrigin("*")
public class BrotherTypeController {


    @Autowired
    private BrotherTypeService service;

    @GetMapping
    public List<BrotherType> getAll() {
        return service.getAll();
    }

    @GetMapping("/active")
    public List<BrotherType> getActive() {
        return service.getActive();
    }

    @GetMapping("/{id}")
    public BrotherType getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public BrotherType create(@RequestBody BrotherType bt) {
        return service.create(bt);
    }

    @PutMapping("/{id}")
    public BrotherType update(@PathVariable Long id, @RequestBody BrotherType bt) {
        return service.update(id, bt);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Brother type deleted successfully";
    }

    @GetMapping("/admin/{adminId}")
    public List<BrotherType> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    @GetMapping("/admin/{adminId}/active")
    public List<BrotherType> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }


}
