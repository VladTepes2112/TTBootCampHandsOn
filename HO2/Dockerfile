FROM tomcat:jdk8
CMD ["catalina.sh", "run"]
RUN rm -rf webapps && mv webapps.dist webapps
COPY webapp/ webapps/math-api
RUN rm webapps/ROOT/index.jsp
RUN rm -rf webapps/examples
COPY webapp/index.jsp webapp/ROOT/index.jsp
MAINTAINER Víctor Carrillo "victorlicioso@live.com.mx"