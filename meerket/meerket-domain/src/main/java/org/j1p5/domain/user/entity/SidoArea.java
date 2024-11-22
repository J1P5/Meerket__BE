package org.j1p5.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class SidoArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sido_code", nullable = false, length = 2)
    private String sidoCode;

    @Column(name = "sido_name", nullable = false, length = 20)
    private String sidoName;
}
