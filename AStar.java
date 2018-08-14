import java.util.*;

public class AStar<E> {

    /**
     * Constructor for new AStar object
     */

    public AStar() {

    }

    private HashMap<E, Integer> gScore;
    private HashMap<E, Integer> fScore;

    /**
     * Performs AStar search given the input graph, start and goal nodes and heuristic
     * @param graph Graph object on which search is being performed
     * @param start Start node
     * @param goal Goal node
     * @param heuristic Heuristic of graph
     * @param greedy Determines whether or not search is greedy
     * @return The path taken from start node to goal node
     * @throws NoSuchFieldException If a vertex is not found in the graph
     */

    public List<E> search(final GraphImpl<E> graph, final E start, final E goal, final Map<E, Integer> heuristic, boolean greedy) throws NoSuchFieldException {
        E currentNode = start;
        gScore = new HashMap<>();
        fScore = new HashMap<>();

        PriorityQueue<E> openQueue = new PriorityQueue<>(new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                if (greedy) {
                    return heuristic.get(o1) - heuristic.get(o2);
                }
                return fScore.get(o1) - fScore.get(o2);
            }
        });

        gScore.put(currentNode, 0);

        fScore.put(currentNode, heuristic.get(currentNode));

        for (E node : graph.getVertices()) {
            fScore.putIfAbsent(node, heuristic.get(node));
        }

        openQueue.add(currentNode);
        Set<E> closedSet = new HashSet<>();
        HashMap<E, E> cameFrom = new HashMap<>();

        while (openQueue.peek() != null) {
            currentNode = openQueue.poll();

            if (currentNode.equals(goal)) {
                return getPath(cameFrom, currentNode);
            }

            closedSet.add(currentNode);
            for (E neighbour : graph.neighbours(currentNode)) {

                if (closedSet.contains(neighbour)) {
                    continue;
                }

                int weight = gScore.get(currentNode) + graph.getWeight(currentNode, neighbour);

                if (!openQueue.contains(neighbour)) {
                    openQueue.add(neighbour);
                }

                if (gScore.containsKey(neighbour) && weight >= gScore.get(neighbour)) continue;

                cameFrom.put(neighbour, currentNode);
                gScore.put(neighbour, weight);
                fScore.put(neighbour, weight + heuristic.get(neighbour));
            }
        }
        return null;
    }

    /**
     * Gets the path formed by the search
     * @param cameFrom Map containing each node's predecessor
     * @param current Goal node of the search
     * @return Path taken by search
     */

    private List<E> getPath(HashMap<E, E> cameFrom, E current) {
        List<E> path = new ArrayList<>();
        while (cameFrom.get(current) != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

}
