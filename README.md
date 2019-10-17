# Processor Module

Processor module consumes stats data that coming from all worker modules. Module look for patterns and predefined 
conditions in streaming stats data. If it finds anything that met any condition or pattern, it produces actions
to the worker modules and master module. Worker modules are listening for vertical scaling information due to it's 
ability to update it's containers. On the other hand master module only cares for horizontal scaling actions. Informative
actions can be performed too. This actions can be email or sms like user based notifications.

## Getting Started

These instructions will get you a copy of the module up and running on your local machine for development and testing
purposes. See deployment for notes on how to deploy the project on a live system.   

### Prerequisites

Eclipse is used as an IDE for processor module. Before development or compiling you need to adjust java compliance level
to 1.7 from Project properties > Java Compiler > JDK Compliance. After that you need to add dependent jars to the
classpath from Project properties > Java Build Path > Add JARS.

Processor module is written in Java. You need to have bundled .jar file to run module. If you do not have .jar file look 
at [compiling](#compiling).

Processor module needs a kafka broker to operate. Module consumes docker stats from this broker. You can specify broker
as bash argument for local testing like below (default value is: localhost:9092):

```bash
java -cp processor.jar linkp.processor.App "localhost:9092"
```

### Compiling

On Eclipse go to File > Export > JAR File and click next. Ensure that export destination is processor/out/processor.jar
and click finish. You are ready for containerization.

### Containerizing

Build docker file with command below. Image name will be `linkp-processor`:

_You need to be in the same folder with [Dockerfile](./Dockerfile)_.

```bash
docker build -t linkp-processor .
```

## Running the module

You can run this module for both local development or docker environment

### Running locally

You can run this module locally by (default kafka broker is: localhost:9092):

```
java -cp processor.jar linkp.processor.App <kafka-broker>
```

### Running as container

You can run this module as docker container or docker service. Services takes care of the state of the containers they
own. I highly recommend to run module as service, user container approach only for development.

 * To run as container (not recommended), you need to specify kafka broker as environment variable.

    ```
    docker run \
                -d \
                -e KAFKA_BROKER='<kafka-broker>' \
                linkp-processor
    ```


 * To run as service you need to specify kafka broker as environment variable.
    
    ```
    docker service create \
                        --name linkp-processor \
                        --mode global \
                        -e KAFKA_BROKER='<kafka-broker>' \
                        linkp-processor:latest
    ```


## Deployment

All containerized modules will be deployed with single setup script. Use [linkp.sh](../linkp.sh) to start all system (
including linkp-processor services) --start-all-services (-sas) to start system.

```
./linkp.sh -sas
```

## Documentation

All related documentation about processor module is located at [docs](./docs) folder.
