## BTS Bug tracking System Project

For Project specification see spec.md file in root directory.

#### Details:
* Java 11.0.7
* Gradle 6.5.1
* H2 (stored on Computer, see Cleanup Section.)

#### Build & Run
To build Project `./gradlew build`
To turn development logs in the console from SpringBoot comment `logging.pattern.console=` line  
with # in src/main/resources/application.properties file.

Run in development mode `./gradlew bootRun`  
Run jar file `java -jar build/libs/bts.jar`  
Read usage advise `java -jar build/libs/bts.jar -h`. 
#### Database:
Database is persistent. Script creates tables if not exists. Information stored
in Db stay there until Db removed.

#### Cleanup after use:
Application persistent DataBase is stored in ~/opt/databases/h2/epam/epam_bt.  
After use run `rm -rf ~/opt/databases/h2/epam/` from terminal.