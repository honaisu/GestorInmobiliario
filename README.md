# Proyecto SIA: Gestor Inmobiliario

Sistema de escritorio para la gestión de ventas en el sector inmobiliario, desarrollado como un proyecto universitario para el curso de Sistemas de Información y Algoritmos. La aplicación centraliza la administración de proyectos de departamentos, implementando una arquitectura robusta de tres capas y persistencia de datos a través de SQLite.

---

## Funcionalidades Clave

- **Gestión Completa (CRUD):** Creación, visualización, modificación y eliminación de proyectos inmobiliarios.
- **Navegación Jerárquica:** Interfaz intuitiva para navegar desde los proyectos hasta sus edificios y departamentos asociados.
- **Búsqueda Avanzada por Criterios:** Potente ventana de filtrado que permite a los usuarios buscar departamentos por rango de precios, tamaño, ubicación y otras características.
- **Persistencia de Datos con SQLite:** Toda la información se almacena en una base de datos local SQLite, gestionada a través de una capa de persistencia con caché en memoria para un rendimiento óptimo.
- **Generación de Recibos en TXT:** Creación automática de un recibo en formato `.txt` al completar una compra.
- **Validación de Datos:** Implementación de validadores y excepciones personalizadas para asegurar la integridad de los datos ingresados por el usuario (RUT, email, etc.).

---

## Arquitectura y Diseño Técnico

El sistema se fundamenta en el paradigma de **Programación Orientada a Objetos** y sigue una **arquitectura de tres capas** para separar responsabilidades y facilitar la escalabilidad.

1. **Capa de Vista (GUI):** Desarrollada íntegramente con **Java Swing**. La clase `VisualDisplayer` se encarga de construir y gestionar todas las ventanas e interacciones con el usuario.
2. **Capa de Servicio (Lógica de Negocio):** Orquestada por la clase `GestorInmobiliarioService`, que aplica el patrón de diseño **Facade** para servir como único punto de entrada a la lógica del negocio.
3. **Capa de Persistencia:** Gestionada por la clase `DatabaseManager` (Singleton), que maneja toda la comunicación con la base de datos **SQLite**. Implementa un sistema de caché en memoria (`Map`) para minimizar las consultas a la base de datos y mejorar la fluidez de la aplicación.

### Principios de Diseño Aplicados

- **Encapsulamiento:** Todos los atributos de las clases del modelo son `private`, con acceso controlado mediante métodos `get` y `set`.
- **Sobrecarga de Métodos:** Se utiliza principalmente en los **constructores** de las clases del modelo (`Edificio`, `Departamento`, etc.) para permitir su creación en diferentes contextos.
- **Sobrescritura de Métodos:** Se aplica en clases anónimas para personalizar el comportamiento de componentes de Swing, como `DefaultTableModel` y `DefaultTableCellRenderer`.

---

## Tecnologías Utilizadas

- **Lenguaje:** Java
- **Interfaz Gráfica:** Java Swing
- **Base de Datos:** SQLite (a través del driver `sqlite-jdbc`)
- **Control de Versiones:** Git y GitHub

---

## Instalación y Ejecución

**Pre-requisitos:**

- Tener instalado el JDK (Java Development Kit).
- Tener instalado Git.

**Pasos:**

1. Clona el repositorio desde GitHub:
    
    ```bash
    git clone [<https://github.com/honaisu/GestorInmobiliario.git>](<https://github.com/honaisu/GestorInmobiliario.git>)
    
    ```
    
2. Navega al directorio del proyecto:
    
    ```bash
    cd GestorInmobiliario
    
    ```
    
3. Asegúrate de que el driver de SQLite (`sqlite-jdbc.jar`) esté en el classpath. Si usas un IDE como Eclipse o IntelliJ, puedes añadirlo como una librería externa al proyecto.
4. Compila y ejecuta la aplicación. Desde la terminal, puedes hacerlo de la siguiente manera:
    
    ```bash
    # Compilar (asumiendo que las fuentes están en 'src' y el .jar en 'lib')
    javac -cp "lib/sqlite-jdbc.jar:." -d bin src/**/*.java
    
    # Ejecutar
    java -cp "lib/sqlite-jdbc.jar:bin" main.Main
    
    ```
    

---

## Autores

- Benjamín Contreras
- Anselmo Díaz
- Ignacio Ruiz