# texoitchallenge

Code for challenge to Texo IT appliance

### Configuration

There is no need for configuration, just running the application should be enough

It uses an H2 in-memory database, which is populated by the CSVLoader (Application Runner) that starts before the webservice is up and reads the CSV file called
movielist.csv under the `main/resources` package, populating the H2 database with its data.

If you want custom CSVs to be read then you can add the file to that package before packaging the application and run with the command example under the next __Run__ section.

It is a Spring Boot application and uses *Maven* for dependency management and packaging. It is developed using __Java 17__ by *Azul Zulu Community*, so be sure
your `$JAVA_HOME` is using the 17th version too. If you want to check if maven is using java 17, try

    mvn --version

### Run

To have a running and ready API, you just need to run the application using your IDE, or package it and run it.

To package it using *Maven*:

    mvn package

The resulting package should be under the `target` directory. To run it (remember it is developed in Java 17):

    java -jar target/challenge-0.0.1-SNAPSHOT.jar

If you want to add custom CSV data other than the default `movielist.csv` file, add it as said in the __Configuration__ section above, and run with:

    java -jar target/challenge-0.0.1-SNAPSHOT.jar --filename=movielist.csv

Or if you want a list of files (under `main/resources`) (all must have headers because it skips the header when reading), pass them separated by comma:

    java -jar target/challenge-0.0.1-SNAPSHOT.jar --filename=movielist.csv,test.csv

Each invalid filename will only trigger an error message for that filename, the application will still be running.

Details about the routes are at `http://<api host, eg localhost>:8080/actuator/mappings`
