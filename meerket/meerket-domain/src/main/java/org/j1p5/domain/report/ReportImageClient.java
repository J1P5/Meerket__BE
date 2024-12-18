package org.j1p5.domain.report;

import java.io.File;
import java.util.List;

public interface ReportImageClient {
    List<String> upload(List<File> images);
}
