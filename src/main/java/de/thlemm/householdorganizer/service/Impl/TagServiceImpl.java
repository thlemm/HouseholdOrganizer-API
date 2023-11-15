package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.model.Tag;
import de.thlemm.householdorganizer.repository.TagRepository;
import de.thlemm.householdorganizer.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Override
    public List<String> getTagsByQueryStartsWith(String query) {
        String finalQuery = query.toLowerCase();

        List<String> tagsList = new ArrayList<>();

        for (Tag tag : tagRepository.findAll()) {
            tagsList.add(tag.getTag());
        }

        List<String> distinctTagsList = tagsList.stream()
                .distinct()
                .collect(Collectors.toList());

        return distinctTagsList
                .stream()
                .filter(a -> a.startsWith(finalQuery))
                .collect(Collectors.toList());
    }
}
