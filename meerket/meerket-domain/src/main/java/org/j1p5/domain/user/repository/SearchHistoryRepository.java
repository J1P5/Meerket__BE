package org.j1p5.domain.user.repository;

import org.j1p5.domain.user.entity.SearchHistory;
import org.springframework.data.repository.CrudRepository;

public interface SearchHistoryRepository extends CrudRepository<SearchHistory, Long> {
}
