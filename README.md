# Quarkus-couchbase-reactive Project

This project implements a Reactive CRUD API that handles airline documents from the travel-sample bucket provided by couchbase.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/

### Requirements

For building and running the application you need:

* JDK 17
* Maven 3
* Couchbase

#### Couchbase
couchbase is running on docker

* Install Docker
* Pull Couchbase: `docker pull couchbase`
* Run: `docker run -d --name db -p 8091-8097:8091-8097 -p 11210:11210 -p 11207:11207 -p 18091-18095:18091-18095 -p 18096:18096 -p 18097:18097 couchbase`
* Couchbase is running on port 8091: `http://localhost:8091`
* Install Couchbase Server ([docs](https://docs.couchbase.com/server/current/getting-started/do-a-quick-install.html#initialize-cluster-web-console))
* Install travel-sample bucket ([docs](https://docs.couchbase.com/server/current/manage/manage-settings/install-sample-buckets.html#install-sample-buckets-with-the-ui))
* Create an Index on travel-sample bucket: `CREATE PRIMARY INDEX 'name' ON 'travel-sample'`
* Set a configuration file called application.yaml with:

```yaml
couchbase:
  host: <COUCHBASE_SERVER_HOST>
  username: <COUCHBASE_SERVER_USERNAME>
  password: <COUCHBASE_SERVER_PASSWORD>
  bucketName: travel-sample
```

### Running the application in dev mode

* You can run your application in dev mode that enables live coding using:\
`./mvnw compile quarkus:dev`

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/

### Packaging and running the application

The application can be packaged using:\
`./mvnw package`

* It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory
* Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

* The application is now runnable using `java -Dquarkus.config.locations=<PATH_TO_CONFIG_FILE> -jar target/quarkus-app/quarkus-run.jar`

* If you want to build an _über-jar_, execute the following command:\
`./mvnw package -Dquarkus.package.type=uber-jar`

* The application, packaged as an _uber-jar_, is now runnable using:\
`java -Dquarkus.config.locations=<PATH_TO_CONFIG_FILE> -jar target/*-runner.jar`

### Open API Schema and UI

* Open API Schema document: `http://localhost:8080/q/openapi`
* Open API UI: `http://localhost:8080/q/swagger-ui`
