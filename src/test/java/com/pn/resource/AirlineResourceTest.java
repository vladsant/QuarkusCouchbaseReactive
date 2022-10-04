package com.pn.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

import com.pn.entity.AirlineEntity;
import com.pn.service.AirlineService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AirlineResourceTest {

  @Mock AirlineService airlineService;
  @InjectMocks AirlineResource underTest;

  @Test
  void should_succeed_addAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.createAirline(expected.getId(), expected))
        .thenReturn(Uni.createFrom().item(expected));

    Response actual = underTest.addAirline(expected).await().indefinitely();

    Assertions.assertEquals(CREATED.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(expected, actual.getEntity());
  }

  @Test
  void should_fail_addAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.createAirline(expected.getId(), expected))
        .thenReturn(Uni.createFrom().failure(new Exception()));

    Response actual = underTest.addAirline(expected).await().indefinitely();

    Assertions.assertEquals(BAD_REQUEST.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(
        "The airline with id: " + expected.getId() + " already exist", actual.getEntity());
  }

  @Test
  void should_succeed_getAllAirlines() {

    List<AirlineEntity> expected =
        List.of(
            new AirlineEntity(
                1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States"));

    Mockito.when(airlineService.getAllAirlines()).thenReturn(Multi.createFrom().iterable(expected));

    var actual = underTest.getAllAirlines().subscribe().asStream().toArray();

    Assertions.assertArrayEquals(expected.toArray(), actual);
  }

  @Test
  void should_succeed_getAirlineById() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.getAirlineById(expected.getId()))
        .thenReturn(Uni.createFrom().item(expected));

    Response actual = underTest.getAirlineById(expected.getId()).await().indefinitely();

    Assertions.assertEquals(OK.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(expected, actual.getEntity());
  }

  @Test
  void should_fail_getAirlineById() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.getAirlineById(expected.getId()))
        .thenReturn(Uni.createFrom().failure(new Exception()));

    Response actual = underTest.getAirlineById(expected.getId()).await().indefinitely();

    Assertions.assertEquals(NOT_FOUND.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(
        "The airline with id: " + expected.getId() + " doesn't exist", actual.getEntity());
  }

  @Test
  void should_succeed_updateAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.updateAirline(expected.getId(), expected))
        .thenReturn(Uni.createFrom().item(expected));

    Response actual = underTest.updateAirline(expected.getId(), expected).await().indefinitely();

    Assertions.assertEquals(CREATED.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(expected, actual.getEntity());
  }

  @Test
  void should_fail_updateAirline() {

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(airlineService.updateAirline(expected.getId(), expected))
        .thenReturn(Uni.createFrom().failure(new Exception()));

    Response actual = underTest.updateAirline(expected.getId(), expected).await().indefinitely();

    Assertions.assertEquals(NOT_FOUND.getStatusCode(), actual.getStatus());
    Assertions.assertEquals(
        "The airline with id: " + expected.getId() + " doesn't exist", actual.getEntity());
  }

  @Test
  void should_succeed_deleteAirline() {

    long id = 1;

    Mockito.when(airlineService.deleteAirline(id)).thenReturn(Uni.createFrom().item(true));

    Response actual = underTest.deleteAirline(id).await().indefinitely();

    Assertions.assertEquals(NO_CONTENT.getStatusCode(), actual.getStatus());
  }

  @Test
  void should_fail_deleteAirline() {

    long id = 1;

    Mockito.when(airlineService.deleteAirline(id))
        .thenReturn(Uni.createFrom().failure(new Exception()));

    Response actual = underTest.deleteAirline(id).await().indefinitely();

    Assertions.assertEquals(NOT_FOUND.getStatusCode(), actual.getStatus());
    Assertions.assertEquals("The airline with id: " + id + " doesn't exist", actual.getEntity());
  }
}
