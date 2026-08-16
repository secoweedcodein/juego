# Battle Arena 2D

Videojuego 2D multijugador para dos jugadores, desarrollado en Java con
arquitectura cliente-servidor mediante sockets, base de datos MySQL con JDBC,
autenticación con hashing de contraseñas y sistema de estadísticas.

## Tecnologías

- Java 21 (Swing para interfaz y gráficos 2D)
- Sockets TCP (ServerSocket / Socket) con serialización de objetos
- MySQL + JDBC (Connector/J)
- Git

## Diagrama de arquitectura

```
 CLIENTE 1 (Swing)                CLIENTE 2 (Swing)
      |  Socket TCP (objetos Message)  |
      +-------------->-----------------+
                     |
              SERVIDOR JAVA
              (autoridad del juego:
               valida movimientos,
               disparos, daño y victoria)
                     |
                  JDBC
                     |
               BASE DE DATOS MySQL
              (usuarios y partidas)
```

## Diagrama de base de datos

```
usuarios                          partidas
+-------------------+             +------------------+
| id (PK)           |<---------+  | id (PK)          |
| username (UNIQUE) |          |  | jugador1_id (FK) |--> usuarios.id
| password (hash)   |          +--| jugador2_id (FK) |--> usuarios.id
| partidas_jugadas  |          |  | ganador_id (FK)  |--> usuarios.id
| victorias         |          |  | fecha            |
| derrotas          |----------+  +------------------+
| fecha_registro    |
+-------------------+
```

## Estructura del proyecto

```
src/com/battlearena/
├── client/            (interfaz y conexión del cliente)
│   ├── Client.java            punto de entrada del cliente
│   ├── game/GamePanel.java    arena 2D y captura de teclado
│   ├── network/ServerConnection.java  socket del cliente + hilo lector
│   └── ui/                      pantallas (login, registro, menú, espera, resultado)
├── server/            (autoridad del juego)
│   ├── Server.java            abre el ServerSocket y acepta clientes
│   ├── network/ClientHandler.java  un hilo por cliente, procesa mensajes
│   ├── game/GameRoom.java     empareja 2 jugadores y controla "partida llena"
│   ├── game/Game.java         lógica: movimiento, proyectiles, daño, victoria
│   ├── database/              acceso a MySQL (usuarios, partidas, transacciones)
│   └── util/PasswordHasher.java  hashing SHA-256 + salt
└── shared/            (compartido)
    ├── protocol/      Message, MessageType, ConfigRed
    └── models/        GameState, PlayerState, Projectile
```

## Clases principales

- **Server**: espera conexiones en el puerto 5000 y crea un ClientHandler por cliente.
- **ClientHandler**: lee mensajes del cliente y responde. Es el puente entre red y lógica.
- **GameRoom**: sala única de máximo 2 jugadores. El tercero recibe GAME_FULL.
- **Game**: estado de la partida y game loop (20 ticks/segundo) que mueve proyectiles.
- **ServerConnection**: socket del cliente con hilo que recibe mensajes del servidor.
- **UserRepository / MatchRepository**: consultas SQL seguras con PreparedStatement.
- **PasswordHasher**: SHA-256 con salt aleatorio, sin dependencias externas.

## Funcionamiento cliente-servidor

1. El cliente se conecta y recibe CONNECTED.
2. Envía REGISTER o LOGIN; el servidor verifica contra MySQL y responde.
3. Con sesión iniciada, JUGAR envía JOIN_GAME.
4. Cuando hay 2 jugadores, el servidor crea la partida y envía GAME_START.
5. El cliente envía PLAYER_MOVE y PLAYER_ATTACK (teclado).
6. El servidor valida todo, mueve proyectiles en su game loop y difunde GAME_STATE.
7. Al llegar a 0 HP, el servidor envía GAME_OVER, guarda la partida en MySQL y
   actualiza victorias/derrotas dentro de una transacción.

## Instalación

1. Instalar JDK 17 o superior y MySQL Server.
2. Descargar MySQL Connector/J y copiar el .jar en la carpeta `lib/`.
3. Crear la base de datos:

```
mysql -u root -p < sql/battle_arena.sql
```

(o abrir el script en MySQL Workbench y ejecutarlo).

4. Editar `src/com/battlearena/server/database/DatabaseConfig.java` con tu
   usuario y contraseña de MySQL.

## Ejecución

Compilar:

```
javac -encoding UTF-8 -cp "lib/*" -d out (todos los .java de src)
```

Servidor:

```
java -cp "out;lib/*" com.battlearena.server.Server
```

Cliente 1 y Cliente 2 (dos terminales):

```
java -cp "out;lib/*" com.battlearena.client.Client
```

## Controles

| Jugador 1 | Jugador 2 |
|-----------|-----------|
| W A S D mover | Flechas mover |
| ESPACIO disparar | ENTER disparar |

## Funcionalidades

- Registro e inicio de sesión con contraseñas hasheadas (SHA-256 + salt).
- Menú principal con estadísticas reales desde MySQL.
- Sala de espera ("Esperando otro jugador...") y "La partida está llena".
- Arena 2D de 800x600 con límites, barras de vida y proyectiles.
- Servidor autoritario: el cliente nunca decide daño ni victoria.
- Guardado de partidas y actualización de estadísticas con transacciones.
- Victoria por KO o por desconexión del rival.
- Manejo de errores sin cierres inesperados.

## Problemas encontrados y soluciones

- **Paquetes vs carpetas**: un archivo .java debe estar en la carpeta que coincide
  con su `package`. Se solucionó reorganizando carpetas.
- **Orden de streams**: crear ObjectInputStream antes que ObjectOutputStream
  congela la conexión. Regla del proyecto: primero salida, luego entrada.
- **Swing y threads**: los mensajes llegan en el hilo de red; toda actualización
  de interfaz se pasa al EDT con SwingUtilities.invokeLater.
- **Puerto ocupado**: si el puerto 5000 está en uso, se cambia solo en ConfigRed.

## Seguridad

- Contraseñas nunca en texto plano (hash + salt).
- PreparedStatement en todas las consultas con datos del usuario.
- Credenciales de MySQL centralizadas en DatabaseConfig.
- El servidor valida todas las acciones del cliente.