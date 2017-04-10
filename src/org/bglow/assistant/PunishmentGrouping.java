package org.bglow.assistant;

import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by kiko on 3/10/2017.
 */

public class PunishmentGrouping {
    private Node[] nodes;
    HashSet<Grouping> groupings;

    public PunishmentGrouping(List<Sentence> sentences) {
        this.nodes = new Node[sentences.size()];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(sentences.get(i));
        }
    }


    boolean next(int[] dist) {
        int max = dist.length;
        for (int i = dist.length - 1; i >= 0; i--) {
            dist[i]++;
            if (dist[i] > max) {
                dist[i] = 0;
            } else
                return true;
        }

        return false;
    }

    boolean check(int[] dist) {
        for (int i = 0; i < nodes.length; i++) {
            for (int j = i + 1; j < nodes.length; j++) {
                if (dist[i] == dist[j] && !nodes[i].isConnectedTo(nodes[j]))
                    return false;
            }
        }

        return true;
    }

    public void generate() {
        int[] dist = new int[nodes.length];
        groupings = new HashSet<>();
        do {
            Grouping grouping = new Grouping(dist, nodes);
            boolean valid = check(dist);
            if (!valid) continue;
            if (groupings.contains(grouping)) continue;
            if (valid) groupings.add(grouping);
        } while (next(dist));
    }

    private static class Node {
        Sentence sentence;

        Node(Sentence sentence) {
            this.sentence = sentence;
        }

        boolean isConnectedTo(Node other) {
            return this == other
                    || sentence.getActDate().compareTo(other.sentence.getActDate()) > 0 && sentence.getActDate().compareTo(other.sentence.getEffectDate()) < 0
                    || other.sentence.getActDate().compareTo(sentence.getActDate()) > 0 && other.sentence.getActDate().compareTo(sentence.getEffectDate()) < 0;
        }
    }

    public class Grouping {
        ArrayList<ArrayList<Sentence>> groups = new ArrayList<>();
        private final Node[] nodes;
        private int[] n;

        public Grouping(int[] dist, Node[] nodes) {
            this.nodes = nodes;
            int[] map = new int[dist.length + 1];
            int id = 1;
            n = new int[dist.length];
            for (int i = 0; i < dist.length; i++) {
                int x = dist[i];
                if (map[x] == 0) {
                    map[x] = id++;
                    groups.add(new ArrayList<>());
                }

                x = map[x];
                n[i] = x;
                groups.get(x - 1).add(nodes[i].sentence);
            }
        }

        public Sentence get(int idx) {
            return nodes[idx].sentence;
        }

        public void forEach(BiConsumer<Integer, Sentence> consumer) {
            for (int i = 0; i < n.length; i++) {
                consumer.accept(n[i], nodes[i].sentence);
            }
        }

        void print() {
            for (int i : n) {
                System.out.printf("%d", i);
            }
            System.out.println();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Grouping) {
                Grouping other = (Grouping) obj;
                if (other.n.length != n.length) return false;
                for (int i = 0; i < n.length; i++) {
                    if (n[i] != other.n[i]) return false;
                }
                return true;
            } else return super.equals(obj);
        }

        @Override
        public int hashCode() {
            int sum = 0;
            for (int i = 0; i < n.length; i++) {
                sum += n[i];
            }
            return sum;
        }
    }
}
