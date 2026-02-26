# Metamapa-grupo9

📁 Configuración de Base de Datos
🐬 MySQL

El archivo:

database/mysql/schema_and_data.sql

contiene la estructura y datos de prueba.

Ejecutar:

Crear una base de datos vacía en MySQL:

CREATE DATABASE nombre_db;

Importar el script desde consola:

mysql -u root -p nombre_db < database/mysql/schema_and_data.sql


🍃 MongoDB

Las colecciones se encuentran en:

database/mongo/

Cada archivo .json corresponde a una colección.

Importar colecciones:

Ejemplo:

mongoimport --uri "TU_URI_MONGO" --collection usuarios --file database/mongo/usuarios.json --jsonArray

Repetir el comando para cada archivo JSON.

Nota:
Las credenciales reales no se incluyen por motivos de seguridad.
El proyecto puede ejecutarse recreando las bases de datos mediante los archivos incluidos en el repositorio.