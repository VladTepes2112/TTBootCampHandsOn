###HANDS ON #4 VÍCTOR RAMÓN CARRILLO QUINTERO
this is the same as HO2 :shrug:

####In the course call, Thirupati mentioned that he wanted us to try with pure tomcat (No spring boot)
That way we would learn how big of a problem spring was helping us with, I also wanted to practice with Docker so here is my hands on #2

###### Build container:
`docker build -t hands_on_2 .`
###### Run container:
`docker run -it --rm -p 80:8080 hands_on_2`
###### Run container if you're going to do changes:
`docker run -it --rm -p 80:8080 -v C:FullPath\HO2\webapp:/usr/local/tomcat/webapps/math-api hands_on_2`
###### e.g: `docker run -it --rm -p 80:8080 -v C:\Users\victo\Documents\PersonalCode\DOCKER_RELATED\TTBootCampHandsOn\HO2\webapp:/usr/local/tomcat/webapps/math-api hands_on_2`
###### Pre-Compile java classes (you need to be from the java directories):
`javac8 -cp .;C:/apache-tomcat-8.5.45/lib/servlet-api.jar ./victor/*.java`

#### If you run it locally you it has to be in the folder "tomcat/webapps/math-api"