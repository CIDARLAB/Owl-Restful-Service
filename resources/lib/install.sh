#!/bin/bash

mvn install:install-file -Dfile=jacop-4.1.0.jar -DgroupId=org -DartifactId=jacop -Dversion=4.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=clotho3api-jar-with-dependencies.jar -DgroupId=org.clothocad -DartifactId=Clotho3JavaAPI -Dversion=3.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=miniEugene-core-1.0.0-jar-with-dependencies.jar -DgroupId=org.cidarlab -DartifactId=miniEugene -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=eugene-2.0.0-SNAPSHOT.jar -DgroupId=org.cidarlab -DartifactId=eugene -Dversion=2.0.0-SNAPSHOT -Dpackaging=jar
