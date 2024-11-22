package org.j1p5.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

@Entity
@Getter
public class EmdArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "emd_code", nullable = false, length = 2)
    private String emdCode;

    @Column(name = "emd_name", nullable = false, length = 20)
    private String emdName;

    @Column(name = "geom", nullable = false)
    private Polygon geom;

    @Column(name = "coordinate", nullable = false)
    private Point coordinate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_area_id")
    private SidoArea sidoArea;
}
