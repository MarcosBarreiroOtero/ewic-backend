# Backend EWIC

Parte backend del sistema ewic donde se gestionan los datos almacenados en la base de datos para posteriormente ofrecerlos al resto de componentes mediante un API REST.

## Requisitos

- Base de datos mysql accesible desde donde está corriendo el backend. Por el momento, de forma temporal, la base de datos tiene que estar en localhost.
- En la base de datos hay que tener una database llamanda ewic con un usuario con nombre y contraseña ewic con datos.
- Tener instalado maven.
- Tener el puerto 8080 libre.

Algunos de los requisitos ligados a configuración son temporales y en un futuro se permitirá a cada usuario que establezca los que prefiera a partir de un archivo de 
propiedades.

## Modo de uso

1. Descargar el código
2. Ejecutar el script de creación de la base de datos situado en /doc/Create.sql. Realizar está operación solo la primera vez que se lanza la aplicación ya que eliminar los datos guardados anteriormente.

        mysql -uewic -pewic -Dewic < Create.sql
        
3. Lanzar el servidor de tomca en el puerto 8080 que expone la API REST

        mvn spring-boot:run
        
 
También se puede crear un archivo de tipo jar mediante **mvn package** que al ejecutarlo lanza el tomcat de la misma forma que se produce en el paso número 3. 
El problema desta alternativa es que la forma, a día de hoy, que hay para dejar de exponer la API REST es buscando el pid del proceso que inicia el jar y matandolo.
