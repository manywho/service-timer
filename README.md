ManyWho Timer Service
=====================

[![Build Status](https://travis-ci.org/manywho/service-timer.svg?branch=develop)](https://travis-ci.org/manywho/service-timer)

This service allows your Flows to pause for a specified amount of time.

## Usage

The latest tagged version of the Timer Service will always be deployed to our shared services platform, and is accessible at https://services.manywho.com/api/timer/1.

If you need to run your own instance of the service (e.g. for compliance reasons), it's easy to spin up following these instructions:

#### Building

To build the service, you will need to have Apache Ant, Maven 3 and a Java 8 implementation installed (OpenJDK and the Oracle Java SE are both supported).

You will need to generate a configuration file for the service by running the provided `build.xml` script with Ant, and passing in a few valid database connection settings:

```bash
$ ant -Ddatabase.url=database.company.net -Ddatabase.username=username -Ddatabase.password=password
```

Now you can build and the runnable shaded JAR:

```bash
$ mvn clean package
```

#### Running

##### Service

The service is a Jersey JAX-RS application, that by default is run under the Grizzly2 server on port 8080 (if you use 
the packaged JAR).

###### Defaults

Running the following command will start the service listening on `0.0.0.0:8080/api/timer/1`:

```bash
$ java -jar timer-service/target/timer-service-1.0-SNAPSHOT.jar
```

###### Custom Port

You can specify a custom port to run the service on by passing the `server.port` property when running the JAR. The
following command will start the service listening on port 9090 (`0.0.0.0:9090/api/timer/1`):

```bash
$ java -Dserver.port=9090 -jar timer-service/target/timer-service-1.0-SNAPSHOT.jar
```

##### Worker

The worker is a simple runnable JAR that runs in the background and processes any required timer jobs. It can be started by running:

```bash
$ java -jar timer-worker/target/timer-worker-1.0-SNAPSHOT.jar
```

## Contributing

Contribution are welcome to the project - whether they are feature requests, improvements or bug fixes! Refer to 
[CONTRIBUTING.md](CONTRIBUTING.md) for our contribution requirements.

## License

This service is released under the [MIT License](http://opensource.org/licenses/mit-license.php).
