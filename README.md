## BTS Bug tracking System Project

For Project's Specification see specs.md file in root directory.
For Project's Specification in Russian Language see specs.ru.md.

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
in Db stay there until Db removed. Database feature is not implemented yet.

#### File System Storage: 
Application storing data to disk for full path see Cleanup after use.

#### Cleanup after use:
Application persistent DataBase is stored in ~/opt/databases/h2/epam/epam_bt.  
After use run `rm -rf ~/opt/databases/h2/epam/` from terminal to delete Database from disk.
If Application has been working in File Storage mode  Data stored
in ~/opt/storage/epam/epam File. To delete files from disk run
`rm -rf ~/opt/storage/epam` .