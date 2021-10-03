package io.xgeeks.examples.spring.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.List;

@RestController
@RequestMapping("/names")
public class NameController {

    private final NameService service;

    @Autowired
    public NameController(NameService service) {
        this.service = service;
    }

    @GetMapping
    public List<NameStatus> findAll() {
        return service.findAll();
    }

    @GetMapping("{name}")
    public NameStatus findByName(@PathVariable("name") String name) {
        return service.findByName(name);
    }

    @DeleteMapping(value = "{name}")
    public void decrement(@PathVariable("name") String name) {
        service.decrement(name);
    }

    @PostMapping
    public void increment(@RequestBody String name) {
        service.increment(name);
    }
}
