package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AirlineEntity {

  private long id;
  private String type;
  private String name;
  private String iata;
  private String icao;
  private String callsign;
  private String country;
}
