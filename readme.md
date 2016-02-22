[![Build Status](https://travis-ci.org/RutledgePaulV/rsql-receiver.svg)](https://travis-ci.org/RutledgePaulV/rsql-receiver)
[![Coverage Status](https://coveralls.io/repos/RutledgePaulV/rsql-receiver/badge.svg?branch=master&service=github)](https://coveralls.io/github/RutledgePaulV/rsql-receiver?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.rutledgepaulv/rsql-receiver/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.rutledgepaulv/rsql-receiver)


### RSQL Query Receiver
A library for parsing rsql queries (a simple textual query language) and building them into a generic 
query tree which can then be built into any number of backend formats or even Java predicates.

### Modular
Pretty much all of the components involved to parse, traverse, and convert all use pluggable implementations. Sometimes
when parsing queries you find that your model as persisted might vary slightly from how you want to expose the query
ability with RSQL. Pre and post argument conversion transformers are meant to be hooks where you can strip out pieces
of the query tree or even rename some fields, etc.


### Backends
This library builds directly into the query tree understood by [q-builders](https://github.com/rutledgepaulv/q-builders).
This means that any backend which is supported via q-builders is also supported by this library. Currently those include:

* Java Predicate
* RSQL Query String
* Elasticsearch QueryBuilder
* Spring Data Mongodb Criteria


### Usage
```java
private QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();


@Test
public void mongo() {

    Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
    System.out.println(condition.query(new MongoVisitor()).getCriteriaObject().toString());

}


@Test
public void elasticsearch() {

    Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
    System.out.println(condition.query(new ElasticsearchVisitor()).toString());

}


@Test
public void predicate() {

    Condition<GeneralQueryBuilder> condition = pipeline.apply("firstName==Paul;age==30", User.class);
    Predicate<User> predicate = condition.query(new PredicateVisitor<>());

}
```


### License

This project is licensed under [MIT license](http://opensource.org/licenses/MIT).
