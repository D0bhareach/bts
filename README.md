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

Application's Default Mode is using File System to store Data to json file.
When started if Data file isn't present at specified location on Disc and 
spring.active.profile in application.properties file is set to `development`
Application will create required files and insert Mock Data to location.
ATTENTION!! Profiles feature has not been tested yet.
See Cleanup for instruction how to clean after use.

Tests aren't ready yet and can't be trusted to test Application.
Test in test directory were used at different time during Development. 

#### Database:
Application persist Data to H2 Database. Database located in
~/opt/databases/h2/epam/bts.
To run Application in Database Mode `java -jar build/libs/bts.jar --database`.

#### File System Storage:
Application Default Mode is using File System.
To use custom location on Disc pass valid path to command at
start `java -jar build/libs/bts.jar --filepath /path/to/your/location/filename`
To clean up delete files at custom location.
ATTENTION!! Path passing feature isn't tested yet.
Application storing data to disk for full path see Cleanup after use.

#### Cleanup after use:
Application persistent DataBase is stored in ~/opt/databases/h2/epam/epam_bt.  
After use run `rm -rf ~/opt/databases/h2/epam/bts` from terminal to delete Database from disk.
If Application has been working in File Storage mode  Data stored in ~/opt/storage/epam/epam .
To delete files from disk run `rm -rf ~/opt/storage/epam` .