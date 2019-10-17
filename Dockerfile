FROM anapsix/alpine-java

RUN mkdir -p /app
WORKDIR /app

COPY ./out/ /app

CMD ["java", "-cp", "processor.jar", "linkp.processor.App"]
