package me.roblabla;

/**
 * File: AStar.java
 * Author: Brian Borowski
 * Date created: December 26, 2010
 * Date last modified: January 12, 2012
 */
import java.util.HashMap;

public final class AStar extends Algorithm {
    private HashMap<Long, AStarNode> openMap, closedMap;

    void solvePuzzle(final long currentState, final int numOfThreads) {
        initialMovesEstimate = NOT_APPLICABLE;

        AStarNode currentConfig = new AStarNode(currentState);
        openMap = new HashMap<Long, AStarNode>();
        closedMap = new HashMap<Long, AStarNode>();
        final FibonacciHeap openHeap = new FibonacciHeap();
        int previous = 0;

        while (running) {
            ++numberVisited;
            movesRequired = currentConfig.g();
            if (movesRequired > previous) {
                if (PuzzleConfiguration.isVerbose()) {
                    if (movesRequired != 1) {
                        System.out.print(
                            "\nSearching paths of length " + movesRequired +
                            " moves...");
                    } else {
                        System.out.print(
                                "\nSearching paths of length 1 move...");
                    }
                }
                previous = movesRequired;
            }
            if (currentConfig.isGoalState()) {
                if (PuzzleConfiguration.isVerbose()) {
                    System.out.println("done.");
                }
                shortestPath = currentConfig.getPath();
                solved = true;
                return;
            }

            closedMap.put(currentConfig.boardConfig, currentConfig);
            for (int i = 3; i >= 0; --i) {
                AStarNode successor;
                switch (i) {
                    case 3:
                        successor = currentConfig.moveLeft();
                        break;
                    case 2:
                        successor = currentConfig.moveRight();
                        break;
                    case 1:
                        successor = currentConfig.moveUp();
                        break;
                    default:
                        successor = currentConfig.moveDown();
                }

                if (successor != null) {
                    final AStarNode
                              openNode = openMap.get(successor.boardConfig),
                              closedNode = closedMap.get(successor.boardConfig);

                    if (closedNode == null || successor.cost < closedNode.cost) {
                        openHeap.insert(successor);
                        openMap.put(successor.boardConfig, successor);
                        ++numberExpanded;
                    } else if (openNode != null && successor.cost < openNode.cost) {
                        openHeap.decreaseKey(openNode, successor.cost);
                        openMap.put(successor.boardConfig, successor);
                    }
                }
            }

            currentConfig = openHeap.removeMin();
        }
    }

    public void cleanup() {
        openMap = null;
        closedMap = null;
    }
}
