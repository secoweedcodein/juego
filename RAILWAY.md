# Despliegue en Railway

El repositorio incluye `pom.xml` y `railway.json` para construir un JAR ejecutable
del servidor. En Railway crea dos servicios dentro del mismo proyecto: **MySQL** y
el servicio de este repositorio.

En el servicio del juego define estas variables, usando referencias a las variables
del servicio MySQL. No copies contrasenas al codigo:

```
PORT=5000
DB_HOST=${{MySQL.MYSQLHOST}}
DB_PORT=${{MySQL.MYSQLPORT}}
DB_NAME=${{MySQL.MYSQLDATABASE}}
DB_USER=${{MySQL.MYSQLUSER}}
DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
```

Luego, en **Networking**, crea un **TCP Proxy** hacia el puerto interno `5000`.
Railway entregara un dominio y un puerto publicos. Esos valores se usan al ejecutar
cada cliente:

```
BATTLE_ARENA_HOST=dominio-del-tcp-proxy
BATTLE_ARENA_PORT=puerto-del-tcp-proxy
```

El servidor crea automaticamente las tablas `usuarios` y `partidas` al iniciar.
Para desarrollo local se mantienen los valores por defecto (`localhost:5000` y una
base de datos local). Define `DB_PASSWORD` en el entorno, nunca en el repositorio.
