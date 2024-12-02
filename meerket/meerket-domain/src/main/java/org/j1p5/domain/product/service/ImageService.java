package org.j1p5.domain.product.service;

import java.io.File;
import java.util.List;

public interface ImageService {

    List<String> upload(List<File> images);
}
