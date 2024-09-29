package de.functionfactory.movie_api.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class HomeController {
    @Autowired
    private ObjectMapper mapper;

    @GetMapping
    public ObjectNode home() {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("message", "Hello World!");

        return objectNode;
    }
}
