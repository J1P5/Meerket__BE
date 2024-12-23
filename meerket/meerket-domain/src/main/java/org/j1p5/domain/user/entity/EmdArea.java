package org.j1p5.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
public class EmdArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "emd_code", nullable = false, length = 8)
    private String emdCode;

    @Column(name = "emd_name", nullable = false, length = 20)
    private String emdName;

    @Column(name = "geom", nullable = false, columnDefinition = "geometry(MULTIPOLYGON)")
    private Geometry geom;

    @Column(name = "coordinate", nullable = false, columnDefinition = "geometry(POINT)")
    private Point coordinate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sgg_area_id")
    private SggArea sggArea;
}
