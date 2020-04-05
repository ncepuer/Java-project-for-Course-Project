package Test;

/**********************主方法位于Facade类中*************************/

public class Edge { // 存储边
	int v; // 始点
	int w; // 终点
	int weight; // 边的权值

	public Edge(int v, int w, int weight) {
		this.v = v;
		this.w = w;
		this.weight = weight;
	}
	
	public Edge() {
		
	}
	
}
