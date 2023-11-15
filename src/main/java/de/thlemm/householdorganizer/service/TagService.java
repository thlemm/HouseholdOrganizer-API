package de.thlemm.householdorganizer.service;

import java.util.List;

public interface TagService {
    List<String> getTagsByQueryStartsWith(String query);
}
