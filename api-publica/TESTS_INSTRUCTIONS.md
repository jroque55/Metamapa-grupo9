Instrucciones para ejecutar pruebas JUnit y configuración recomendada

Resumen
- Problema detectado al ejecutar `mvn test`: Maven indicó "No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK". Esto es un problema del entorno (no hay un JDK disponible para que el plugin de compilación compile el código).

Pasos para arreglar el entorno (Windows, cmd.exe)
1) Instalar JDK 17 (recomendado):
   - Descarga OpenJDK 17 (por ejemplo, Adoptium Temurin): https://adoptium.net/es/temurin/releases/?version=17
   - Instala el JDK.

2) Configurar variables de entorno (cmd.exe):
   - Abre "Editar las variables de entorno del sistema" -> Variables de entorno.
   - Añade/edita JAVA_HOME apuntando a la carpeta de instalación del JDK (por ejemplo C:\Program Files\Eclipse Adoptium\jdk-17.0.x) 
   - Añade/edita PATH para que contenga `%JAVA_HOME%\bin` antes que otras entradas de Java.

3) Verificar desde cmd.exe:

   java -version
   javac -version

   Ambos comandos deben devolver la versión 17.x.

4) Ejecutar las pruebas con Maven desde la raíz del proyecto (cmd.exe):

   mvn test

Si quieres limpiar y volver a compilar:

   mvn clean test

Recomendaciones en `pom.xml` (si deseas aplicar mejoras para pruebas)
- El proyecto ya incluye `spring-boot-starter-test`, pero recomiendo asegurarse de tener el plugin de compilador y surefire configurados para Java 17. Si necesitas editar `pom.xml`, añade o revisa las siguientes secciones:

1) Dependencias de test adicionales (opcional, ya presentes en muchos proyectos):

<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>test</scope>
</dependency>

2) Plugins recomendados (maven-compiler-plugin y maven-surefire-plugin):

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.11.0</version>
      <configuration>
        <source>17</source>
        <target>17</target>
        <release>17</release>
      </configuration>
    </plugin>

    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.0.0</version>
      <configuration>
        <useModulePath>false</useModulePath>
      </configuration>
    </plugin>
  </plugins>
</build>

Nota: Debido a un problema con la herramienta de edición en esta sesión (rutas con caracteres especiales), no pude aplicar automáticamente estos cambios al `pom.xml`. Si quieres, puedo intentarlo otra vez si pegas aquí la ruta exacta o me das permiso para editar el archivo; otra opción es que copies y pegues las secciones anteriores en tu `pom.xml`.

Sugerencias para escribir pruebas JUnit en este proyecto
- Usa Mockito para mocks de repositorios (como ya está hecho en `src/test/...`).
- Para tests que requieran contexto de Spring (por ejemplo, repositorios JPA), usa `@DataJpaTest` con la base en memoria H2.
- Mantén las pruebas unitarias rápidas: mockea dependencias externas y valida la lógica en servicios.

Ejemplo rápido (ya existe algo similar en el repo):
- `src/test/java/.../HechoServiceTest.java` usa `@ExtendWith(MockitoExtension.class)` e `@InjectMocks` para testear `HechoService` con repositorios mockeados.

¿Quieres que intente aplicar los cambios al `pom.xml` aquí (vuelvo a intentarlo) o prefieres que te deje el fragmento listo para pegar? También puedo agregar un ejemplo de `README` y/o tests adicionales si lo deseas.

