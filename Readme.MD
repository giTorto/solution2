THE SERVER
First of all, for the whole project I used a real DB. The schema of the DB is available in the file db_schema.png.
In order to have a normalized Database I decided to use only one table for measure History and Health Profile.
This implies that the HealthProfile is automatically created from the DataBase by the most recent values
in the database for each measure type.

CODE STRUCTURE

-  The classes in the model package are related to the database, except the HealthProfile that allows to
visualize the measure types in the right way. The Person, MeasureHistory and MeasureType classes are
entities linked to the database tables.

-  In the package dozer there are all the classes that allows to send XML/JSON in the format required.
Indeed I used dozer to map the objects from one class to another. The file "dozerMappings.xml" contains
the information for the mapping.

-  In addition, to follow the requirements in the assignment I created a wrapper class for Person, Measures and Measure Types.
The wrapper classes are available in the wrapper package.
In the package resources there are the classes that manage the Http Requests and Responses.

-  In the test.model package there are the classes that I used to test the classes that are bound to
the db table.

IMPLEMENTATION
In order to understand better the implementation of this project I created the Java DOC.
In this way I think that it is easier to understand the code.

COMPILING AND EXECUTING
- In order to compile is sufficient to run the ant target "compile", available in the build.xml, that retrieves all the dependecies
and compiles the project.
- For the execution I used to run it into my IDE, which is INTELLIJ IDEA, but it should work also
in Eclipse.

THE CLIENT
COMPILING AND EXECUTING
- In order to compile is sufficient to run the ant target "compile", available in the build.xml, that retrieves all the dependecies
and compile the file.
- For the execution there is the target "execute", available in the build.xml, that accepts some parameters:
   - -DbaseURL allows to set up the url for the request to the server
   - -DdateAfter allows to set up the date after to require to the fellow student in the VIVA session(for extra point)
   - -DdateBefore  allows to set up the date before to require to the fellow student in the VIVA session(for extra point)
   - -Dmin allows to set up the min to require to the fellow student in the VIVA session(for extra point)
   - -Dmax allows to set up the max to require to the fellow student in the VIVA session(for extra point)

IMPLEMENTATION
The client has been written separately from the server, there are only few packages:
- model package contains the classes useful for the marshalling and unmarshalling.
- dozer package contains the class to map a MeasureHistory to its bean
- wrapper package contains three class used to wrap the various objects
- In the test class there all the requests to send to the server, for each step there is a specific method (e.g. for step 3.1 the method is doRequest1 and so on).

In each step on the console is printed the header with the request details and the body of the response
received from the server.
