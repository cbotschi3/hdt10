import java.io.File;
import java.util.*;

public class Main {
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

                    int[][] next = new int[graph.getNumVertices()][graph.getNumVertices()];
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
