package org.j1p5.api.activityArea.dto;

import java.util.List;
import org.j1p5.domain.activityArea.dto.ActivityAreaFullAddress;
import org.springframework.data.domain.Page;

public record AllAreaResponse(
        int totalPage, List<ActivityAreaFullAddress> content, boolean hasNext) {
    public static AllAreaResponse from(Page<ActivityAreaFullAddress> page) {
        return new AllAreaResponse(page.getTotalPages(), page.getContent(), page.hasNext());
    }
}
