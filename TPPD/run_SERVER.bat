cd Servidor\out\artifacts\TPPD_jar\
java -jar TPPD.jar localhost/pd
cmd \k
::cd Servidor\src\
::javac -cp ".\Servidor\lib\mysql-connector-java-8.0.13.jar" Controlador.Main.java
::cmd /k 


::cd Servidor\out\production\TPPD\
::java -cp ".\Servidor\lib\mysql-connector-java-8.0.13.jar" Controlador.Main localhost/pd
::cmd /k 


::cd Servidor\out\production\TPPD\
::javac -cp ".\Servidor\lib\mysql-connector-java-8.0.13.jar" Controlador.Main.java
::cmd /k 