package com.example.shoong.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "Segment") // DB 테이블이 "Segment" 이므로 지정
@Getter
@Setter
@NoArgsConstructor
public class Segment {
  @Id
  @Column(name = "segmentID", length = 36, nullable = false)
  private String segmentID;

  @ManyToOne
  @JoinColumn(name = "pathID", nullable = false)
  private Path path;

  private Integer sequenceNumber;

  @Column(length = 255)
  private String description;

  private Integer distance;

  private LocalTime duration; // TIME => LocalTime으로 매핑

  @Column(length = 2)
  private String direction;
}
