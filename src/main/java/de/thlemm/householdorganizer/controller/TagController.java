package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class TagController {

    @Autowired
    TagService tagService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/tags")
    public List<String> getTags(@RequestParam(value = "query", defaultValue="") String query) {

        return tagService.getTagsByQueryStartsWith(query);
    }
}
