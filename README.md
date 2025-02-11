
# API REST Sistema Liga de Fútbol. ⚽💻

Backend desarrollado en spring boot.


## Implementación de:

✔ Spring security + JWT + roles.

✔ Manejo de excepciones y validaciones customizadas.

✔ Consumo de api Imgur para almacenamiento de imágenes.

✔ Integración con Jaspersoft Reports para generación de archivos PDF.

✔ Consumo de api stripe para gestión de pagos.

✔ Uso de Spring Data Jpa, Spring Validatión, documentación en Swagger y mucho más. 




## 💻Como usar el proyecto
Para correr este proyecto, debes crear un 
`application-dev.properties`

Puedes tomar el `application-properties.example`  y copiarlo a tu `application-dev.properties` para llenar lo siguiente:

## 🛠Configuración de base de datos
En tu gestor de base de datos, en mi caso MySQL, debes crear la base de datos `sistemafutbolBD`

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/sistemafutbolBD
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql: true

## 🛠Configuración Spring Security
Asigna valores para la generación del token. Nota, expiration se considera en milisegundos.

`jwt.secret`

`jwt.issuer`

`jwt.expiration`

## 🛠Configuración de Imgur
Debes registrarte en Imgur para crear tus credenciales:

`imgur.client.id=`
`imgur.client.secret=`

## 🛠Configuración del usuario administrador
Se creará (si no existe) un usuario administrador, para ello define sus credenciales: 

`admin.email=`
`admin.password`









![Logo](https://th.bing.com/th/id/OIP.8T-A0VXHyVMpArtaF1r7TgHaDy?rs=1&pid=ImgDetMain)


## Authors

- [@JonaBarrera](https://github.com/JonathanAlbertoBarrera)

