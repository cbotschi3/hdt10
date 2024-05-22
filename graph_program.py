import networkx as nx
import numpy as np

def load_graph(filename):
    G = nx.DiGraph()
    with open(filename, 'r') as file:
        for line in file:
            city1, city2, distance = line.split()
            G.add_edge(city1, city2, weight=int(distance))
    return G

def floyd_warshall_shortest_paths(G):
    return dict(nx.floyd_warshall_predecessor_and_distance(G))

def get_path(predecessors, start, end):
    if start == end:
        return [start]
    if end not in predecessors[start]:
        return []
    path = [end]
    while end in predecessors[start]:
        end = predecessors[start][end]
        path.append(end)
    path.reverse()
    return path

def find_graph_center(G):
    fw_result = nx.floyd_warshall(G)
    eccentricity = nx.eccentricity(G, sp=fw_result)
    center = nx.center(G, e=eccentricity)
    return center[0] if center else None

def main():
    G = load_graph("guategrafo.txt")
    predecessors, distances = floyd_warshall_shortest_paths(G)

    while True:
        print("1. Obtener ruta más corta")
        print("2. Mostrar centro del grafo")
        print("3. Modificar grafo")
        print("4. Salir")
        option = int(input())

        if option == 1:
            start_city = input("Ingrese la ciudad origen: ")
            end_city = input("Ingrese la ciudad destino: ")
            path = get_path(predecessors, start_city, end_city)
            if path:
                print(f"Distancia más corta: {distances[start_city][end_city]}")
                print(f"Ruta: {' -> '.join(path)}")
            else:
                print("No hay ruta disponible.")

        elif option == 2:
            center = find_graph_center(G)
            print(f"Centro del grafo: {center}")

        elif option == 3:
            print("1. Interrupción de tráfico")
            print("2. Establecer conexión")
            mod_option = int(input())

            if mod_option == 1:
                city1 = input("Ingrese la ciudad1: ")
                city2 = input("Ingrese la ciudad2: ")
                if G.has_edge(city1, city2):
                    G.remove_edge(city1, city2)
                    predecessors, distances = floyd_warshall_shortest_paths(G)

            elif mod_option == 2:
                city1 = input("Ingrese la ciudad1: ")
                city2 = input("Ingrese la ciudad2: ")
                distance = int(input("Ingrese la distancia en KM: "))
                G.add_edge(city1, city2, weight=distance)
                predecessors, distances = floyd_warshall_shortest_paths(G)

        elif option == 4:
            break

if __name__ == "__main__":
    main()
