package factory.map;

import factory.vectors.Point;
import factory.vectors.Vector;
import factory.vectors.VectorPicture;
import io.args.EditArgs;

import java.util.Arrays;
import java.util.LinkedList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MapMain {
    private int radius;
    public LinkedList<Vector> getMap(VectorPicture vectorPicture, Vector[] vectors, EditArgs editArgs) {
        radius=editArgs.radius;
        LinkedList<Cluster> clusters=getClusters(vectors);
        buildLinks(clusters);
        vectorPicture.clusters=clusters;
        return null;
    }
    private LinkedList<Cluster> getClusters(Vector[] vectors){
        LinkedList<Cluster> clusters=new LinkedList<>();
        for (int i = 0; i < vectors.length; i++) {
            if(!vectors[i].used){
                Cluster cluster=new Cluster();
                cluster.add(vectors[i]);
                growCluster(vectors[i],cluster,vectors);
                clusters.add(cluster);
            }
        }
        return clusters;
    }
    private void buildLinks(LinkedList<Cluster>clusters){
        for (Cluster c :clusters) {
            int size=2*c.size();
            //System.out.println(size);
            int links[][][]=new  int[size][size][2]; // [i cell][jcell][visible (0 - not visible, 1 - visible)]
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    links[i][j][0]=1_000_000;
                }
            }
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Vector v=c.get(i/2);
                    Vector v2=c.get(j/2);
                    int dist=1_000_000;
                    if (i == j){
                        dist = 0;
                    }else{
                        if(i%2==0){// v(i).start
                            if(j%2==0){//v(j).start
                                dist=distance(v.start, v2.start);
                            }else{//v(j).end
                                dist=distance(v.start, v2.end);
                            }
                        }else{// v(i).end
                            if(j%2==0){//v(j).start
                                dist=distance(v.end, v2.start);
                            }else{//v(j).end
                                dist=distance(v.end, v2.end);
                            }
                        }
                    }
                    if(dist<radius||i/2==j/2)links[i][j][0]=dist;
                    if(i/2==j/2)links[i][j][1]=1;
                    // System.out.print(links[i][j][0]+"\t");
                }
                //  System.out.println();
            }
            //now we have in links[i][j][k] distance between dots i and j, and visible they are or not (k==0?)
            //let's build tree using Prima's alg.
            buildTree(links, size);
        }
    }
    private void buildTree(int G[][][], int V) {

        int INF = 9999999;

        int no_edge; // number of edge

        // create a array to track selected vertex
        // selected will become true otherwise false
        boolean[] selected = new boolean[V];

        // set selected false initially
        Arrays.fill(selected, false);

        // set number of edge to 0
        no_edge = 0;

        // the number of egde in minimum spanning tree will be
        // always less than (V -1), where V is number of vertices in
        // graph

        // choose 0th vertex and make it true
        selected[0] = true;

        // print for edge and weight
        System.out.println("Edge : Weight");

        while (no_edge < V - 1) {
            // For every vertex in the set S, find the all adjacent vertices
            // , calculate the distance from the vertex selected at step 1.
            // if the vertex is already in the set S, discard it otherwise
            // choose another vertex nearest to selected vertex at step 1.

            int min = INF;
            int x = 0; // row number
            int y = 0; // col number

            for (int i = 0; i < V; i++) {
                if (selected[i] == true) {
                    for (int j = 0; j < V; j++) {
                        // not in selected and there is an edge
                        if (!selected[j] && G[i][j][0] != 0) {
                            if (min > G[i][j][0]) {
                                min = G[i][j][0];
                                x = i;
                                y = j;
                            }
                        }
                    }
                }
            }
            System.out.println(x + " - " + y + " :  " + G[x][y][0]);
            selected[y] = true;
            no_edge++;
        }
    }
    private int distance(Point s, Point e){
        return (int) sqrt(pow(s.x-e.x,2)+pow(s.y-e.y,2));
    }
    private void growCluster(Vector vector, Cluster cluster, Vector[] vectors) {
        vector.used=true;
        Point start=vector.start;
        Point end=vector.end;
        for (int i = 0; i < vectors.length; i++) {
            if(!vectors[i].used){
                Point start2=vectors[i].start;
                Point end2=vectors[i].end;
                if(pow(start.x-end2.x,2)+pow(start.y-end2.y,2)<=pow(radius,2)){
                    cluster.add(vectors[i]);
                    growCluster(vectors[i], cluster, vectors);
                }
                if(pow(end.x-start2.x,2)+pow(end.y-start2.y,2)<=pow(radius,2)){
                    cluster.add(vectors[i]);
                    growCluster(vectors[i], cluster, vectors);
                }
                if(pow(end.x-end2.x,2)+pow(end.y-end2.y,2)<=pow(radius,2)){
                    cluster.add(vectors[i]);
                    growCluster(vectors[i], cluster, vectors);
                }
                if(pow(start.x-start2.x,2)+pow(start.y-start2.y,2)<=pow(radius,2)){
                    cluster.add(vectors[i]);
                    growCluster(vectors[i], cluster, vectors);
                }
            }
        }
    }
}
