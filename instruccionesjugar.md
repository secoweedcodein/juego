# Instrucciones para ejecutar Battle Arena 2D

## Requisitos

- Tener instalado Java 17 o superior.
- Tener conexión a Internet.
- Descargar o clonar este repositorio.

## Ejecutar el juego

1. Abre una terminal dentro de la carpeta del proyecto.
2. Ejecuta los siguientes comandos:

```powershell
$env:BATTLE_ARENA_HOST="tokaido.proxy.rlwy.net"
$env:BATTLE_ARENA_PORT="40933"
java -cp "out;lib/*" com.battlearena.client.Client
```

3. Se abrirá la ventana de Battle Arena 2D.
4. Crea una cuenta o inicia sesión.
5. Presiona **Jugar**. El primer jugador verá una pantalla de espera.

Para comenzar una partida se necesitan dos jugadores. El segundo jugador debe ejecutar los mismos comandos desde otro computador, o desde otra terminal, e iniciar sesión con una cuenta diferente.

## Controles

| Jugador 1 | Jugador 2 |
|---|---|
| W, A, S, D: mover | Flechas: mover |
| Espacio: atacar | Enter: atacar |

## Notas

- El juego se conecta a un servidor remoto; no es necesario instalar MySQL ni ejecutar un servidor local.
- No abras la dirección del servidor en un navegador: el juego usa una conexión TCP, no una página web.
- Si el juego no inicia, verifica que Java esté instalado ejecutando:

```powershell
java -version
```

- Si aparece un error de conexión, vuelve a intentar más tarde o consulta al responsable del proyecto para confirmar que el servidor sigue activo.