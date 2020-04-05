package Test;

import java.util.*;

/**********************������λ��Facade����*************************/

public class MGraph {	//	�ڽӾ���
	public static final int inf = 999; // ���������
	public String[] vex;
	/** �洢�ڵ���Ϣ */
	public int[][] arc;
	/** �洢����Ϣ���ڽӾ��� */
	public int vexnum = 0;
	public int arcnum = 0;
	public int type = 0; // ͼ�����ͣ�������=1��������=2
	Edge[] e=null;
	
	int n=0;
	String[] path;
	int[] visit;
	boolean[] dfs_visit;//������ʱ������
	int[][] dfs_result;
	boolean[] bfs_visit;//������ʱ������
	int[][] bfs_result;
	int[][] prim_result;
	int dfs_num1,dfs_num2,bfs_num1,bfs_num2;

	public MGraph() {}
	public MGraph(String[] vexname, Edge[] e, int type) { // �ڽӾ���ĳ�ʼ��
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
		case 1: // ������
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
		case 2: // ������
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
	
	public MGraph clone() {		//	�����ڽӾ���ĸ���
		MGraph mg=new MGraph(this.vex,this.e,this.type);
		return mg;
	}
	
	public boolean isNeighbor(int v,int w) {	//	�ж�������Ƿ���ڱ�
		if(this.arc[v][w]!=inf && this.arc[v][w]!=0)
			return true;
		else return false;
	}

	public static void output(MGraph G) { // �������
		System.out.println("��ͼ���ڽӾ���Ϊ��");
		for (int i = 0; i < G.vexnum; i++) {
			for (int j = 0; j < G.vexnum; j++)
				System.out.printf("%-3d  ", G.arc[i][j]);
			System.out.println();
		}
	}
	
	/******��YY���ڽӾ�����ȱ�������ͨ����ͨͼ��******
	 * ����DFS��x����x��Ӧ���������Ķ����ţ�0��ͷ����
	 * G.dfs_result���ʱ������н��
	 * ͼ�������ӷ��ʱ�����飬��ʱ�뵽label,���ж�Ӧ�����źͷ������
	 * �ֲ���ʾ��ǵ�,ͨ�Ż��ƴ����wait()notify()
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
			if(!dfs_visit[i])//#�������
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
		dfs_result[dfs_num1][dfs_num2++]=v;//#ͼ����������������ɫ�仯��ʾһ�����㱻����
		dfs_visit[v]=true;//#labelˢ��
		for (int i = 0; i < vexnum; i++) 
		{//#ͼ����ھ���������
			if((arc[v][i]!=0)&& !dfs_visit[i] && arc[v][i]!=inf)
			{//#��ֹͣ��Ŀ��λ��,ͼ����Ȧ����Ӧ����
				df_travel(i);
			}
		}
	}
	//#ͼ��������������
	//(YY)����DFS����
	
	
	/******��YY���ڽӾ�����ȱ�������ͨ����ͨͼ��******
	 * ����BFS��x����x��Ӧ���������Ķ����ţ�0��ͷ����
	 * G.bfs_result���ʱ������н��
	 * ��ͬDFS���ֲ���ʾ��ǵ�
	 * ͨ�Ż��ƴ����wait()notify()
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
			if(!bfs_visit[i])//#�������
			{
				bfs_num2=0;
				bf_travel(i);
				bfs_num1++;
			}
		}
	}
	void bf_travel(int v)
	{
		//����һ������
		Queue<Integer> queue = new LinkedList<Integer>();
		int x;
		
		//System.out.print(vex[v]+"\t");
		//bfs_result=bfs_result+vex[v]+"\t";
		int j=0,w=0;
		bfs_result[bfs_num1][bfs_num2++]=v;//#ͼ����������������ɫ�仯��ʾһ�����㱻����
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
	//#ͼ�������б�����
	//(YY)����BFS����
	
	//����ͼ��������ͼ�Ƿ����ҵ�һ����·�������ܶ������⣩
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
	
	//�ж�����ͼ�Ƿ��л���DFS��Ӧ�ã�
		public void dfs_huan(MGraph G) {
			int[] visit=new int[G.vexnum];
			for(int i=0;i<G.vexnum;i++) visit[i]=0;
			int i=0;
			dfs_(G,visit,i);
		}
		public void dfs_(MGraph G,int[] visit,int i) {
			visit[i]=1;
			int di=0;    //����
			int vi=0;    //�����
			for(int j=0;j<G.vexnum;j++) {
				if(G.arc[i][j]!=0 && G.arc[i][j]!=inf) {
					di++;
					if(visit[j]==1) vi++;
				}
			}
			if(vi==di && di!=1) {
				System.out.println("�л�·");
				return;
			}
			for(int j=0;j<G.vexnum;j++) {
				if(G.arc[i][j]!=0 && G.arc[i][j]!=inf && visit[j]==0) dfs_(G,visit,j);
			}
		}
		
		/******��YY��prim�㷨����ͨͼ����������******
		 * G.prim()
		 * G.prim_result���ʱ������н��
		 * �ֲ���ʾ��ǵ�
		 */
		
		class Low {
			int vi;//�������϶��㣨��֪���㣩���±�
			int weight;//��ѡ��Ȩֵ
		}
		public void prim() 
		{
			int min,k=0;
			Low[] low=new Low[vexnum];
			//��ʼ��
			
			for(int i=0;i<vexnum;i++)
			{
				low[i]=new Low();
				low[i].vi=0;
				low[i].weight=arc[0][i];
			}
			low[0].weight=0;
			//����low
			for(int i=0;i<vexnum;i++)
			{
				min=inf;
				for(int j=0;j<vexnum;j++)
					if(low[j].weight<min&&low[j].weight!=0)
					{	min=low[j].weight;	k=j;	}
				//#������ɱߣ�ͼ��ͼ��Ӧ�ȱ�����ɫ�仯
//				System.out.print(vex[low[k].vi]+"--"+vex[k]+"\n");
				low[k].weight=0;
				for(int j=0;j<vexnum;j++)
					if(arc[k][j]<low[j].weight)
					{
						low[j].weight=arc[k][j];
						low[j].vi=k;
					}
			}
//			System.out.print("\n��С�������ı�Ϊ:\n");
			for(int i=1;i<vexnum;i++)
			{
				System.out.print(vex[low[i].vi]+"--"+vex[i]+"\n");
				prim_result[i-1][0]=low[i].vi;
				prim_result[i-1][1]=i;
			}
		}
	
		//��С������֮Kruskal
		public int[][] Kruskal(MGraph G) {
			int [][] path=new int[G.arcnum-1][2];
			Edge[] e=new Edge[G.arcnum];
			for(int i=0;i<e.length;i++) {
				e[i]=new Edge();
			}
			int[] visit=new int[G.vexnum];
			int k=0,i=0,j=0;
			for(i=0;i<G.vexnum;i++) visit[i]=i;
			for(i=0;i<G.vexnum;i++) {    //��ȡ���еı�
				for(j=i;j<G.vexnum;j++) {
					if(G.arc[i][j]!=0 && G.arc[i][j]!=inf) {
						e[k].v=i;	e[k].w=j;
						e[k].weight=G.arc[i][j];
						k++;
					}
				}
			}
			Edge t;
			for(i=0;i<k;i++) {          //����
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

	public static int[][] Dijkstra(MGraph G, int v) { // ���·����Dijkstra
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
			System.out.printf("%s��%s�����·��Ϊ:", G.vex[v], G.vex[i]);
			for (int j = 0; j < G.vexnum && path[i][j] != -1; j++) {
				System.out.printf("%s ", G.vex[path[i][j]]);
			}
			System.out.println();
		}
		return path;
	}
	
}
