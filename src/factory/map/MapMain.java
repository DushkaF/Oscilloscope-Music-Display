package factory.map;

import factory.vectors.Point;
import factory.vectors.Vector;
import factory.vectors.VectorPicture;
import io.args.EditArgs;

import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MapMain {
    private int radius;
    public LinkedList<Vector> getMap(VectorPicture vectorPicture, Vector[] vectors, EditArgs editArgs) {
        radius=editArgs.radius;
        LinkedList<Cluster> clusters=getClusters(vectors);
        Vector[][] orderedClusters=buildLinks(clusters);

        LinkedList<Vector> orderedVectors=new LinkedList<>();
        for (int i = 0; i < orderedClusters.length; i++) {
            for (int j = 0; j < orderedClusters[i].length; j++) {
                orderedVectors.add(orderedClusters[i][j]);
            }
        }
        vectorPicture.clusters=clusters;
        vectorPicture.orderedClusters=orderedClusters;
        return orderedVectors;
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

    private Vector[][] buildLinks(LinkedList<Cluster>clusters){
        Vector[][] orderedclusters =new Vector[clusters.size()][];
        LinkedList<Vector[]>pathList=new LinkedList<>();
        for (Cluster c :clusters) {
            int size=2*c.size();
            //System.out.println(size);
            int links[][][]=new  int[size][size][2]; // [i cell][jcell][visible (0 - not visible, 1 - visible)]
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    links[i][j][0]=-1;
                }
            }
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Vector v=c.get(i/2);
                    Vector v2=c.get(j/2);
                    int dist=-1;
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
                    if(dist<=radius||i/2==j/2)links[i][j][0]=dist;
                    if(i/2==j/2)links[i][j][1]=1;
                    // System.out.print(links[i][j][0]+"\t");
                }
                //  System.out.println();
            }
            //now we have in links[i][j][k] distance between dots i and j, and visible they are or not (k==0?)
            //lets use prima to build tree
            ArrayList<Branch> branches=buildTree(links);
            //now we have minimal tree and we shall create path
            int startI=-1; //starting point (has only 1 branch)
            for (int i = 0; i < size; i++) {
                int numOfbr=0;
                for (Branch b : branches) {
                    if(b.I==i||b.J==i)numOfbr++;
                    if(numOfbr>1)break;
                }
                if (numOfbr==1){
                    startI=i; break;
                }
            }
            //now let's walk recursevely
            used=new boolean[size];
            path="";
            walk(branches,  startI);
            //System.out.println(path);
            //now we have path, so we can make ordered list of vectors in cluster;
            path=path.trim();
            String pth[]=path.split(" ");
            /*System.out.println(path);
            for (String str :
                    pth) {
                System.out.print(str+" ");
            }*/
            Vector vectorsPath[]=new Vector[pth.length];
            for (int i =0;i<pth.length-1;i++){
                int s=Integer.parseInt(pth[i]);
                int e=Integer.parseInt(pth[i+1]);
                Vector vs=c.vectors.get(s/2);
                Vector ve=c.vectors.get(e/2);
                Point ps;
                Point pe;
                if(s%2==0){
                    ps=vs.start;
                }else{
                    ps=vs.end;
                }
                if(e%2==0){
                    pe=ve.start;
                }else{
                    pe=ve.end;
                }
                if(s%2==0&&s+1==e||s%2==1&&s-1==e||e%2==0&&e+1==s||e%2==1&&e-1==s){ //if s and e are in one cell (one vector)
                    vectorsPath[i+1]=new Vector(ps,pe,true);
                }else{
                    vectorsPath[i+1]=new Vector(ps,pe,false);
                }
            }
            pathList.add(vectorsPath);
        }
        //now we have orderedList with clusters of ordered vectors in it, we need to sort it, to minimalize our path
        //let's create another cluster of vectors, which represents end and start of each cluster and create a minimal path
        /*for (int i = 0; i < pathList.size(); i++) {
            orderedclusters[i]=pathList.get(i);
            if(i==0){
                orderedclusters[i][0]=new Vector(new Point(0,0,0,0),new Point(0,0,0,0),false);
            }else{
                orderedclusters[i][0]=new Vector(orderedclusters[i-1][orderedclusters[i-1].length-1].end, orderedclusters[i][1].start,false);
            }
        }*/ //linking clusters without sorting
        orderedclusters=sortClusters(pathList);
        return orderedclusters;
    }

    private Vector[][] sortClusters(LinkedList<Vector[]> pathList) {
       /* for (Vector[] arr:pathList) {
            for (Vector v:arr ) {
                System.out.print(v+" ");
            }
            System.out.println();
        }*/
        boolean used[] = new boolean[pathList.size()] ;
        int order[]=new int[pathList.size()];
        boolean reverse[]=new boolean[pathList.size()];
        int length=order.length;
        Point end=pathList.get(0)[pathList.get(0).length-1].end;
        int minDist=100000000;
        int nextNearest=0;
        boolean isReversed=false;
        int count=0;
        used[0]=true;
        order[count++]=0;
        while (hasUnvisited(used)){
            minDist=100000000;
            nextNearest=0;
            isReversed=false;
            for (int i = 0; i < length; i++) {
                //System.out.println(minDist);
                if(!used[i]){
                    if(distance(end, pathList.get(i)[1].start)<minDist){
                        minDist=distance(end, pathList.get(i)[1].start);
                        nextNearest=i;
                        isReversed=false;
                    }
                    if(distance(end, pathList.get(i)[1].end)<minDist){
                        minDist=distance(end, pathList.get(i)[1].end);
                        nextNearest=i;
                        isReversed=true;
                    }
                }
            }
            if(!reverse[nextNearest]){
            end=pathList.get(nextNearest)[pathList.get(nextNearest).length-1].end;}
            else{
            end=pathList.get(nextNearest)[1].start;
            }
            used[nextNearest]=true;
            reverse[nextNearest]=isReversed;
            order[count++]=nextNearest;
            /*System.out.println(nextNearest+" "+end+" "+minDist+" "+isReversed);
            for (boolean b :used) {
                System.out.print(b+" ");
            }
            System.out.println();
            for (boolean b :reverse) {
                System.out.print(b+" ");
            }
            System.out.println();*/
        }
        //we have order[] with order of clusters, now we need to link them
        Vector[][] sortedClusters=new Vector[length][];
        for (int i = 0; i < length; i++) {
            int clSize=pathList.get(order[i]).length;

            for (int j = 0; j < clSize; j++) {
               // System.out.println(clSize+" "+j+" "+length+" "+ sortedClusters.length+" "+sortedClusters[i].length);
                if(reverse[i]){
                    sortedClusters[i]=new Vector[clSize];
                    Vector[] v=pathList.get(order[i]);
                    for (int k = 0; k < clSize; k++) {
                        if(k!=0){
                            sortedClusters[i][k]=v[clSize-k];
                        }
                    }
                }else{
                    sortedClusters[i]=pathList.get(order[i]);
                }
                if(j!=0&&reverse[i])sortedClusters[i][j].reverse();
            }
            if(i==0){
                sortedClusters[i][0]=new Vector(new Point(0,0,0,0), sortedClusters[i][1].start, false);
                //create linking vector from 0 0
            }else{
                sortedClusters[i][0]=new Vector(sortedClusters[i-1][sortedClusters[i-1].length-1].end, sortedClusters[i][1].start, false);
                //create linking vector from prev cluster
            }
            //sortedClusters[i][0]=new Vector(new Point(0,0,0,0),new Point(0,0,0,0),false);
        }
        return sortedClusters;
    }

    private boolean used[];
    private String path;
    private void walk(ArrayList<Branch> branches, int vertex){
        path+=" "+vertex;
        used[vertex]=true;
        for (Branch b : branches) {
            if(b.I==vertex){
                if (!used[b.J]){
                    walk(branches, b.J);
                    if(hasUnvisited(used)){
                        path+=" "+vertex;
                    }
                }
            }
            if(b.J==vertex){
                if (!used[b.I]){
                    walk(branches, b.I);
                    if(hasUnvisited(used)){
                        path+=" "+vertex;
                    }
                }
            }
        }
    }

    private ArrayList<Branch> buildTree(int links[][][]){
        ArrayList<Branch> branches = new ArrayList<>(); // лучше использовать arraylist почти всегда
        int n = links.length;
        boolean visited[] = new boolean[links.length];
        int vertex = 0;
        visited[vertex] = true;

        while (hasUnvisited(visited)){ // можно заменить на цикл for, в дереве ровно 2n - 1 ребро, если 2n вершин
            int new_vertex = vertex % 2 == 0 ? vertex + 1 : vertex - 1;
            // сразу считаем новую вершину, чтобы не дублировать стремное выражение
            if (!visited[new_vertex]) { // need to visit second point of vector
                branches.add(new Branch(vertex, new_vertex)); // add to list of branches in tree
                visited[new_vertex] = true; // visit next point
                vertex = new_vertex;
            } else {
                int minBranch = 1_000_000;
                int imin = -1;
                int jmin = -1;
                for (int i = 0; i < visited.length; i++) { //check every visited point for minimal branches
                    if (!visited[i]) continue; // заменил на отсечение по обратному условию, чтобы не было лишней вложенности кода
                    for (int j = 0; j < links.length; j++) { // find minimal branch
                        if (visited[j] || links[i][j][0] == -1) continue; // то же самое
                        if (links[i][j][0] < minBranch) { // find minimal branch from visited i to unvisited j
                            minBranch = links[i][j][0];
                            imin = i;
                            jmin = j;
                        }
                    }
                }
                visited[jmin] = true; // visit point j
                branches.add(new Branch(imin, jmin)); // add branch from imin to jmin
                vertex = jmin; // change point
            }
        }
        return branches;
    }

    static class Branch{
        int I;
        int J;
        public Branch(int i,int j){
            I=i;
            J=j;
        }
        @Override
        public String toString() {
            return "Branch{" +
                    "I=" + I +
                    ", J=" + J +
                    '}';
        }
    }

    private boolean hasUnvisited(boolean[] visited) {
        for (int i = 0; i < visited.length; i++) {
            if(!visited[i])return true;
        }
        return false;
    }

    private int distance(Point s, Point e){
        return (int) sqrt(pow(s.x-e.x,2)+pow(s.y-e.y,2));
    }

}
