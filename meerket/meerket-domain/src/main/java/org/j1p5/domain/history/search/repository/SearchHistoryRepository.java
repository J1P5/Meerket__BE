package org.j1p5.domain.history.search.repository;

import org.j1p5.domain.history.search.entity.SearchHistory;
import org.springframework.data.repository.CrudRepository;

public interface SearchHistoryRepository extends CrudRepository<SearchHistory, Long> {}
