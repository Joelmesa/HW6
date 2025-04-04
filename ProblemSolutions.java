
/******************************************************************
 *
 *   YOUR NAME / SECTION NUMBER
 *
 *   This java file contains the problem solutions for the methods lastBoulder,
 *   showDuplicates, and pair methods. You should utilize the Java Collection
 *   Framework for these methods.
 *
 ********************************************************************/

import java.util.*;

public class ProblemSolutions {

    public static int lastBoulder(int[] boulders) {
        java.util.PriorityQueue<Integer> maxHeap = new java.util.PriorityQueue<>(java.util.Collections.reverseOrder());
        for (int stone : boulders) {
            maxHeap.add(stone);
        }

        while (maxHeap.size() > 1) {
            int x = maxHeap.poll();
            int y = maxHeap.poll();
            if (x != y) {
                maxHeap.add(Math.abs(x - y));
            }
        }

        return maxHeap.isEmpty() ? 0 : maxHeap.poll();
    }

    public static ArrayList<String> showDuplicates(ArrayList<String> input) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String s : input) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }

        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                result.add(entry.getKey());
            }
        }

        Collections.sort(result);
        return result;
    }

    public static ArrayList<String> pair(int[] input, int k) {
        HashSet<Integer> seen = new HashSet<>();
        HashSet<String> pairSet = new HashSet<>();
        ArrayList<String> result = new ArrayList<>();

        for (int num : input) {
            int complement = k - num;
            if (seen.contains(complement)) {
                int a = Math.min(num, complement);
                int b = Math.max(num, complement);
                String pair = "(" + a + ", " + b + ")";
                if (!pairSet.contains(pair)) {
                    pairSet.add(pair);
                    result.add(pair);
                }
            }
            seen.add(num);
        }

        Collections.sort(result);
        return result;
    }
}
