package org.bglow.assistant;

import java.time.Period;
import java.util.*;

/**
 * Created by kiko on 3/10/2017.
 */

public class PunishmentGrouping {
    private ArrayList<Node> nodes;

    static class Traverser {
        ArrayList<Node> graph;
        HashSet<Node> used;
        ArrayList<HashSet<Node>> groups;
        ArrayList<Node> conflicts;

        int max = 0;

        int[] dist;

        public Traverser(Collection<Node> graph) {
            this.graph = new ArrayList<>(graph);
        }

        void traverse() {
            used = new HashSet<>();
            groups = new ArrayList<>();
            conflicts = new ArrayList<>();
            for (Node n : graph) {
                if (!n.locked) {
                    HashSet<Node> group = new HashSet<>();
                    used = new HashSet<>();
                    group.add(n);
                    if (dsf(n, group)) {
                        groups.add(group);
                        group.forEach(node -> node.locked = true);

                    }
                }
            }
            for (Node n : graph) {
                if (!n.locked)
                    conflicts.add(n);
            }

            dist = new int[conflicts.size()];
            max = conflicts.size() + groups.size();

            prevs.add(generateVariantKey());

            System.out.println();
        }

        boolean dsf(Node u, HashSet<Node> group) {
            if (used.contains(u)) return true;
            used.add(u);
//            System.out.printf("%s ", u.sentence.getId());
            for (Node v : u.nbrs) {
                if (used.contains(v)) continue;
                if (!tryAdd(group, v))
                    return false;
                boolean ok = dsf(v, group);
                if (!ok)
                    return false;
            }

            return true;
        }

        ArrayList<HashSet<Node>> check() {
            ArrayList<HashSet<Node>> var = new ArrayList<>();
            for (HashSet<Node> group : groups) {
                var.add(new HashSet<>(group));
            }

            while (var.size() < max) {
                var.add(new HashSet<>());
            }

            for (int i = 0; i < conflicts.size(); i++) {
                int assigned = dist[i];
                if (!tryAdd(var.get(assigned), conflicts.get(i)))
                    return null;
            }

            for (int i = 0; i < conflicts.size(); i++) {
                HashSet<Node> group = var.get(dist[i]);
                Node c = conflicts.get(i);

                if (group.size() == 1) {
                    for (HashSet<Node> otherGroup : var) {
                        if (group != otherGroup && otherGroup.size() > 0 && tryAdd(otherGroup, c))
                            return null;
                    }
                }
            }

            for (int i = 0; i < var.size(); i++) {
                if (var.get(i).isEmpty())
                    var.remove(i--);
            }

            return var;
        }

        private boolean tryAdd(HashSet<Node> group, Node node) {
            for (Node x : group) {
                if (!node.isConnectedTo(x)) {
                    return false;
                }
            }
            group.add(node);
            return true;
        }

        HashSet<String> prevs = new HashSet<>();

        boolean next2() {
            if (next() == false) return false;
            String key = generateVariantKey();
            while (prevs.contains(key)) {
                if (next() == false) return false;
                key = generateVariantKey();
            }

            prevs.add(key);

            return true;
        }

        String generateVariantKey() {
            int[] map = new int[max];
            for (int i = 0; i < map.length; i++) map[i] = i < groups.size() ? i : -1;
            int id = groups.size();
            int[] n = new int[dist.length];
            for (int i = 0; i < dist.length; i++) {
                int x = dist[i];
                if (map[x] < 0) {
                    map[x] = id++;
                }
                n[i] = map[x];
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int x : n) stringBuilder.append(x + ";");
            String hashable = stringBuilder.toString();
            return hashable;
        }

        boolean next() {
            for (int i = dist.length - 1; i >= 0; i--) {
                dist[i]++;
                if (dist[i] >= max) {
                    dist[i] = 0;
                } else
                    return true;
            }

            return false;
        }
    }


    public PunishmentGrouping(List<Sentence> sentences) {
        this.nodes = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++) {
            nodes.add(new Node(sentences.get(i)));
        }

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (nodes.get(i).isConnectedTo(nodes.get(j))) {
                    nodes.get(i).nbrs.add(nodes.get(j));
                    nodes.get(j).nbrs.add(nodes.get(i));
                }
            }
        }
    }


    public List<Grouping> generate() {
        ArrayList<Grouping> groupings = new ArrayList<>();
        Traverser t = new Traverser(nodes);
        t.traverse();
        do {
            ArrayList<HashSet<Node>> solution = t.check();
            if(solution != null) {
                groupings.add(new Grouping(solution));
            }

        } while (t.next2());

        groupings.sort((o1, o2) -> o1.compareTo(o2));

        return groupings;
    }

    private static class Node {
        Sentence sentence;
        boolean locked = false;

        Node(Sentence sentence) {
            this.sentence = sentence;
        }

        boolean isConnectedTo(Node other) {
            return this == other
                    || sentence.getActDate().compareTo(other.sentence.getActDate()) > 0 && sentence.getActDate().compareTo(other.sentence.getEffectDate()) < 0
                    || other.sentence.getActDate().compareTo(sentence.getActDate()) > 0 && other.sentence.getActDate().compareTo(sentence.getEffectDate()) < 0;
        }

        public ArrayList<Node> nbrs = new ArrayList<>();
    }

    public class Grouping {
        ArrayList<ArrayList<Sentence>> groups = new ArrayList<>();
        Punishment jailTotal = new Punishment(Period.ofMonths(0), Punishment.Type.Jail);
        Punishment probationTotal = new Punishment();

        public Grouping(ArrayList<HashSet<Node>> solution) {
            for(HashSet<Node> set : solution) {
                ArrayList<Sentence> list = new ArrayList<>();
                Punishment punishment = null;
                for(Node n : set) {
                    list.add(n.sentence);
                    if(punishment == null)
                        punishment = n.sentence.getPunishment();
                    else
                        punishment = Punishment.max(punishment, n.sentence.getPunishment());
                }
                groups.add(list);
                if(punishment != null) {
                    Punishment total = punishment.getType() == jailTotal.getType() ? jailTotal : probationTotal;
                    total.add(punishment);
                }
            }
        }

        public int compareTo(Grouping o2) {
            int jc = jailTotal.compareTo(o2.jailTotal);
            int pc = probationTotal.compareTo(o2.probationTotal);
            return jc == 0 ? pc : jc;
        }
    }
}
