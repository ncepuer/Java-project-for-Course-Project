package Test;

import java.util.*;

/**********************主方法位于Facade类中*************************/

public class MGraph {	//	邻接矩阵
	public static final int inf = 999; // 定义无穷大
	public String[] vex;
	/** 存储节点信息 */
	public int[][] arc;
	/** 存储边信息（邻接矩阵） */
	public int vexnum = 0;
	public int arcnum = 0;
	public int type = 0; // 图的类型：无向网=1，有向网=2
	Edge[] e=null;
	
	int n=0;
	String[] path;
	int[] visit;
	boolean[] dfs_visit;//顶点访问标记数组
	int[][] dfs_result;
	boolean[] bfs_visit;//顶点访问标记数组
	int[][] bfs_result;
	int[][] prim_result;
	int dfs_num1,dfs_num2,bfs_num1,bfs_num2;

	public MGraph() {}
	public MGraph(String[] vexname, Edge[] e, int type) { // 邻接矩阵的初始化
		this.vexnum = vexname.length;
		this.e=e;
		this.arcnum = e.length;
		this.type = type;
		bfs_visit=new boolean[vexnum];
		bfs_result=new int[vexnum][vexnum];
		dfs_visit=new boolean[vexnum];
		dfs_result=new int[vexnum][vexnum];
		prim_result=new int[vexnum-1][2];
		vex = new String[vexnum];
		arc = new int[vexnum][vexnum];
		path=new String[vexnum];
		visit=new int[vexnum];
		for (int i = 0; i < vexnum; i++) {
			vex[i] = vexname[i];
		}
		switch (type) {
		case 1: // 无向网
			for (int i = 0; i < vexnum; i++)
				for (int j = 0; j < vexnum; j++) {
					if (i == j)
						arc[i][j] = 0;
					else
						arc[i][j] = inf;
				}
			for (int i = 0; i < arcnum; i++) {
				arc[e[i].v][e[i].w] = e[i].weight;
				arc[e[i].w][e[i].v] = e[i].weight;
			}
			break;
		case 2: // 有向网
			for (int i = 0; i < vexnum; i++)
				for (int j = 0; j < vexnum; j++) {
					if (i == j)
						arc[i][j] = 0;
					else
						arc[i][j] = inf;
				}
			for (int i = 0; i < arcnum; i++) {
				arc[e[i].v][e[i].w] = e[i].weight;
			}
			break;
		}
	}
	
	public MGraph clone() {		//	生成邻接矩阵的副本
		MGraph mg=new MGraph(this.vex,this.e,this.type);
		return mg;
	}
	
	public boolean isNeighbor(int v,int w) {	//	判断两点间是否存在边
		if(this.arc[v][w]!=inf && this.arc[v][w]!=0)
			return true;
		else return false;
	}

	public static void output(MGraph G) { // 输出矩阵
		System.out.println("该图的邻接矩阵为：");
		for (int i = 0; i < G.vexnum; i++) {
			for (int j = 0; j < G.vexnum; j++)
				System.out.printf("%-3d  ", G.arc[i][j]);
			System.out.println();
		}
	}
	
	/******（YY）邻接矩阵深度遍历（连通非连通图）******
	 * 调用DFS（x），x对应：遍历起点的顶点标号（0开头）；
	 * G.dfs_result访问遍历序列结果
	 * 图像区附加访问标记数组，暂时想到label,两行对应顶点编号和访问情况
	 * 分步演示标记点,通信机制待添加wait()notify()
	 */
	
	void DFS(int x){
		dfs_num1=0;
		dfs_num2=0;
		for(int i=0;i<vexnum;i++)
			for(int j=0;j<vexnum;j++)
				dfs_result[i][j]=-1;
		for(int i=0;i<vexnum;i++)
			dfs_visit[i]=false;
		df_travel(x);
		dfs_num1++;
		for(int i=0;i<vexnum;i++)
		{
			if(!dfs_visit[i])//#框出顶点
			{
				dfs_num2=0;
				df_travel(i);
				dfs_num1++;
			}
		}
	}
	void df_travel(int v)
	{
//		System.out.print(vex[v]+"\t");
		dfs_result[dfs_num1][dfs_num2++]=v;//#图像区动画，例如颜色变化表示一个顶点被遍历
		dfs_visit[v]=true;//#label刷新
		for (int i = 0; i < vexnum; i++) 
		{//#图标框在矩阵区滑动
			if((arc[v][i]!=0)&& !dfs_visit[i] && arc[v][i]!=inf)
			{//#框停止于目标位置,图像区圈出相应顶点
				df_travel(i);
			}
		}
	}
	//#图像区标记数组擦除
	//(YY)矩阵DFS结束
	
	
	/******（YY）邻接矩阵深度遍历（连通非连通图）******
	 * 调用BFS（x），x对应：遍历起点的顶点标号（0开头）；
	 * G.bfs_result访问遍历序列结果
	 * （同DFS）分步演示标记点
	 * 通信机制待添加wait()notify()
	 */

	void BFS(int x){
		bfs_num1=0;
		bfs_num2=0;
		for(int i=0;i<vexnum;i++)
			for(int j=0;j<vexnum;j++)
				bfs_result[i][j]=-1;
		for(int i=0;i<vexnum;i++)
			bfs_visit[i]=false;
		bf_travel(x);
		bfs_num1++;
		for(int i=0;i<vexnum;i++)
		{
			if(!bfs_visit[i])//#框出顶点
			{
				bfs_num2=0;
				bf_travel(i);
				bfs_num1++;
			}
		}
	}
	void bf_travel(int v)
	{
		//创建一个队列
		Queue<Integer> queue = new LinkedList<Integer>();
		int x;
		
		//System.out.print(vex[v]+"\t");
		//bfs_result=bfs_result+vex[v]+"\t";
		int j=0,w=0;
		bfs_result[bfs_num1][bfs_num2++]=v;//#图像区动画，例如颜色变化表示一个顶点被遍历
		bfs_visit[v]=true;
		queue.offer(v);
		
		while(queue.peek()!=null)
		{
			x=queue.poll();
			for(int i=0;i<vexnum;i++)
			{
				if((arc[x][i]!=0)&&!bfs_visit[i]&& arc[x][i]!=inf)
				{
					System.out.print(vex[i]+"\t");
					w++;
					bfs_result[bfs_num1][bfs_num2++]=i;
					queue.offer(i);
					bfs_visit[i]=true;
				}
			}
		}
	}
	//#图像区所有标记清除
	//(YY)矩阵BFS结束
	
	//有向图或者无向图是否能找到一条简单路径（哈密尔顿问题）
	public int[] Hamilton(MGraph G) {
		Boolean flag=true;
		int[] path=new int[G.vexnum];
		for(int i=0;i<G.vexnum;i++) {
			visit[i]=0;
			path[i]=-1;
		}
		for(int i=0;i<G.vexnum;i++) {
			if(visit[i]==0) {
			    dfs_ht(G,i,visit,path,flag);	
			}
		}
		for(int i=0;i<G.vexnum;i++) {
//			System.out.print(path[i]);
//			System.out.println();
		}
		return path;
	}
	public void dfs_ht(MGraph G,int i,int[] visit,int[] path,Boolean flag) {
		if(flag==false)	return;
		visit[i]=1;
		path[n]=i; n++;
		if(n==G.vexnum) {
			flag=false;
		}
		int j=0;
		if(n==G.vexnum) {
			for(j=0;j<G.vexnum;j++) {
//				System.out.print(path[j]);
//				break;
			}
//			System.out.println();
		}
		for(j=0;j<G.vexnum;j++) {
			if(G.arc[i][j]!=0 && G.arc[i][j]!=inf && visit[j]==0)
				dfs_ht(G,j,visit,path,flag);
		}
		visit[i]=0;
		n--;
	}
	
	//判断无向图是否有环（DFS的应用）
		public void dfs_huan(MGraph G) {
			int[] visit=new int[G.vexnum];
			for(int i=0;i<G.vexnum;i++) visit[i]=0;
			int i=0;
			dfs_(G,visit,i);
		}
		public void dfs_(MGraph G,int[] visit,int i) {
			visit[i]=1;
			int di=0;    //边数
			int vi=0;    //标记数
			for(int j=0;j<G.vexnum;j++) {
				if(G.arc[i][j]!=0 && G.arc[i][j]!=inf) {
					di++;
					if(visit[j]==1) vi++;
				}
			}
			if(vi==di && di!=1) {
				System.out.println("有回路");
				return;
			}
			for(int j=0;j<G.vexnum;j++) {
				if(G.arc[i][j]!=0 && G.arc[i][j]!=inf && visit[j]==0) dfs_(G,visit,j);
			}
		}
		
		/******（YY）prim算法（连通图，无向网）******
		 * G.prim()
		 * G.prim_result访问遍历序列结果
		 * 分步演示标记点
		 */
		
		class Low {
			int vi;//生成树上顶点（已知顶点）的下标
			int weight;//待选边权值
		}
		public void prim() 
		{
			int min,k=0;
			Low[] low=new Low[vexnum];
			//初始化
			
			for(int i=0;i<vexnum;i++)
			{
				low[i]=new Low();
				low[i].vi=0;
				low[i].weight=arc[0][i];
			}
			low[0].weight=0;
			//更新low
			for(int i=0;i<vexnum;i++)
			{
				min=inf;
				for(int j=0;j<vexnum;j++)
					if(low[j].weight<min&&low[j].weight!=0)
					{	min=low[j].weight;	k=j;	}
				//#输出生成边，图像图对应比边线颜色变化
//				System.out.print(vex[low[k].vi]+"--"+vex[k]+"\n");
				low[k].weight=0;
				for(int j=0;j<vexnum;j++)
					if(arc[k][j]<low[j].weight)
					{
						low[j].weight=arc[k][j];
						low[j].vi=k;
					}
			}
//			System.out.print("\n最小生成树的边为:\n");
			for(int i=1;i<vexnum;i++)
			{
				System.out.print(vex[low[i].vi]+"--"+vex[i]+"\n");
				prim_result[i-1][0]=low[i].vi;
				prim_result[i-1][1]=i;
			}
		}
	
		//最小生成树之Kruskal
		public int[][] Kruskal(MGraph G) {
			int [][] path=new int[G.arcnum-1][2];
			Edge[] e=new Edge[G.arcnum];
			for(int i=0;i<e.length;i++) {
				e[i]=new Edge();
			}
			int[] visit=new int[G.vexnum];
			int k=0,i=0,j=0;
			for(i=0;i<G.vexnum;i++) visit[i]=i;
			for(i=0;i<G.vexnum;i++) {    //提取所有的边
				for(j=i;j<G.vexnum;j++) {
					if(G.arc[i][j]!=0 && G.arc[i][j]!=inf) {
						e[k].v=i;	e[k].w=j;
						e[k].weight=G.arc[i][j];
						k++;
					}
				}
			}
			Edge t;
			for(i=0;i<k;i++) {          //排序
				for(j=0;j<k-i-1;j++) {
					if(e[j].weight>e[j+1].weight) {
						t=e[j];
						e[j]=e[j+1];
						e[j+1]=t;
					}
				}
			}
			k=0;
//			System.out.println(e[0].weight);
			for(i=0;i<G.arcnum;i++) {
				int p=visit[e[i].v];
				int q=visit[e[i].w];
				if(p!=q) {
					visit[e[i].v]=visit[e[i].w];
					path[k][0]=e[i].v;
					path[k][1]=e[i].w;
					System.out.println(G.vex[e[i].v]+" "+G.vex[e[i].w]+" "+e[i].weight);
					k++;
					for(int r=0;r<G.vexnum;r++) {
						if(visit[r]==p) {
							visit[r]=q;
						}
					}
				}
			}
			return path;
		}

	public static int[][] Dijkstra(MGraph G, int v) { // 最短路径：Dijkstra
		int[] dist = new int[G.vexnum];
		int[][] path = new int[G.vexnum][G.vexnum];
		int k, m, min;
		boolean flag = true;
		for (int i = 0; i < G.vexnum; i++)
			for (int j = 0; j < G.vexnum; j++)
				path[i][j] = -1;
		for (int i = 0; i < G.vexnum; i++) {
			dist[i] = G.arc[v][i];
			if (dist[i] != 0 && dist[i] != inf) {
				path[i][0] = v;
				path[i][1] = i;
			}
		}
		while (flag) {
			k = 0;
			min = inf;
			for (int j = 0; j < G.vexnum; j++)
				if (dist[j] != 0 && dist[j] < min) {
					k = j;
					min = dist[j];
				}
			for (int j = 0; j < G.vexnum; j++)
				if (dist[k] + G.arc[k][j] < dist[j]) {
					dist[j] = dist[k] + G.arc[k][j];
					for (m = 0; m < G.vexnum; m++)
						path[j][m] = path[k][m];
					for (m = 0; m < G.vexnum && path[j][m] != -1; m++)
						;
					path[j][m] = j;
				}
			dist[k] = 0;
			flag = false;
			for (int j = 0; j < G.vexnum; j++)
				if (dist[j] != 0 && dist[j] < inf)
					flag = true;
		}
		for (int i = 0; i < G.vexnum; i++) {
			if (i == v)
				continue;
			System.out.printf("%s到%s的最短路径为:", G.vex[v], G.vex[i]);
			for (int j = 0; j < G.vexnum && path[i][j] != -1; j++) {
				System.out.printf("%s ", G.vex[path[i][j]]);
			}
			System.out.println();
		}
		return path;
	}
	
}
