package com.pn.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.pn.config.CouchbaseConfig;
import com.pn.entity.AirlineEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class AirlineService {

  private final CouchbaseConfig couchbaseConfig;

  Cluster cluster;
  Collection collection;

  public AirlineService(CouchbaseConfig couchbaseConfig) {
    this.couchbaseConfig = couchbaseConfig;
  }

  @PostConstruct
  void initialize() {
    cluster =
        Cluster.connect(
            couchbaseConfig.host(), couchbaseConfig.username(), couchbaseConfig.password());

    Bucket bucket = cluster.bucket("travel-sample");
    collection = bucket.defaultCollection();
  }

  public Uni<AirlineEntity> createAirline(long id, AirlineEntity airlineEntity) {
    var airline =
        collection
            .reactive()
            .insert(getDocumentId(id), airlineEntity)
            .map(mutationResultMono -> airlineEntity);
    return Uni.createFrom().publisher(airline);
  }

  public Multi<AirlineEntity> getAllAirlines() {
    var airlines =
        cluster
            .reactive()
            .query("select airline.* from `travel-sample`.inventory.airline")
            .flux()
            .flatMap(result -> result.rowsAs(AirlineEntity.class));
    return Multi.createFrom().publisher(airlines);
  }

  public Uni<AirlineEntity> getAirlineById(long id) {
    var airline =
        collection
            .reactive()
            .get(getDocumentId(id))
            .map(result -> result.contentAs(AirlineEntity.class));
    return Uni.createFrom().publisher(airline);
  }

  public Uni<AirlineEntity> updateAirline(long id, AirlineEntity airlineEntity) {
    airlineEntity.setId(id);
    var airline =
        collection
            .reactive()
            .replace(getDocumentId(id), airlineEntity)
            .map(mutationResultMono -> airlineEntity);
    return Uni.createFrom().publisher(airline);
  }

  public Uni<Boolean> deleteAirline(long id) {
    var airline = collection.reactive().remove(getDocumentId(id)).map(mutationResultMono -> true);
    return Uni.createFrom().publisher(airline);
  }

  private static String getDocumentId(long id) {
    return "airline_" + id;
  }
}
