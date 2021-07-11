#!/bin/bash

/usr/bin/java -Xmx1g -Xms256m \
-DremoteHost=${REMOTE_HOST} \
-DremotePort=${REMOTE_PORT} \
-dbindAddr=$(BIND_ADDR) \
-Dport=${PORT} \
-Dfile.encoding=UTF-8 \
-jar ./build/libs/tcp-proxy.jar
