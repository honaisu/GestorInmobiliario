# Proyecto SIA: Gestor Inmobiliario

Sistema de software para la gestión de ventas en el sector inmobiliario, desarrollado como un proyecto universitario. La aplicación centraliza la administración de múltiples proyectos de departamentos e incorpora una lógica de negocio con precios dinámicos que se adaptan a las condiciones del mercado en tiempo real.

---

## Objetivo del Proyecto

El objetivo principal es desarrollar un sistema para el manejo integral del ciclo de venta de departamentos, implementando un modelo de precios dinámico que se ajuste automáticamente a la oferta de propiedades y a la demanda de los clientes.

---

## Funcionalidades Principales
- **Gestión de Proyectos**: Permite a los vendedores administrar los proyectos y las unidades que tienen a la venta.
- **Búsqueda y Reserva**: Ofrece a los compradores una interfaz para buscar y reservar departamentos que cumplan con sus criterios.
- **Algoritmo de Precios Dinámico**: Incluye un componente central que ajusta el precio de las propiedades basándose en la oferta disponible y la demanda de los usuarios activos.
  
---

## Diseño Técnico 

El diseño del sistema se fundamenta en el paradigma de Programación Orientada a Objetos para modelar las entidades del mundo real de forma coherente y escalable.
- **Arquitectura de Capa de Servicio**: La lógica de negocio está orquestada por la clase `GestorInmobiliarioService`, que actúa como una fachada para centralizar las operaciones y gestionar las colecciones de datos principales.
- **Uso de Estructuras de Datos**:
    - Se utiliza la estructura de datos `Map` del Java Collections Framework para almacenar proyectos, usuarios y reservas.
    - Las clases `ProyectoInmobiliario`, `Edificio` y `Departamento` se modela mediante colecciones anidadas para una representación natural de los datos.
- **Encapsulamiento**: Todos los atributos de las clases son `private` para proteger la integridad del estado de los objetos. El acceso se controla a través de métodos públicos `get` y `set`.  
- **Interfaz de Consola**: Las funcionalidades del sistema son operables a través de una interfaz de texto estándar.

---

## Tecnologías
- **Lenguaje**: Java (JDK 11).
- **Control de Versiones**: Git y GitHub

---

## Instrucciones de Ejecución

1. Clonar el repositorio desde GitHub: `git clone https://github.com/honaisu/GestorInmobiliario.git`
2. Navegar al directorio del proyecto.
3. Compilar los archivos fuente `.java`.
4. Ejecutar la clase principal que contiene el método `main`, ubicada en la carpeta `src/main/Main.java`.
