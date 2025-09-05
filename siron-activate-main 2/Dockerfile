FROM amazoncorretto:17-alpine3.19-jdk

#RUN apk add --no-cache procps

# We make four distinct layers so if there are capability changes the library layers can be re-used
COPY --chown=185 licenses-service/target/quarkus-app/lib/ /lib/
COPY --chown=185 licenses-service/target/quarkus-app/*.jar /
COPY --chown=185 licenses-service/target/quarkus-app/app/ /app/
COPY --chown=185 licenses-service/target/quarkus-app/quarkus/ /quarkus/


USER 185
EXPOSE 8080
ENV JAVA_OPTS_APPEND="-Dquarkus.banner.enabled=false"

ENTRYPOINT ["java","-jar","/quarkus-run.jar"]
