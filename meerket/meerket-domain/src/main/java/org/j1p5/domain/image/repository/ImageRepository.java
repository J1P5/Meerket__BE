package org.j1p5.domain.image.repository;

import org.j1p5.domain.image.entitiy.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {}
