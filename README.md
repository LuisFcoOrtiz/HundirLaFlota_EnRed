# HundirLaFlota_EnRed
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
