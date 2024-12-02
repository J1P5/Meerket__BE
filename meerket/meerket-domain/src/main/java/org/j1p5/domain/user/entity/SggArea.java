package org.j1p5.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class SggArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sgg_code", nullable = false, length = 5)
    private String sggCode;

    @Column(name = "sgg_name", nullable = false, length = 20)
    private String sggName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_area_id")
    private SidoArea sidoArea;
}
