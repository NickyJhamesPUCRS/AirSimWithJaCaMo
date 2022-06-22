FROM ubuntu:20.04

LABEL maintainer="nicky.souza@edu.pucrs.br"
LABEL version="0.1"
LABEL description="This is custom Docker Image AirSim with JaCaMo"



# Disable Prompt During Packages Installation
ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y software-properties-common gcc && \
    add-apt-repository -y ppa:deadsnakes/ppa

RUN -E add-apt-repository ppa:openjdk-r/ppa
RUN apt-get update && apt-get install -y python3.8 python3-distutils python3-pip python3-apt  wget unzip

WORKDIR /opt/java
RUN curl https://download.java.net/java/GA/jdk15.0.2/0d1cfde4252546c6931946de8db48ee2/7/GPL/openjdk-15.0.2_linux-x64_bin.tar.gz -o openjdk-15.0.2_linux-x64_bin.tar.gz
RUN tar -xzf openjdk-15.0.2_linux-x64_bin.tar.gz && \
    rm -rf openjdk-15.0.2_linux-x64_bin.tar.gz

WORKDIR /usr/src/airsim

RUN wget -c https://services.gradle.org/distributions/gradle-7.4.2-bin.zip -P /tmp
RUN unzip -d /opt/gradle /tmp/gradle-7.4.2-bin.zip

ENV GRADLE_HOME=/opt/gradle/gradle-7.4.2
ENV JAVA_HOME=/opt/java/jdk-15.0.2

ENV PATH=$PATH:$GRADLE_HOME/bin
ENV PATH=$PATH:$JAVA_HOME/bin

COPY . .