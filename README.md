# Tigerdb

## Motor de base de datos orientado a objetos desarrollado completamente en Java

Para utilizarlo solo debe importar este proyecto como dependecia maven, agregando el siguiente tag:

	<repositories>
		<repository>
			<id>clojars</id>
			<name>Clojars repository</name>
			<url>https://clojars.org/repo</url>
		</repository>
	</repositories>

La dependecia es:

	<dependency>
		<groupId>org.tigerdb</groupId>
    		<artifactId>tigerdb</artifactId>
    		<version>2.2</version>
	</dependency>

Tigerdb opera con lion como motor de almacenamiento, es decir, el proyecto que se encarga de darle un formato específico a los datos que son guardados en un archivo, si desea sustituir lion por otro motor de almacenamiento programado por usted, deberá agregarlo como dependencia a este proyecto si esta forkeado, o al propio que utiliza tigerdb como dependencia. Para que su nuevo motor sea compatible con tigerdb, usted deberá declarar alguna clase que usted utilice para el almacenaje de datos, de la Clase Abstracta StoreManager<T> provista por tiger-bridge, la api del proyecto que realiza el puente entre tigerdb y su proyecto, que esta presente dento del classpath de este proyecto. Al finalizar, cuando tigerdb inicie cargará el archivo tigerdb.properties, en donde se encuentra la propiedad "store.classname", si este archivo no existe el motor cargará lion por defecto, usted deberá crear el archivo en la ruta de su proyecto con el siguiente formato:
	store.classname=nombre-completo-de-su-clase-almacenadora

Esto permitirá que sea cargado su motor de almacenamiento y tigerdb lo podrá utilizar.

## Lion: https://github.com/martinpiz097/lion
## tiger-bridge: https://github.com/martinpiz097/tiger-bridge

###############################################################################################################################################################

# Tigerdb

## Object oriented database engine developed completely in Java

To use it you should only import this project as maven dependcia, adding the following tag:

	<repositories>
		<repository>
			<id>clojars</id>
			<name>Clojars repository</name>
			<url>https://clojars.org/repo</url>
		</repository>
	</repositories>

Dependence is:

	<dependency>
		<groupId>org.tigerdb</groupId>
    		<artifactId>tigerdb</artifactId>
    		<version>2.2</version>
	</dependency>

Tigerdb operates with lion as a storage engine, that is, the project that is responsible for giving a specific format to the data that is saved in a file, if you want to replace lion with another storage engine programmed by you, you must add it as a dependency to this project if it is forged, or to the one that uses tigerdb as a dependency. In order for your new engine to be compatible with tigerdb, you must declare some class that you use for the storage of data, of the StoreManager Abstract Class <T> provided by tiger-bridge, the project api that makes the bridge between tigerdb and its project, which is present in the classpath of this project. At the end, when tigerdb starts loading the file tigerdb.properties, where the property "store.classname" is found, if this file does not exist the engine will load lion by default, you must create the file in the path of your project with the following format:
store.classname = full-name-of-your-class-store

This will allow your storage engine to be loaded and tigerdb will be able to use it.

## Lion: https://github.com/martinpiz097/lion
## tiger-bridge: https://github.com/martinpiz097/tiger-bridge
