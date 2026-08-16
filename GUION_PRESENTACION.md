# Guion para presentar Battle Arena 2D (5-7 minutos)

## 1. Introducción (1 min)
"Desarrollé un videojuego 2D multijugador para dos jugadores utilizando Java.
El proyecto usa arquitectura cliente-servidor mediante sockets, base de datos
MySQL conectada con JDBC, y un sistema de autenticación y estadísticas."

## 2. Arquitectura (1 min)
Muestra el diagrama del README. Frases clave:
- "El servidor es la autoridad: valida movimientos, disparos y victorias."
- "Los clientes solo envían acciones y pintan el estado que reciben."
- "Cada cliente tiene su propio hilo en el servidor (ClientHandler)."

## 3. Demo en vivo (3 min)
1. Inicia el servidor ANTES de la demo.
2. Abre cliente 1: registra un usuario en vivo (muestra que la contraseña se
   guarda como hash: abre Workbench y muestra la tabla usuarios).
3. Inicia sesión con ambos clientes.
4. Pulsa JUGAR en cliente 1: muestra "Esperando otro jugador...".
5. Pulsa JUGAR en cliente 2: la partida comienza.
6. Mueve y dispara con ambos: explica que el servidor difunde el estado
   20 veces por segundo.
7. Deja que un jugador gane: muestra pantallas VICTORIA/DERROTA.
8. Muestra la tabla partidas y las estadísticas actualizadas en MySQL.
9. Pulsa ESTADISTICAS en el menú: números reales desde la base.

## 4. Cierre (1 min)
- "Las contraseñas usan SHA-256 con salt, sin dependencias externas."
- "Todas las consultas usan PreparedStatement contra inyección SQL."
- "El guardado de resultados usa transacciones: todo o nada."
- Menciona mejoras futuras: obstáculos, más armas, más jugadores.

## Plan B si falla algo en vivo
- Ten 2 usuarios ya registrados.
- Ten el servidor corriendo y una partida de respaldo grabada en video.