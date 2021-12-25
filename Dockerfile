FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

RUN apt-get update && apt-get install -y dos2unix
RUN mkdir /var/lib/budgetserver
RUN mkdir /var/lib/exchange
WORKDIR /var/lib/budgetserver/
ADD build/libs/CBudgetRendite-0.0.1-SNAPSHOT.jar /var/lib/budgetserver/budgetrendite.jar
ADD scripts/budgetRead.sh /var/lib/budgetserver/budgetRendite.sh
RUN chmod 777 /var/lib/budgetserver/budgetRendite.sh
RUN dos2unix /var/lib/budgetserver/budgetRendite.sh
ENTRYPOINT ["/var/lib/budgetserver/budgetRendite.sh"]