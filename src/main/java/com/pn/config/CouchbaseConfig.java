package com.pn.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.ConfigMapping.NamingStrategy;

@ConfigMapping(prefix = "couchbase", namingStrategy = NamingStrategy.VERBATIM)
public interface CouchbaseConfig {

  String host();

  String username();

  String password();
}
