package com.pn.service;

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.ReactiveCluster;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.ReactiveQueryResult;
import com.pn.entity.AirlineEntity;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

  @InjectMocks AirlineService underTest;

  @Test
  void should_succeed_createAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    ReactiveCollection reactiveCollection = Mockito.mock(ReactiveCollection.class);
    MutationResult result = Mockito.mock(MutationResult.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.reactive()).thenReturn(reactiveCollection);
    Mockito.when(reactiveCollection.insert("airline_" + expected.getId(), expected))
        .thenReturn(Mono.just(result));

    AirlineEntity actual =
        underTest.createAirline(expected.getId(), expected).await().indefinitely();

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void should_fail_createAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    ReactiveCollection reactiveCollection = Mockito.mock(ReactiveCollection.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.reactive()).thenReturn(reactiveCollection);
    Mockito.when(reactiveCollection.insert("airline_" + expected.getId(), expected))
        .thenReturn(Mono.error(new DocumentExistsException(null)));

    Uni<AirlineEntity> actual = underTest.createAirline(expected.getId(), expected);
    UniAssertSubscriber<AirlineEntity> subscriber =
        actual.subscribe().withSubscriber(UniAssertSubscriber.create());

    subscriber.assertFailed();
  }

  @Test
  void should_succeed_getAllAirlines() {
    underTest.cluster = Mockito.mock(Cluster.class);
    ReactiveCluster reactiveCluster = Mockito.mock(ReactiveCluster.class);
    Mono<ReactiveQueryResult> queryResultMono = Mockito.mock(Mono.class);
    Flux<ReactiveQueryResult> queryResultFlux = Mockito.mock(Flux.class);

    List<AirlineEntity> expected =
        List.of(
            new AirlineEntity(
                1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States"));

    Mockito.when(underTest.cluster.reactive()).thenReturn(reactiveCluster);
    Mockito.when(reactiveCluster.query("select airline.* from `travel-sample`.inventory.airline"))
        .thenReturn(queryResultMono);
    Mockito.when(queryResultMono.flux()).thenReturn(queryResultFlux);
    Mockito.when(queryResultFlux.flatMap(Mockito.any())).thenReturn(Flux.fromIterable(expected));

    var actual = underTest.getAllAirlines().subscribe().asStream().toArray();

    Assertions.assertArrayEquals(expected.toArray(), actual);
  }

  @Test
  void should_succeed_getAirlineById() {
    underTest.collection = Mockito.mock(Collection.class);
    ReactiveCollection reactiveCollection = Mockito.mock(ReactiveCollection.class);
    Mono<GetResult> result = Mockito.mock(Mono.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.reactive()).thenReturn(reactiveCollection);
    Mockito.when(reactiveCollection.get("airline_" + expected.getId())).thenReturn(result);
    Mockito.when(result.map(Mockito.any())).thenReturn(Mono.just(expected));

    AirlineEntity actual = underTest.getAirlineById(expected.getId()).await().indefinitely();

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void should_fail_getAirlineById() {
    underTest.collection = Mockito.mock(Collection.class);
    ReactiveCollection reactiveCollection = Mockito.mock(ReactiveCollection.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.reactive()).thenReturn(reactiveCollection);
    Mockito.when(reactiveCollection.get("airline_" + expected.getId()))
        .thenReturn(Mono.error(new DocumentNotFoundException(null)));

    Uni<AirlineEntity> actual = underTest.getAirlineById(expected.getId());
    UniAssertSubscriber<AirlineEntity> subscriber =
        actual.subscribe().withSubscriber(UniAssertSubscriber.create());

    subscriber.assertFailed();
  }

  @Test
  void should_succeed_updateAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    ReactiveCollection reactiveCollection = Mockito.mock(ReactiveCollection.class);
    MutationResult result = Mockito.mock(MutationResult.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.reactive()).thenReturn(reactiveCollection);
    Mockito.when(reactiveCollection.replace("airline_" + expected.getId(), expected))
        .thenReturn(Mono.just(result));

    AirlineEntity actual =
        underTest.updateAirline(expected.getId(), expected).await().indefinitely();

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void should_fail_updateAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    ReactiveCollection reactiveCollection = Mockito.mock(ReactiveCollection.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.reactive()).thenReturn(reactiveCollection);
    Mockito.when(reactiveCollection.replace("airline_" + expected.getId(), expected))
        .thenReturn(Mono.error(new DocumentNotFoundException(null)));

    Uni<AirlineEntity> actual = underTest.updateAirline(expected.getId(), expected);
    UniAssertSubscriber<AirlineEntity> subscriber =
        actual.subscribe().withSubscriber(UniAssertSubscriber.create());

    subscriber.assertFailed();
  }

  @Test
  void should_succeed_deleteAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    ReactiveCollection reactiveCollection = Mockito.mock(ReactiveCollection.class);
    MutationResult result = Mockito.mock(MutationResult.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.reactive()).thenReturn(reactiveCollection);
    Mockito.when(reactiveCollection.remove("airline_" + expected.getId()))
        .thenReturn(Mono.just(result));

    Boolean actual = underTest.deleteAirline(expected.getId()).await().indefinitely();

    Assertions.assertTrue(actual);
  }

  @Test
  void should_fail_deleteAirline() {
    underTest.collection = Mockito.mock(Collection.class);
    ReactiveCollection reactiveCollection = Mockito.mock(ReactiveCollection.class);

    AirlineEntity expected =
        new AirlineEntity(
            1, "airline", "American Airlines", "AA", "AAL", "AMERICAN", "United States");

    Mockito.when(underTest.collection.reactive()).thenReturn(reactiveCollection);
    Mockito.when(reactiveCollection.remove("airline_" + expected.getId()))
        .thenReturn(Mono.error(new DocumentNotFoundException(null)));

    Uni<Boolean> actual = underTest.deleteAirline(expected.getId());
    UniAssertSubscriber<Boolean> subscriber =
        actual.subscribe().withSubscriber(UniAssertSubscriber.create());

    subscriber.assertFailed();
  }
}
