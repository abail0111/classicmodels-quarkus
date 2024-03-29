# Classic Models API (Quarkus)

The Classic Models API project is a Quarkus implementation of the
[BIRT](https://eclipse.github.io/birt-website/docs/template-sample-database/) /
[MySQL](https://www.mysqltutorial.org/mysql-sample-database.aspx)
sample database as a RESTful- and GraphQL web service.

## Setup Database

This project uses a slightly modified version of the
[BIRT](https://eclipse.github.io/birt-website/docs/template-sample-database/) /
[MySQL](https://www.mysqltutorial.org/mysql-sample-database.aspx)
sample database 'ClassicModels'. To create the 'classicmodels' database use the
PostgreSQL or MySQL scripts from the `/misc/` directory. 
Please update the `application.properties` file according to the selected database.

### PostgreSQL

```shell script
# Start PSQL
psql -U postgres

# Create database
CREATE DATABASE classicmodels WITH ENCODING 'UTF8' LC_COLLATE='German_Germany.1252' LC_CTYPE='German_Germany.1252';
# Connect to the new database
\c classicmodels

# Create tables and import data

# Option 1: Use create script.
# Make sure you have set the correct paths to the data files in the script.
\i {project}/misc/postgres/create_classicmodels.sql

# Option 2: Use the database dump
\i {project}/misc/postgres/classicmodels_dump.sql
```

### MySQL

```shell script
# Start the mysql utility
mysql --local-infile=1 -u root -p

# Enabling LOAD DATA LOCAL INFILE in mysql
SET GLOBAL local_infile=1;

# Create the ClassicModels database and load the schema and content
create database classicmodels;
use classicmodels;
source create_classicmodels-auto_increment.sql;
source load_classicmodels.sql;
quit;
```

## Testing

The application contains GraphQL test cases based on generated GraphQL-Operations.
In case of significant changes of the schema, the operations and tests must be updated.

### Generate GraphQL Operations

Use [gql-generator](https://github.com/timqian/gql-generator)
to generate queries and mutations from GraphQL Schema.
Copy the Schema file from
[/graphql/schema.graphql](http://localhost:8081/graphql/schema.graphql)
into the test resource directory `src\test\resources\graphql` and run the generator.

```shell script
# Install
npm install gql-generator -g

# Generate queries and mutations from schema file
gqlg --schemaFilePath ./schema.graphql --destDirPath ./operations --depthLimit 2
```


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8081/q/dev/.


## Using Opentracing

A detailed guide to Opentracing can be found [here](https://quarkus.io/guides/opentracing). \
Start the tracing system to collect and display the captured traces:
```shell script
docker run -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 jaegertracing/all-in-one:latest
```
Start the application in dev mode:
```shell script
./mvnw compile quarkus:dev
```
To see all tracing results in the Jeager UI visit [localhost:16686](http://localhost:16686/)


## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.


## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/classicmodels-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.
