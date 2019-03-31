# Maximum Flow Algorithms
Implementation of some classic algorithms solving the general maximum flow problem:
* *Ford-Fulkerson*
* *Edmonds-Karp*
* *Dinic*
* *Goldberg-Tarjan* (also known as *Preflow-Push* or *Push-Relabel*)

The algorithms are visualized for an auto-generated maximal-planar graph of any given
size (which is the number of vertices and the maximal arc capacity). User can choose between 
running the whole algorithm at once with a given delay between the intermediate steps and running 
one single step of the algorithm. The vertices and arcs are highlighted accordingly.

###### GUI showing a generated maximal-planar graph containing 55 vertices and a max arc capacity of 100:
![Main GUI](images/gui.png)

###### One intermediate step of Ford-Fulkerson with the flow augmenting path found by DFS highlighted:
<img src="https://raw.githubusercontent.com/tabneib/mxflwalgo/master/images/fordfulkerson-1step.png" 
data-canonical-src="https://raw.githubusercontent.com/tabneib/mxflwalgo/master/images/fordfulkerson-1step.png" width="600"/>

###### One intermediate step of Edmonds-Karp with the flow augmenting path found by BFS highlighted:
<img src="https://raw.githubusercontent.com/tabneib/mxflwalgo/master/images/edmondskarp-1step.png" 
data-canonical-src="https://raw.githubusercontent.com/tabneib/mxflwalgo/master/images/edmondskarp-1step.png" width="600"/>

###### One intermediate step of Dinic with the blocking flow highlighted:
<img src="https://raw.githubusercontent.com/tabneib/mxflwalgo/master/images/dinic-1step.png" 
data-canonical-src="https://raw.githubusercontent.com/tabneib/mxflwalgo/master/images/dinic-1step.png" width="600"/>
