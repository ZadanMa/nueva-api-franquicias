# API Franquicias - Aplicación Reactiva con Spring Boot, WebFlux y MongoDB

Esta aplicación es una API para manejar una lista de franquicias, donde cada franquicia tiene un nombre y una lista de sucursales. Cada sucursal tiene un nombre y una lista de productos, y cada producto se define por su nombre y cantidad de stock. La aplicación está desarrollada con Spring Boot, WebFlux y MongoDB en modo reactivo, utilizando un enfoque basado en Router/Handler para aprovechar al máximo la programación funcional y no bloqueante.
El api esta en fase de desarrollo, para acceder a la api, debe dirigirse a la rama dev.

## Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Instalación y Ejecución Local](#instalación-y-ejecución-local)
- [Ejecución con Docker](#ejecución-con-docker)
- [Endpoints Principales](#endpoints-principales)
- [Notas Adicionales](#notas-adicionales)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)

## Características

- **Programación Reactiva:** Utiliza Spring WebFlux y el driver reactivo de R2DBC para operaciones no bloqueantes.
- ** Arquitectura hexagonal para separación clara de responsabilidades
- ** Manejo estructurado de errore
- ** Endpoints Compuestos:** Incluye endpoints para asociar sucursales y productos a franquicias existentes.
- ** Dockerizado:** La aplicación se empaqueta en una imagen Docker ligera basada en JDK 17 Alpine.
- ** Formato de respuesta estandarizado

## Arquitectura

El proyecto sigue una arquitectura hexagonal (también conocida como "Ports & Adapters") con la siguiente estructura:
```bash  
src/
├── main/
│   ├── java/
│   │   └── proyecto/nequi/api_franquicias/
│   │       ├── application/          # Configuración y casos de uso
│   │       │   └── UsecaseConfig/  
│   │       ├── domain/               # Lógica de negocio central
│   │       │   ├── api/              # Puertos (Service Ports)
│   │       │   ├── model/            # Entidades del dominio
│   │       │   ├── spi/              # Interfaces para persistencia
│   │       │   ├── usecase/          # Casos de uso (business logic)
│   │       │   ├── enum/             # Enumeraciones del sistema
│   │       │   └── exceptions/       # Excepciones de dominio
│   │       └── infrastructure/       # Adaptadores (persistencia, endpoints)
│   │           ├── adapters/         # Implementaciones de puertos
│   │           │   └── persistence/  # Repositorios y mappers
│   │           └── entrypoints/      # Handlers y routers
│   └── resources/                    # Configuración (application.properties)
└── test/                             # Tests unitarios e integración
```
## Este proyecto implementa una arquitectura hexagonal que separa el código en tres capas principales:
### 1. Dominio (Centro)
####   - El núcleo de la aplicación contiene toda la lógica de negocio y está completamente aislado de las tecnologías externas.

####   - Modelos: Representan entidades del negocio (Franquicia, Sucursal, Producto).
####   - Puertos Primarios (API): Definen servicios que el dominio ofrece al exterior.
####   - Puertos Secundarios (SPI): Especifican cómo el dominio necesita interactuar con servicios externos.
####   - Casos de uso: Implementan la lógica de negocio utilizando puertos secundarios.

### 2. Infraestructura (Periferia)
####   - Conecta el dominio con el mundo exterior e implementa los detalles técnicos.

####   - Adaptadores de Entrada: Implementan la API REST mediante handlers y routers.
####   - Adaptadores de Salida: Implementan la persistencia mediante repositorios y entidades.
####   - DTOs y Mappers: Transforman datos entre el formato externo y el dominio.

### 3. Aplicación (Configuración)
####   - Configura y conecta los componentes del sistema.
####   - Configuración de casos de uso: Inyecta dependencias y conecta adaptadores con puertos.

### 4. Tecnologías Utilizadas

####   - Java 17
####   - Spring Boot 3.x
####   - Spring WebFlux: Framework reactivo para APIs web
####   - R2DBC: Acceso reactivo a bases de datos relacionales
####   - MapStruct: Generación automática de código para mapeo de objetos
####   - Lombok: Reducción de código boilerplate
####   - JUnit 5 y Mockito: Testing
####   - OpenAPI/Swagger: Documentación de API

### 5. Flujo de Peticiones

  #### - Las peticiones HTTP son recibidas por los Routers
  #### - Los Handlers procesan la petición y llaman a los Puertos de Servicio
  #### - Los Casos de Uso implementan la lógica de negocio
  #### - Los Puertos de Persistencia son utilizados para acceder a datos
  #### - Los Adaptadores de Persistencia implementan el acceso a la base de datos
  #### - La respuesta fluye de vuelta por el mismo camino

### 6. Modelo de Datos
  #### - El sistema gestiona las siguientes entidades:
  
  #### - Franquicia: Representa una marca o empresa con múltiples sucursales
  #### - Sucursal: Establecimiento físico perteneciente a una franquicia
  #### - Producto: Artículo o servicio ofrecido en las sucursales

### 7. API REST
  #### - Formato de Respuesta
```bash
jsonCopiar{
  "code": "string",
  "message": "string",
  "data": { ... },
  "errors": [
    {
      "code": "string",
      "message": "string",
      "param": "string"
    }
  ]
}
```
### 8. Manejo de Errores
  #### - El sistema diferencia entre dos tipos de errores:
  
  - #### - Errores de Negocio (BusinessException): Representan violaciones de reglas de negocio
  - #### - Errores Técnicos (TechnicalException): Problemas de infraestructura o técnicos

## Requisitos

  - Java 17
  - Gradle 
  - MySql (puedes usar una instancia local o remota)
  - Docker (opcional, para empaquetar y desplegar la aplicación)

## Instalación y Ejecución Local

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuusuario/api-franquicias-nequi.git
cd api_franquicias
```

### 2. Configurar Mysql
- Si usas MongoDB localmente, asegúrate de que esté corriendo en mongodb://localhost:27017. Puedes usar Docker para levantar una instancia:
```bash
docker run -d -p 3306:3006 --name mysql mysql:latest
```

### 3. Construir y ejecutar la aplicación
```bash
mvn clean install
mvn spring-boot:run
```
## Ejecución con Docker
- Para construir y ejecutar la aplicación en un contenedor Docker:
```bash
docker build -t api-franquicias .
docker run -p 8080:8080 --name api-franquicias api-franquicias
```
## Endpoints Principales
### Franquicias

- POST /franquicias - Registrar una franquicia

- PUT /franquicias/{franquiciaId} - Actualizar nombre de una franquicia

- GET /franquicias/{franquiciaId}/full - Obtener información completa de una franquicia

### Sucursales

- POST /api/franquicias/{franquiciaId}/sucursales - Agregar una nueva sucursal a una franquicia
  
- GET /api/franquicias/sucursales - Obtener todas las sucursales

- GET /api/franquicias/sucursales/{sucursalId} - Obtener una sucursal por ID

- PUT /api/franquicias/sucursales/{sucursalId} - Actualizar el nombre de una sucursal

- GET /franquicias/{franquiciaId}/productos-mas-stock - Obtener el producto con más stock en una sucursal de la franquicia


### Productos
- POST /api/franquicias/sucursales/{sucursalId}/productos - Agregar un producto a una sucursal

- POST /api/franquicias/productos - Registrar un nuevo producto

- PUT /api/franquicias/sucursales/{sucursalId}/productos/asociar/{productoId} - Asociar un producto a una sucursal

- GET /api/franquicias/productos - Obtener todos los productos

- GET /api/franquicias/productos/{productoId} - Obtener un producto por ID

- DELETE /api/franquicias/sucursales/{sucursalId}/productos/{productoId} - Eliminar un producto de una sucursal

- PUT /api/franquicias/productos/{productoId}/stock - Actualizar stock de un producto

- PUT /api/franquicias/productos/{productoId}/nombre - Actualizar nombre de un producto



### Terraform

## Notas Adicionales
- La aplicación está diseñada para ser reactiva y escalar de manera eficiente.

- Se recomienda el uso de herramientas como Postman o cURL para probar los endpoint

## Contribuciones

## Licencias

