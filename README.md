# HundirLaFlota_EnRed | Battleship_network
# [ES]
Juego de hundir la flota en red local con sockets TCP.
Contiene 2 programas, servidor que contiene la lógica del juego y visualiza las jugadas y cliente que efectua las jugadas 
(El cliente se ejecuta una vez para cada jugador).

# Caracteristicas
Servidor
* Inicia 2 hilos de ejecución para activar los sockets TCP
* Tiene 2 puertos de escucha TCP (6000 y 6002) los cuales reciben las jugadas del programa cliente
* La lógica del programa esta en la clase (Partida) la cual tiene métodos sincronizados.
* Los hilos de juego de cada jugador acceden a un recurso (Turno) el cual esta sincronizado con los métodos (wait()) y (notify())
para que cada jugador ejecute su jugada de forma ordenada y no pueda un jugador ejecutar 2 jugadas seguidas
* Mantiene en tiempo real la lógica de partida para ver quien gana y como va cada jugada

Cliente
* Inicia 1 hilo para el envio de jugadas al programa servidor
* Inicia una conexion cliente socket TCP al host y puerto indicado 
    *(6000) jugador 1
    *(6002) jugador 2
* Manda los datos necesarios para crear el objeto (Partida) de cada jugador
* Manda las posiciones de disparo para cada jugada

# [EN]
Battleship local network game with TCP Sockets
2 programs, Server with game logic and Client to play and send each move.
(Client must be run for each player)

# Features
Server
* Starts 2 threads to activate TCP sockets
* Has 2 TCP ports to listen the movements (6000 and 6002)
* (Partida) is the class with the game logic with synchronized methods
* Threads access to the same resource (Turno) wich is synchronized with (wait()) and (notify()) methods, with this 2 player play orderly one time for each player

Client
* Starts 1 thread to send the game movements
* Starts the client socket TCP to the host and port indicated
* Send the data to create the (Partida) object for each player
* Send the shoot possitions to shoot the other player

# Capturas | screenshots

![1](https://user-images.githubusercontent.com/8844134/33661452-3a29771a-da88-11e7-866f-45afd2526caa.png)

![3](https://user-images.githubusercontent.com/8844134/33661485-685477c0-da88-11e7-9259-76aba529c7a9.png)
