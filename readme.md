[![Build Status](https://travis-ci.org/RutledgePaulV/rest-query-engine.svg)](https://travis-ci.org/RutledgePaulV/rest-query-engine)
[![Coverage Status](https://coveralls.io/repos/RutledgePaulV/rest-query-engine/badge.svg?branch=master&service=github)](https://coveralls.io/github/RutledgePaulV/rest-query-engine?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.rutledgepaulv/rest-query-engine/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.rutledgepaulv/rest-query-engine)


### Rest Query Engine
A library for adding arbitrarily flexible querying to any java API. Almost all APIs deal with some set of predefined models
which they expose via various endpoints. You shouldn't have to create your own mechanisms for querying against collections
of these models and then figuring out how to make that query work against wherever you're storing it.

This common problem is where RQE comes in. Using RQE you send your queries across HTTP as a simple query parameter 
using the so-simple-you-feel-stupid query language [rsql](https://github.com/jirutka/rsql-parser). This library handles 
everything necessary to parse that incoming query, convert the various arguments in the query into the type that will 
actually appear in the database, and build an intermediate query tree that can be visited to produce a query for any 
number of backends.


### Modular
Pretty much all of the components involved to parse, traverse, and convert all use pluggable implementations. As this
library matures I'll document all the extension points and why you might use them.


### Backends
This library builds queries into the intermediate form understood by [q-builders](https://github.com/rutledgepaulv/q-builders).
This means that any backend which is supported via q-builders is also supported by this library. Currently those include:

* Java Predicate
* RSQL Query String
* Elasticsearch QueryBuilder
* Spring Data Mongodb Criteria


### Client Side
Do you provide a Java-based SDK for your API? Or do you send real rest requests in your test suite? Then you'll probably
be interested in using [q-builders](https://github.com/rutledgepaulv/q-builders) on the client side too. Since RSQL is a
supported target of the intermediate representation you can use it to construct your queries for the API in a type and typo 
safe way.


### Usage
```java
private QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();


@Test
public void mongo() {

    Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
    Criteria query = condition.query(new MongoVisitor());

}


@Test
public void elasticsearch() {

    Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
    QueryBuilder query = condition.query(new ElasticsearchVisitor());

}


@Test
public void predicate() {

    Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
    Predicate<User> predicate = condition.query(new PredicateVisitor<>());

}

```


### License

This project is licensed under [MIT license](http://opensource.org/licenses/MIT).
