package Test;

import java.util.*;

/**********************������λ��Facade����*************************/

public class AdjGraph {		//	�ڽӱ�
	public static class ArcNode {
		public int adjvex;
		public int weight;
		public ArcNode nextarc;
	} // �߽��

	public static class VexNode {
		public String data;
		public int num = 0; // �������
		public ArcNode firstarc = null;
	} // ����

	public VexNode[] Alist;
	public int vexnum = 0;
	public int arcnum = 0;
	public int type = 0; // ͼ�����ͣ�������=1��������=2
	String[] vexname=null;
	Edge[] e=null;

	public AdjGraph() {}
	public AdjGraph(String[] vexname, Edge[] e, int type) { // �ڽӱ�ĳ�ʼ��
		this.vexname=vexname;
		this.vexnum = vexname.length;
		this.e=e;
		this.arcnum = e.length;
		this.type = type;
		Alist = new VexNode[vexnum];
		for (int i = 0; i < vexnum; i++) {
			Alist[i] = new VexNode();
			Alist[i].data = vexname[i];
		}
		switch (type) {
		case 1: // ������
			for (int i = 0; i < arcnum; i++) {
				ArcNode p = new ArcNode();
				ArcNode q = new ArcNode();
				this.Alist[e[i].w].num++;
				this.Alist[e[i].v].num++;
				p.adjvex = e[i].w;
				q.adjvex = e[i].v;
				p.weight = e[i].weight;
				q.weight = e[i].weight;
				p.nextarc = Alist[e[i].v].firstarc;
				q.nextarc = Alist[e[i].w].firstarc;
				Alist[e[i].v].firstarc = p;
				Alist[e[i].w].firstarc = q;
			}
			break;
		case 2: // ������
			for (int i = 0; i < arcnum; i++) {
				ArcNode p = new ArcNode();
				this.Alist[e[i].w].num++;
				p.adjvex = e[i].w;
				p.weight = e[i].weight;
				p.nextarc = Alist[e[i].v].firstarc;
				Alist[e[i].v].firstarc = p;
			}
			break;
		}
	}
	
	public AdjGraph clone() {	//	�����ڽӱ�ĸ���
		AdjGraph ag=new AdjGraph(this.vexname,this.e,this.type);
		return ag;
	}

	public static void output(AdjGraph G) { // ����ڽӱ�
		System.out.println("��ͼ���ڽӱ�Ϊ��");
		for (int i = 0; i < G.vexnum; i++) {
			System.out.printf("  %s", G.Alist[i].data);
			ArcNode p = G.Alist[i].firstarc;
			while (p != null) {
				System.out.printf("->%s", G.Alist[p.adjvex].data);
				p = p.nextarc;
			}
			System.out.println();
		}
	}

	public static boolean top_loop(AdjGraph G,int[] result) { // �ж�����ͼ�Ƿ���ڻ�·
		if(G.type==1)	return false;
		int v, w, n = 0;
		ArcNode p;
		Stack<Integer> S = new Stack<Integer>();
		for (int i = 0; i < G.vexnum; i++)
			if (G.Alist[i].num == 0)
				S.push(new Integer(i));
		while (!S.empty()) {
			v = S.pop().intValue();
			result[n]=v;	//********
			n++;
			p = G.Alist[v].firstarc;
			while (p != null) {
				w = p.adjvex;
				G.Alist[w].num--;
				if (G.Alist[w].num == 0)
					S.push(new Integer(w));
				p = p.nextarc;
			}
		}
		if (n < G.vexnum)
			return true;
		else
			return false;
	}
	
	public static int[][] critical_path(AdjGraph G) { // �ؼ�·��
		int [][]path_critical=new int[G.vexnum][2];
		for (int i = 0; i < G.vexnum; i++) {
			path_critical[i][0]=-1;
			path_critical[i][1]=-1;
		}
		if (G.type != 2) {
			System.out.println("��ͼ��������ͼ��");
			return path_critical;
		}
		int v, w;
		int ve[] = new int[G.vexnum], vl[] = new int[G.vexnum];
		ArcNode p;
		Stack<Integer> S1 = new Stack<Integer>(), S2 = new Stack<Integer>();
		for (int i = 0; i < G.vexnum; i++) {
			ve[i] = 0;
		}
		for (int i = 0; i < G.vexnum; i++)
			if (G.Alist[i].num == 0)
				S1.push(new Integer(i));
		while (!S1.empty()) {
			v = S1.pop().intValue();
			S2.push(new Integer(v));
			p = G.Alist[v].firstarc;
			while (p != null) {
				w = p.adjvex;
				G.Alist[w].num--;
				if (G.Alist[w].num == 0)
					S1.push(new Integer(w));
				if (ve[v] + p.weight > ve[w])
					ve[w] = ve[v] + p.weight;
				p = p.nextarc;
			}
		}
		for (int i = 0; i < G.vexnum; i++)
			vl[i] = ve[G.vexnum - 1];
		while (!S2.empty()) {
			v = S2.pop().intValue();
			p = G.Alist[v].firstarc;
			while (p != null) {
				w = p.adjvex;
				if (vl[w] - p.weight < vl[v])
					vl[v] = vl[w] - p.weight;
				p = p.nextarc;
			}
		}
		System.out.println("�ؼ�·��Ϊ:");
		int i=0;
		for (i = 0; i < G.vexnum; i++) {
			if (ve[i] == vl[i])
				System.out.printf("%s ", G.Alist[i].data);
			System.out.println();
			path_critical[i][0]=ve[i];
			path_critical[i][1]=vl[i];
		}
		return path_critical;
	}
	
}
