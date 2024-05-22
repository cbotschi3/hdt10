import java.io.*;
import java.util.*;

public class Graph {
    private int numVertices;
    private int[][] adjMatrix;
    private String[] cities;
    private static final int INF = Integer.MAX_VALUE;

    public Graph(int numVertices, String[] cities) {
        this.numVertices = numVertices;
        this.cities = cities;
        adjMatrix = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            Arrays.fill(adjMatrix[i], INF);
            adjMatrix[i][i] = 0;
        }
    }

    public void addEdge(String city1, String city2, int weight) {
        int index1 = getIndex(city1);
        int index2 = getIndex(city2);
        if (index1 != -1 && index2 != -1) {
            adjMatrix[index1][index2] = weight;
        }
    }

    public void removeEdge(String city1, String city2) {
        int index1 = getIndex(city1);
        int index2 = getIndex(city2);
        if (index1 != -1 && index2 != -1) {
            adjMatrix[index1][index2] = INF;
        }
    }

    private int getIndex(String city) {
        return Arrays.asList(cities).indexOf(city);
    }

    public int[][] floydWarshall() {
        int[][] dist = new int[numVertices][numVertices];
        int[][] next = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                dist[i][j] = adjMatrix[i][j];
                if (adjMatrix[i][j] != INF && i != j) {
                    next[i][j] = j;
                }
            }
        }

        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
        return dist;
    }

    public List<String> getPath(int start, int end, int[][] next) {
        List<String> path = new ArrayList<>();
        if (next[start][end] == 0) return path;
        path.add(cities[start]);
        while (start != end) {
            start = next[start][end];
            path.add(cities[start]);
        }
        return path;
    }

    public int getCenter() {
        int[][] dist = floydWarshall();
        int[] eccentricity = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (dist[i][j] != INF) {
                    eccentricity[i] = Math.max(eccentricity[i], dist[i][j]);
                }
            }
        }

        int minEccentricity = INF;
        int center = -1;
        for (int i = 0; i < numVertices; i++) {
            if (eccentricity[i] < minEccentricity) {
                minEccentricity = eccentricity[i];
                center = i;
            }
        }

        return center;
    }

    public void displayMatrix() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] == INF) {
                    System.out.print("INF ");
                } else {
                    System.out.print(adjMatrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try {
            Scanner fileScanner = new Scanner(new File("guategrafo.txt"));
            List<String> cityList = new ArrayList<>();
            List<String[]> edgeList = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String[] line = fileScanner.nextLine().split(" ");
                cityList.add(line[0]);
                cityList.add(line[1]);
                edgeList.add(line);
            }

            Set<String> citySet = new HashSet<>(cityList);
            String[] cities = citySet.toArray(new String[0]);

            Graph graph = new Graph(cities.length, cities);
            for (String[] edge : edgeList) {
                graph.addEdge(edge[0], edge[1], Integer.parseInt(edge[2]));
            }

            graph.displayMatrix();

            Scanner inputScanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Obtener ruta más corta");
                System.out.println("2. Mostrar centro del grafo");
                System.out.println("3. Modificar grafo");
                System.out.println("4. Salir");
                int option = inputScanner.nextInt();
                inputScanner.nextLine();

                if (option == 1) {
                    System.out.println("Ingrese la ciudad origen:");
                    String startCity = inputScanner.nextLine();
                    System.out.println("Ingrese la ciudad destino:");
                    String endCity = inputScanner.nextLine();

                    int startIndex = Arrays.asList(cities).indexOf(startCity);
                    int endIndex = Arrays.asList(cities).indexOf(endCity);

                    int[][] next = new int[graph.numVertices][graph.numVertices];
                    int[][] dist = graph.floydWarshall();

                    List<String> path = graph.getPath(startIndex, endIndex, next);
                    System.out.println("Distancia más corta: " + dist[startIndex][endIndex]);
                    System.out.println("Ruta: " + String.join(" -> ", path));

                } else if (option == 2) {
                    int centerIndex = graph.getCenter();
                    System.out.println("Centro del grafo: " + cities[centerIndex]);

                } else if (option == 3) {
                    System.out.println("1. Interrupción de tráfico");
                    System.out.println("2. Establecer conexión");
                    int modOption = inputScanner.nextInt();
                    inputScanner.nextLine();

                    if (modOption == 1) {
                        System.out.println("Ingrese la ciudad1:");
                        String city1 = inputScanner.nextLine();
                        System.out.println("Ingrese la ciudad2:");
                        String city2 = inputScanner.nextLine();
                        graph.removeEdge(city1, city2);

                    } else if (modOption == 2) {
                        System.out.println("Ingrese la ciudad1:");
                        String city1 = inputScanner.nextLine();
                        System.out.println("Ingrese la ciudad2:");
                        String city2 = inputScanner.nextLine();
                        System.out.println("Ingrese la distancia en KM:");
                        int distance = inputScanner.nextInt();
                        graph.addEdge(city1, city2, distance);
                    }
                    graph.displayMatrix();

                } else if (option == 4) {
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
