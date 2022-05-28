package factory.map;

import factory.vectors.Point;
import factory.vectors.Vector;
import factory.vectors.VectorPicture;
import io.args.EditArgs;

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
    private Vector[][] buildLinks(LinkedList<Cluster>clusters){
        Vector[][] orderedclusters =new Vector[clusters.size()][];
        LinkedList<Vector[]>orderedList=new LinkedList<>();
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
            LinkedList<Branch> branches=buildTree(links);
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
            Vector orderedVectors[]=new Vector[pth.length];
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
                    orderedVectors[i+1]=new Vector(ps,pe,true);
                }else{
                    orderedVectors[i+1]=new Vector(ps,pe,false);
                }
            }
            orderedList.add(orderedVectors);
        }
        //now we have orderedList with clusters of ordered vectors in it, we need to sort it, to minimalize our path
        //let's create another cluster of vectors, which represents end and start of each cluster and create a minimal path
        for (int i = 0; i < orderedList.size(); i++) {
            orderedclusters[i]=orderedList.get(i);
            if(i==0){
                orderedclusters[i][0]=new Vector(new Point(0,0,0,0),new Point(0,0,0,0),false);
            }else{
                orderedclusters[i][0]=new Vector(orderedclusters[i-1][orderedclusters[i-1].length-1].end, orderedclusters[i][1].start,false);
            }
        }
        sortClusters(orderedList);
        return orderedclusters;
    }

    private Vector[][] sortClusters(LinkedList<Vector[]> orderedList) {
        LinkedList<Vector> vectors=new LinkedList<>();
        for (Vector[] v:orderedList) {
            vectors.add(new Vector(v[1].start,v[v.length-1].end,true));
        }
        //created all vectors representing clusters
        LinkedList<Branch> branches= new LinkedList<>();
        boolean visited[]=new boolean[vectors.size()*2];
        int vertex=0;
        visited[vertex]=true;
        while(hasUnvisited(visited)){
            if(vertex%2==0&&!visited[vertex+1]||vertex%2!=0&&!visited[vertex-1]){//need to visit second point of vector
                branches.add(new Branch(vertex, vertex%2==0?vertex+1:vertex-1)); //add to list of branches in tree
                visited[vertex%2==0?vertex+1:vertex-1]=true; //visit next point
                vertex=vertex%2==0?vertex+1:vertex-1; //change point
            }else{
                int minBranch=1_000_000;
                int imin=-1;
                int jmin=-1;
                for (int i = 0; i < visited.length; i++) {//check every visited point for minimal branches
                    if(visited[i]){
                        for (int j = 0; j < visited.length; j++) { //find minimal branch
                            if(!visited[j]){
                                Point s;
                                Point e;
                                if(i%2==0){
                                    if(j%2==0){
                                        s=vectors.get(i/2).start;
                                        e=vectors.get(j/2).start;
                                    }else{
                                        s=vectors.get(i/2).start;
                                        e=vectors.get(j/2).end;
                                    }
                                }else{
                                    if(j%2==0){
                                        s=vectors.get(i/2).end;
                                        e=vectors.get(j/2).start;
                                    }else{
                                        s=vectors.get(i/2).start;
                                        e=vectors.get(j/2).end;
                                    }
                                }
                                if(distance(s,e)<minBranch){
                                    imin=i;
                                    jmin=j;
                                    minBranch=distance(s,e);
                                }
                            }
                        }
                    }
                }
                visited[jmin]=true; //visit point j
                branches.add(new Branch(imin, jmin)); //add branch from imin to jmin
                vertex=jmin; //change point
            }
        }
        //now we have list branches with la-la-la
        int startI=-1; //starting point (has only 1 branch)
        for (int i = 0; i < visited.length; i++) {
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
        used=new boolean[visited.length];
        path="";
        walk(branches,  startI);
        //System.out.println(path);
        //now we have path between clusters, let's try it!
        path=path.trim();

        String pth[]=path.split(" ");
            System.out.println(path);
            for (String str :
                    pth) {
                System.out.print(str+" ");
            }
        Vector orderedVectors[]=new Vector[pth.length];
        for (int i =0;i<pth.length-1;i++){
            int s=Integer.parseInt(pth[i]);
            int e=Integer.parseInt(pth[i+1]);
            Vector vs=vectors.get(s/2);
            Vector ve=vectors.get(e/2);
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
                orderedVectors[i+1]=new Vector(ps,pe,true);
            }else{
                orderedVectors[i+1]=new Vector(ps,pe,false);
            }
        }

        return null;
    }

    private boolean used[];
    private String path;
    private void walk(LinkedList<Branch> branches, int vertex){
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
    private LinkedList<Branch> buildTree(int links[][][]){
        /*for (int i = 0; i < links.length; i++) {
            for (int j = 0; j < links.length; j++) {
                System.out.print(links[i][j][0]+"\t");
            }
            System.out.println();
        }
        System.out.println();*/
        LinkedList<Branch> branches= new LinkedList<>();
        boolean visited[]=new boolean[links.length];
        int vertex=0;
        visited[vertex]=true;
        while(hasUnvisited(visited)){
            if(vertex%2==0&&!visited[vertex+1]||vertex%2!=0&&!visited[vertex-1]){//need to visit second point of vector
                branches.add(new Branch(vertex, vertex%2==0?vertex+1:vertex-1)); //add to list of branches in tree
                visited[vertex%2==0?vertex+1:vertex-1]=true; //visit next point
                vertex=vertex%2==0?vertex+1:vertex-1; //change point
            }else{
                int minBranch=1_000_000;
                int imin=-1;
                int jmin=-1;
                for (int i = 0; i < visited.length; i++) {//check every visited point for minimal branches
                    if(visited[i]){
                        for (int j = 0; j < links.length; j++) { //find minimal branch
                            if(!visited[j]&&links[i][j][0]!=-1){
                                if(links[i][j][0]<minBranch){ //find minimal branch from visited i to unvisited j
                                    minBranch=links[i][j][0];
                                    imin=i;
                                    jmin=j;
                                }
                            }
                        }
                    }
                }
                visited[jmin]=true; //visit point j
                branches.add(new Branch(imin, jmin)); //add branch from imin to jmin
                vertex=jmin; //change point
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
