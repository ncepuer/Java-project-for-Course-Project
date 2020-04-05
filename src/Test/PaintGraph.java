package Test;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**********************主方法位于Facade类中*************************/

public class PaintGraph extends JPanel {		//			画图类
	DisplayGUI parent;
	MGraph mg;
	AdjGraph ag;
	Graphics graph;
	final int inf = 999;
	List<Point> ps;
	int status = -1; // 状态：-1=未开始，0=进行中，1=已结束
	int num=0;
	int num1=0;
	String result="";

	public PaintGraph(DisplayGUI p) {
		this.parent = p;
		this.mg = p.mg;
		this.ag = p.ag;
		this.graph = this.getGraphics();
	}

	public void paint(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		graph=g;
		ps = getPoints(mg.vexnum);
		for (int i = 0; i < mg.vexnum; i++) {
			drawNode(mg.vex[i], i, Color.RED, Color.BLACK, g);
		}
		for (int i = 0; i < mg.vexnum; i++) {
			for (int j = 0; j < mg.vexnum; j++) {
				if (mg.arc[i][j] != 0 && mg.arc[i][j] != inf) {
					drawLink(i, j, Color.RED, Color.BLACK, g);
					if(mg.type==2)
						drawRow(i, j, Color.RED, (Graphics2D)g);
				}
			}
		}
	}

	List<Point> getPoints(int n) {			//			找到正n边形各顶点的坐标
		ArrayList<Point> ps = new ArrayList<Point>();
		int ox = getWidth() / 2;
		int oy = getHeight() / 2;
		double r = (ox > oy ? oy : ox) * 0.9;
		double angle = 2 * Math.PI / n;
		double startAngle = (Math.PI - angle) / 2;
		for (int i = 0; i < n; i++) {
			int x = (int) (ox + r * Math.cos(startAngle + i * angle));
			int y = (int) (oy + r * Math.sin(startAngle + i * angle));
			ps.add(new Point(x, y));
		}
		return ps;
	}
	
	/**************已修改****************/
	public void change(int[] vex_list,int num) {			//			展示图结构的变化	
		if(parent.selection==4) {
			drawNode(ag.Alist[vex_list[num]].data,vex_list[num],Color.BLUE,Color.WHITE,getGraphics());
			if(num+1<vex_list.length && mg.arc[vex_list[num]][vex_list[num+1]]!=0 && mg.arc[vex_list[num]][vex_list[num+1]]!=inf) {
				drawLink(vex_list[num], vex_list[num+1], Color.BLUE, Color.BLACK, getGraphics());
				if(ag.type==2)
					drawRow(vex_list[num], vex_list[num+1], Color.BLUE, (Graphics2D)getGraphics());
			}
		}
		else if(parent.selection==8) {
			drawNode(ag.Alist[vex_list[num]].data,vex_list[num],Color.BLUE,Color.WHITE,getGraphics());
			for(int i=0;i<mg.vexnum;i++)
				if(mg.isNeighbor(vex_list[num], i)) {
				drawLink(vex_list[num], i, Color.WHITE, Color.WHITE, getGraphics());
				if(ag.type==2)
					drawRow(vex_list[num], i, Color.WHITE, (Graphics2D)getGraphics());
			}
		}
		else if(parent.selection==9) {
			drawNode(ag.Alist[vex_list[num]].data,vex_list[num],Color.BLUE,Color.WHITE,getGraphics());
			for(int i=0;i<mg.vexnum;i++)
				if(mg.isNeighbor(vex_list[num], i)) {
				drawLink(vex_list[num], i, Color.WHITE, Color.WHITE, getGraphics());
				if(ag.type==2)
					drawRow(vex_list[num], i, Color.WHITE, (Graphics2D)getGraphics());
			}
		}
	}
	
	public void change(int[][] vex_list, int num) {			//			展示图结构的变化
		if(parent.selection==1) {
			drawNode(ag.Alist[vex_list[num1][num]].data,vex_list[num1][num],Color.BLUE,Color.WHITE,getGraphics());
			if(num+1<vex_list.length && mg.arc[vex_list[num1][num]][vex_list[num1][num+1]]!=0 && mg.arc[vex_list[num1][num]][vex_list[num1][num+1]]!=inf) {
				drawLink(vex_list[num1][num], vex_list[num1][num+1], Color.BLUE, Color.BLACK, getGraphics());
				if(ag.type==2)
					drawRow(vex_list[num1][num], vex_list[num1][num+1], Color.BLUE, (Graphics2D)getGraphics());
			}
		}
		else if(parent.selection==2) {
			drawNode(ag.Alist[vex_list[num1][num]].data,vex_list[num1][num],Color.BLUE,Color.WHITE,getGraphics());
			if(num+1<vex_list.length && mg.arc[vex_list[num1][num]][vex_list[num1][num+1]]!=0 && mg.arc[vex_list[num1][num]][vex_list[num1][num+1]]!=inf) {
				drawLink(vex_list[num1][num], vex_list[num1][num+1], Color.BLUE, Color.BLACK, getGraphics());
				if(ag.type==2)
					drawRow(vex_list[num1][num], vex_list[num1][num+1], Color.BLUE, (Graphics2D)getGraphics());
			}
		}
		else if(parent.selection==5)
		{
			drawNode(mg.vex[vex_list[num][0]],vex_list[num][0],Color.BLUE,Color.WHITE,getGraphics());
			drawNode(mg.vex[vex_list[num][1]],vex_list[num][1],Color.BLUE,Color.WHITE,getGraphics());
			drawLink(vex_list[num][0],vex_list[num][1], Color.BLUE,Color.BLACK,getGraphics());
			if(parent.mg.type==2)
				drawRow(vex_list[num][0],vex_list[num][1], Color.BLUE, (Graphics2D)getGraphics());
		}
		else if(parent.selection==6)
		{
			drawNode(mg.vex[vex_list[num][0]],vex_list[num][0],Color.BLUE,Color.WHITE,getGraphics());
			drawNode(mg.vex[vex_list[num][1]],vex_list[num][1],Color.BLUE,Color.WHITE,getGraphics());
			drawLink(vex_list[num][0],vex_list[num][1], Color.BLUE,Color.BLACK,getGraphics());
			if(parent.mg.type==2)
				drawRow(vex_list[num][0],vex_list[num][1], Color.BLUE, (Graphics2D)getGraphics());
		}
		else if(parent.selection==7)
		{
			drawNode(mg.vex[vex_list[parent.end][num]],vex_list[parent.end][num],Color.BLUE,Color.WHITE,getGraphics());
			if(vex_list[parent.end][num+1]!=-1)
			{
				drawLink(vex_list[parent.end][num],vex_list[parent.end][num+1], Color.BLUE,Color.BLACK,getGraphics());
				if(parent.mg.type==2)
					drawRow(vex_list[parent.end][num],vex_list[parent.end][num+1], Color.BLUE, (Graphics2D)getGraphics());
			}
		}
		else if(parent.selection==9)
		{
			drawNode(ag.Alist[num].data,num,Color.BLUE,Color.WHITE,getGraphics());
		}
	}

	/**************已修改****************/
	public int proceed(int step) {			//		算法步骤的跳跃
		int nextstep = -1;
		int sleeptime=1000;
		switch (parent.selection) {
		case 1:
			switch (step) {
			case 0:
				status = 0;
				num1=0;
				nextstep = 1;
				break;
			case 1:
				nextstep = 2;
				break;
			case 2:
				nextstep = 3;
				break;
			case 3:
				nextstep=4;
				break;
			case 4:
				nextstep = 5;
				break;
			case 5:
				num=0;
				if (parent.vexlist_2[num1][num]==-1)
					nextstep = 8;
				else {
					nextstep = 6;
				}
				break;
			case 6:
				nextstep = 7;
				break;
			case 7:
				nextstep = 9;
				break;
			case 8:
				status = 1;
				result="深度优先遍历的结果为：";
				for(int i=0;i<mg.vexnum;i++)
					for(int j=0;j<mg.vexnum;j++)
						if(parent.vexlist_2[i][j]!=-1)
							result=result+parent.mg.vex[parent.vexlist_2[i][j]]+" ";
				nextstep = 0;
				break;
			case 9:
				nextstep = 10;
				break;
			case 10:
				this.change(parent.vexlist_2,num);
				num++;
				nextstep = 11;
				break;
			case 11:
				if(num>=parent.vexlist_2[num1].length || parent.vexlist_2[num1][num]==-1) {
					nextstep = 14;
				}
				else {
					nextstep = 12;
				}
				break;
			case 12:
				nextstep = 13;
				break;
			case 13:
				nextstep = 9;
				break;
			case 14:
				num1++;
				nextstep=5;
				break;
			}
			break;
		case 2:
			switch (step) {
			case 0:
				status = 0;
				num1=0;
				nextstep = 1;
				break;
			case 1:
				nextstep = 2;
				break;
			case 2:
				nextstep = 3;
				break;
			case 3:
				nextstep=4;
				break;
			case 4:
				num=0;
				if (parent.vexlist_2[num1][num]==-1)
					nextstep = 7;
				else {
					nextstep = 5;
				}
				break;
			case 5:
				nextstep = 6;
				break;
			case 6:
				nextstep = 8;
				break;
			case 7:
				status = 1;
				result="广度优先遍历的结果为：";
				for(int i=0;i<mg.vexnum;i++)
					for(int j=0;j<mg.vexnum;j++)
						if(parent.vexlist_2[i][j]!=-1)
							result=result+parent.mg.vex[parent.vexlist_2[i][j]]+" ";
				nextstep = 0;
				break;
			case 8:
				nextstep = 9;
				break;
			case 9:
				nextstep = 10;
				break;
			case 10:
				this.change(parent.vexlist_2,num);
				num++;
				nextstep = 11;
				break;
			case 11:
				nextstep=12;
				break;
			case 12:
				if(num>=parent.vexlist_2[num1].length || parent.vexlist_2[num1][num]==-1) {
					nextstep = 21;
				}
				else {
					nextstep = 13;
				}
				break;
			case 13:
				nextstep = 14;
				break;
			case 14:
				nextstep = 15;
				break;
			case 15:
				nextstep = 16;
				break;
			case 16:
				this.change(parent.vexlist_2,num);
				num++;
				nextstep = 17;
				break;
			case 17:
				nextstep = 18;
				break;
			case 18:
				nextstep = 19;
				break;
			case 19:
				nextstep = 20;
				break;
			case 20:
				nextstep = 12;
				break;
			case 21:
				num1++;
				nextstep = 4;
				break;
			}
			break;
		case 3:
			switch (step) {
			case 0:
			}
			break;
		case 4:
			switch (step) {
			case 0:
				status=0;
				nextstep=1;break;
			case 1:
				nextstep=2;break;
			case 2:
				nextstep=3;break;
			case 3:
				nextstep=5;break;
			case 4:
				status=1;
				if(parent.vexlist_1[mg.vexnum-1]==-1) {
					result="此图不存在哈密尔顿链！";
				}
				else {
					result="此图的一条哈密尔顿链为：";
					for(int i=0;i<parent.vexlist_1.length;i++)
						result=result+mg.vex[parent.vexlist_1[i]]+" ";
				}
				nextstep=0;break;
			case 5:
				nextstep=6;break;
			case 6:
				nextstep=7;break;
			case 7:
				if(parent.vexlist_1[num]!=-1) {
					if(num+1>=parent.vexlist_1.length)
						change(parent.vexlist_1,num);
					else
						if(parent.vexlist_1[num+1]!=-1)
							change(parent.vexlist_1,num);
				}
				num++;
				nextstep=8;break;
			case 8:
				if(num==parent.vexlist_1.length)
					nextstep=9;
				else
					nextstep=13;
					break;
			case 9:
				nextstep=10;break;
			case 10:
				nextstep=11;break;
			case 11:
				nextstep=12;break;
			case 12:
				nextstep=4;break;
			case 13:
				nextstep=14;break;
			case 14:
				nextstep=15;break;
			case 15:
				nextstep=5;break;
			case 16:
				nextstep=17;break;
			case 17:
				nextstep=4;
				break;
			}
			break;
		case 5:
			switch (step) {
			case 0:
				status=0;
				nextstep=1;break;
			case 1:
				nextstep=2;break;
			case 2:
				nextstep=3;break;
			case 3:
				nextstep=4;break;
			case 4:
				nextstep=5;break;
			case 5:
				nextstep=6;break;
			case 6:
				nextstep=7;break;
			case 7:
				if(num>=parent.vexlist_2.length)
					nextstep=19;
				else
					nextstep=8;
				break;
			case 8:
				nextstep=9;break;
			case 9:
				nextstep=10;break;
			case 10:
				nextstep=11;break;
			case 11:
				nextstep=12;break;
			case 12:
				nextstep=13;break;
			case 13:
				nextstep=14;break;
			case 14:
				nextstep=15;break;
			case 15:
				nextstep=16;break;
			case 16:
				nextstep=17;break;
			case 17:
				change(parent.vexlist_2,num);
				num++;
				nextstep=18;break;
			case 18:
					nextstep=7;
				break;
			case 19:
				nextstep=20;break;
			case 20:
				status=1;
				result="最小生成树的边有：";
				for(int i=0;i<parent.vexlist_2.length;i++)
					result=result+mg.vex[parent.vexlist_2[i][0]]+"-"+mg.vex[parent.vexlist_2[i][1]]+"  ";
				nextstep=0;break;
			}
			break;
		case 6:
			switch (step) {
			case 0:
				status=0;
				nextstep=1;break;
			case 1:
				nextstep=2;break;
			case 2:
				nextstep=3;break;
			case 3:
				nextstep=4;break;
			case 4:
				nextstep=5;break;
			case 5:
				nextstep=6;break;
			case 6:
				nextstep=7;break;
			case 7:
				nextstep=8;break;
			case 8:
				nextstep=9;break;
			case 9:
				nextstep=10;break;
			case 10:
				nextstep=11;break;
			case 11:
				nextstep=12;break;
			case 12:
				if(parent.vexlist_2[parent.end][num]==-1)
					nextstep=29;
				else
					nextstep=13;
				break;
			case 13:
				nextstep=14;break;
			case 14:
				nextstep=15;break;
			case 15:
				nextstep=16;break;
			case 16:
				nextstep=17;break;
			case 17:
				nextstep=18;break;
			case 18:
				if(num>=parent.vexlist_2.length)
					nextstep=27;
				else
					nextstep=19;
				break;
			case 19:
				nextstep=20;break;
			case 20:
				nextstep=21;break;
			case 21:
				this.change(parent.vexlist_2, num);
				num++;
				nextstep=22;break;
			case 22:
				nextstep=23;break;
			case 23:
				nextstep=24;break;
			case 24:
				nextstep=25;break;
			case 25:
				nextstep=26;break;
			case 26:
				nextstep=18;break;
			case 27:
				status=1;
				result="最小生成树的边有：";
				for(int i=0;i<parent.vexlist_2.length;i++)
					result=result+mg.vex[parent.vexlist_2[i][0]]+"-"+mg.vex[parent.vexlist_2[i][1]]+"  ";
				nextstep=0;break;
			}
			break;
		case 7:
			switch (step) {
			case 0:
				status=0;
				nextstep=1;break;
			case 1:
				nextstep=2;break;
			case 2:
				nextstep=3;break;
			case 3:
				nextstep=4;break;
			case 4:
				nextstep=5;break;
			case 5:
				nextstep=6;break;
			case 6:
				nextstep=7;break;
			case 7:
				nextstep=8;break;
			case 8:
				nextstep=9;break;
			case 9:
				nextstep=10;break;
			case 10:
				nextstep=11;break;
			case 11:
				nextstep=12;break;
			case 12:
				if(parent.vexlist_2[parent.end][num]==-1)
					nextstep=29;
				else
					nextstep=13;
				break;
			case 13:
				nextstep=14;break;
			case 14:
				nextstep=15;break;
			case 15:
				nextstep=16;break;
			case 16:
				nextstep=17;break;
			case 17:
				nextstep=18;break;
			case 18:
				nextstep=19;break;
			case 19:
				nextstep=20;break;
			case 20:
				nextstep=21;break;
			case 21:
				nextstep=22;break;
			case 22:
				nextstep=23;break;
			case 23:
				this.change(parent.vexlist_2,num);
				num++;
				nextstep=24;break;
			case 24:
				nextstep=25;break;
			case 25:
				nextstep=26;break;
			case 26:
				nextstep=27;break;
			case 27:
				nextstep=28;break;
			case 28:
				nextstep=12;break;
			case 29:
				nextstep=30;break;
			case 30:
				status=1;
				result=mg.vex[parent.begin]+"至"+mg.vex[parent.end]+"的最短路径为：";
				for(int i=0;i<parent.vexlist_2[parent.end].length && parent.vexlist_2[parent.end][i]!=-1;i++)
					result=result+mg.vex[parent.vexlist_2[parent.end][i]]+" ";
				nextstep=0;break;
			}
			break;
		case 8:
			switch (step) {
			case 0:
				status=0;
				nextstep=1;break;
			case 1:
				nextstep=2;break;
			case 2:
				nextstep=3;break;
			case 3:
				nextstep=4;break;
			case 4:
				nextstep=5;break;
			case 5:
				nextstep=6;break;
			case 6:
				nextstep=7;break;
			case 7:
				if(num>=parent.vexlist_1.length || parent.vexlist_1[num]==-1)
					nextstep=16;
				else
					nextstep=8;
				break;
			case 8:
				this.change(parent.vexlist_1, num);
				num++;
				nextstep=9;break;
			case 9:
				nextstep=10;break;
			case 10:
				nextstep=11;break;
			case 11:
				parent.save2.repaint();
				nextstep=12;break;
			case 12:
				nextstep=13;break;
			case 13:
				nextstep=14;break;
			case 14:
				nextstep=15;break;
			case 15:
				nextstep=7;break;
			case 16:
				nextstep=17;break;
			case 17:
				nextstep=18;break;
			case 18:
				nextstep=19;break;
			case 19:
				status=1;
				if(parent.flag_huilu)
					result="此有向图存在回路！";
				else
					result="此有向图不存在回路！";
				nextstep=0;break;
			}
			break;
		case 9:
			switch (step) {
			case 0:
				status=0;
				nextstep=1;break;
			case 1:
				nextstep=2;break;
			case 2:
				nextstep=3;break;
			case 3:
				nextstep=4;break;
			case 4:
				nextstep=5;break;
			case 5:
				num1=1;
				parent.ps.repaint();
				nextstep=6;break;
			case 6:
				nextstep=7;break;
			case 7:
				nextstep=8;break;
			case 8:
				nextstep=9;break;
			case 9:
				if(num>=parent.vexlist_1.length)
					nextstep=21;
				else
					nextstep=10;
				break;
			case 10:
				change(parent.vexlist_1,num);
				num++;
				nextstep=11;break;
			case 11:
				nextstep=12;break;
			case 12:
				nextstep=13;break;
			case 13:
				parent.save2.repaint();
				nextstep=14;break;
			case 14:
				nextstep=15;break;
			case 15:
				nextstep=16;break;
			case 16:
				nextstep=17;break;
			case 17:
				parent.ps.repaint();
				nextstep=18;break;
			case 18:
				nextstep=19;break;
			case 19:
				nextstep=20;break;
			case 20:
				nextstep=9;break;
			case 21:
				num1=1;
				repaint();
				nextstep=22;break;
			case 22:
				num1=2;
				parent.ps.repaint();
				nextstep=23;break;
			case 23:
				if(num==0)
				{
					repaint();
					nextstep=32;
				}
				else
					nextstep=24;
				break;
			case 24:
				num--;
				change(parent.vexlist_1,num);
				nextstep=25;break;
			case 25:
				nextstep=26;break;
			case 26:
				nextstep=27;break;
			case 27:
				nextstep=28;break;
			case 28:
				parent.ps.repaint();
				nextstep=29;break;
			case 29:
				nextstep=30;break;
			case 30:
				nextstep=31;break;
			case 31:
				nextstep=23;break;
			case 32:
				if(num>=parent.vexlist_2.length)
					nextstep=35;
				else
					nextstep=33;
				break;
			case 33:
				nextstep=34;break;
			case 34:
				if(parent.vexlist_2[num][0]==parent.vexlist_2[num][1])
					change(parent.vexlist_2,num);
				num++;
				nextstep=32;
				break;
			case 35:
				status=1;
				result="所求的关键活动为：";
				for(int i=0;i<parent.vexlist_2.length;i++)
					if(parent.vexlist_2[i][0]==parent.vexlist_2[i][1])
						result=result+ag.Alist[i].data+" ";
				nextstep=0;break;
			}
			break;
		}
		return nextstep;
	}


	public void reset() {			//		重置各组件的状态
		this.mg = parent.mg;
		this.ag = parent.ag;
		this.status = -1;
		this.num=0;
		this.num1=0;
		this.result="";
		repaint();
	}

	public  void drawNode(String str, int i, Color bkColor, Color fgColor, Graphics g) { // 绘制结点
		Color old = g.getColor();
		Font old1 = g.getFont();
		g.setColor(bkColor);
		g.drawOval(ps.get(i).x - 15, ps.get(i).y - 15, 30, 30);
		g.fillOval(ps.get(i).x - 15, ps.get(i).y - 15, 30, 30);
		g.setColor(fgColor);
		g.setFont(new Font("宋体", Font.BOLD, 18));
		g.drawString(mg.vex[i], ps.get(i).x - 10, ps.get(i).y + 5);
		g.setColor(old);
		g.setFont(old1);
	}

	public void drawLink(int i, int j, Color color_line,Color color_str, Graphics g) { // 绘制边
		Color old = g.getColor();
		g.setColor(color_line);
		g.drawLine(ps.get(i).x, ps.get(i).y, ps.get(j).x, ps.get(j).y);
		g.setColor(color_str);
		g.drawString(String.valueOf(mg.arc[i][j]), (ps.get(i).x+ps.get(j).x)/2+5, (ps.get(i).y+ps.get(j).y)/2+5);
		g.setColor(old);
	}

	/**************已修改****************/
	public void drawRow(int i, int j, Color color_line, Graphics2D g2)		//		绘制箭头
	{
		Color old = g2.getColor();
		g2.setColor(color_line);
		double awrad0 = Math.atan((double)Math.abs(ps.get(j).y-ps.get(i).y)/(double)Math.abs(ps.get(j).x-ps.get(i).x));;
		int sx,sy,ex,ey;
		if(ps.get(i).x>ps.get(j).x) {
			if(ps.get(i).y>ps.get(j).y) {
				sx=(int) (ps.get(i).x-15*Math.cos(awrad0));
				sy=(int) (ps.get(i).y-15*Math.sin(awrad0));
				ex=(int) (ps.get(j).x+15*Math.cos(awrad0));
				ey=(int) (ps.get(j).y+15*Math.sin(awrad0));
			}
			else {
				sx=(int) (ps.get(i).x-15*Math.cos(awrad0));
				sy=(int) (ps.get(i).y+15*Math.sin(awrad0));
				ex=(int) (ps.get(j).x+15*Math.cos(awrad0));
				ey=(int) (ps.get(j).y-15*Math.sin(awrad0));
			}
		}
		else {
			if(ps.get(i).y>ps.get(j).y) {
				sx=(int) (ps.get(i).x+15*Math.cos(awrad0));
				sy=(int) (ps.get(i).y-15*Math.sin(awrad0));
				ex=(int) (ps.get(j).x-15*Math.cos(awrad0));
				ey=(int) (ps.get(j).y+15*Math.sin(awrad0));
			}
			else {
				sx=(int) (ps.get(i).x+15*Math.cos(awrad0));
				sy=(int) (ps.get(i).y+15*Math.sin(awrad0));
				ex=(int) (ps.get(j).x-15*Math.cos(awrad0));
				ey=(int) (ps.get(j).y-15*Math.sin(awrad0));
			}
		}
		double H = 10; // 箭头高度
		double L = 4; // 底边的一半
		int x3 = 0;
		int y3 = 0;
		int x4 = 0;
		int y4 = 0;
		double awrad = Math.atan(L / H); // 箭头角度
		double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
		double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
		double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
		double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
		double y_3 = ey - arrXY_1[1];
		double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
		double y_4 = ey - arrXY_2[1];

		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();
		// 画线
//		g2.drawLine(sx, sy, ex, ey);
		//
		GeneralPath triangle = new GeneralPath();
		triangle.moveTo(ex, ey);
		triangle.lineTo(x3, y3);
		triangle.lineTo(x4, y4);
		triangle.closePath();
		//实心箭头
		g2.fill(triangle);
		//非实心箭头
		//g2.draw(triangle);

		g2.setColor(old);
	}

	 // 计算
	 public double[] rotateVec(int px, int py, double ang,
	   boolean isChLen, double newLen) {

	  double mathstr[] = new double[2];
	  // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
	  double vx = px * Math.cos(ang) - py * Math.sin(ang);
	  double vy = px * Math.sin(ang) + py * Math.cos(ang);
	  if (isChLen) {
	   double d = Math.sqrt(vx * vx + vy * vy);
	   vx = vx / d * newLen;
	   vy = vy / d * newLen;
	   mathstr[0] = vx;
	   mathstr[1] = vy;
	  }
	  return mathstr;
	 }
	
}

/**************已修改****************/
class Save1GUI extends JPanel{			//	绘制邻接矩阵
	DisplayGUI parent;
	MGraph mg;
	AdjGraph ag;
	
	public Save1GUI(DisplayGUI p){
		this.parent=p;
		this.mg=p.mg;
		this.ag=p.ag;
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
	    g.setColor(Color.BLACK);
		int a=20,b=20,c=310,d=310;   //允许表格所占用的空间
		int t=mg.vexnum+1;  //表格的行数和列数
		int length=(c-a)/t,width=(d-b)/t; //每行的长度和宽度
		int i,j;
		int k=0,l=0;
		for(i=0;i<=t;i++) {
			g.drawLine(a+i*length, b, a+i*length, d);
			g.drawLine(a, b+i*length, c, b+i*length);
		}

		Font font = new Font("宋体", Font.BOLD, 18);
	    g.setFont(font);
	    k=0;l=0;
	    for(i=a+length,k=0;i<c;i=i+length,k++) {
	    	if(k==mg.vexnum) break;
	    	for(j=b+2*length,l=0;j<d;j=j+length,l++) {
	    		if(l==mg.vexnum) break;
	    		if(mg.arc[k][l]==MGraph.inf)
	    			g.drawString(String.valueOf(mg.arc[k][l]), i+length/8, j-length/4);
	    		else
	    			g.drawString(String.valueOf(mg.arc[k][l]), i+length/4, j-length/4);
	    	}
	    }
	    
/**  若计算机性能较差，可加上此段，防止绘图后部分区域无法正常显示   **/
//        try {
//            
//            Thread.sleep(500);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
	    
	    k=0;l=0;
	    g.drawString("顶点", a, b+length-length/4);
		g.setColor(Color.RED);
	    for(i=a+2*length;i<c;i=i+length) {
	    	g.drawString(mg.vex[k], a+length/4, i-length/4);
	    	k++;
	    	if(k==mg.vexnum) break;
	    }
	    k=0;
	    for(i=b+length;i<d;i=i+length) {
	    	g.drawString(mg.vex[k], i+length/4, b+length-length/4);
	    	k++;
	    	if(k==mg.vexnum) break;
	    }
	}
	
}

/**************已修改****************/
class Save2GUI extends JPanel{			//	绘制邻接表
	DisplayGUI parent;
	MGraph mg;
	AdjGraph ag;
	
	int length,width,t;
	final int a=20,b=20,c=250,d=250;   //允许表格所占用的空间
	
	public Save2GUI(DisplayGUI p){
		this.parent=p;
		this.ag=p.ag;
		this.mg=p.mg;
		t=ag.vexnum;
		length=(c-a)/t; width=length;
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		//from this
		
		int i=0,j=0;

		for(i=0;i<=3;i++) {
			g.drawLine(a+i*length, b, a+i*length, d);	
		}
		for(j=0;j<=t;j++) {
			g.drawLine(a, b+j*length, a+3*length, b+j*length);
		}

	    g.setColor(Color.BLACK);
		Font font = new Font("宋体", Font.BOLD, 18);
	    g.setFont(font);
		g.setColor(Color.RED);
	    int k=0,l=0;
	    int[] num0 = new int[ag.vexnum];
	    for(i=a+length;i<c;i=i+length) {
	    	num0[k] = ag.Alist[k].num;
			g.setColor(Color.RED);
	    	g.drawString(ag.Alist[k].data, a+length/4, i-length/4);
	    	if(parent.panel_paint.status==-1) {
	    		g.setColor(Color.RED);
	    		g.drawString(String.valueOf(num0[k]),a+length+length/4,i-length/4);
	    	}
	    	else if(parent.panel_paint.status==0) {
	    		g.setColor(Color.RED);
	    		for(int m=0;m<parent.panel_paint.num;m++)
	    			if(mg.isNeighbor(parent.vexlist_1[m], k)) {
	    				num0[k]--;
	    				g.setColor(Color.BLUE);
	    			}
	    		g.drawString(String.valueOf(num0[k]),a+length+length/4,i-length/4);
	    	}
	    	else if(parent.panel_paint.status==1) {
	    		g.setColor(Color.BLUE);
	    		g.drawString(String.valueOf(0),a+length+length/4,i-length/4);
	    	}
	    	k++;
	    	if(k==ag.vexnum) break;
	    }
	    
/**  若计算机性能较差，可加上此段，防止绘图后部分区域无法正常显示   **/
//        try {
//            
//            Thread.sleep(500);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }  

	    g.setColor(Color.BLACK);
	    for(i=0;i<ag.vexnum;i++) {
	    	AdjGraph.ArcNode p=new AdjGraph.ArcNode();
	    	p=ag.Alist[i].firstarc;
	    	j=0;
	    	while(p!=null) { 
	    		g.drawLine(a+2*length+length/2+7*j*length/2, b+length/2+i*length, a+length/2+j*7*length/2+3*length, b+length/2+i*length);    //绘制箭头
	    		g.drawLine(a+length/2+j*7*length/2+3*length,b+i*length,a+length/2+j*7*length/2+6*length,b+i*length); 
	    		g.drawLine(a+length/2+j*7*length/2+3*length,b+i*length+length,a+length/2+j*7*length/2+6*length,b+i*length+length); //两条横边
	    		g.drawLine(a+length/2+j*7*length/2+3*length,b+i*length,a+length/2+j*7*length/2+3*length,b+i*length+length);
	    		g.drawLine(a+length/2+j*7*length/2+4*length,b+i*length,a+length/2+j*7*length/2+4*length,b+i*length+length);
	    		g.drawLine(a+length/2+j*7*length/2+5*length,b+i*length,a+length/2+j*7*length/2+5*length,b+i*length+length);
	    		g.drawLine(a+length/2+j*7*length/2+6*length,b+i*length,a+length/2+j*7*length/2+6*length,b+i*length+length);
	    		g.drawLine(a+length/2+j*7*length/2+4*length,b+i*length,a+length/2+j*7*length/2+4*length,b+i*length+length);
	    		g.drawString(ag.Alist[p.adjvex].data,a+length/2+j*7*length/2+3*length+length/4,b+i*length+length/2+length/4);
	    		g.drawString(String.valueOf(p.weight),a+length/2+j*7*length/2+4*length+length/4,b+i*length+length/2+length/4);
	    		j++;
	    		p=p.nextarc;
	    	}
	    }
	}
	
}

/**************已修改****************/
class Critical_ps extends JPanel{		//		绘制ve。vl表格(只在关键路径功能中显示)
	DisplayGUI parent;
	AdjGraph G;
    int[][] path;
    Graphics graph;
    
    int a,b,c,d,length,width;
    
    public Critical_ps(DisplayGUI p){
    	this.parent=p;
    	this.G=p.ag;
    	this.graph=this.getGraphics();
    	a=10;	b=10;
    	c=280;	d=280;
    	length=(c-a)/(G.vexnum+1);
    	width=length;
    	path=G.critical_path(p.ag.clone());
    }
    
    public void paint(Graphics g) {
    	g.clearRect(0, 0, this.getWidth(), this.getHeight());
    	Font font = new Font("宋体", Font.BOLD, 18);
    	g.setFont(font);
    	for(int i=0;i<=G.vexnum+1;i++) {
    		g.setColor(Color.BLACK);
    		g.drawLine(a, b+length*i, a+3*length, b+length*i);
    		if(i<G.vexnum) {
    	    	g.setColor(Color.RED);
    			g.drawString(G.vexname[i], a+length/4, b+length*(i+2)-length/4);
    		}
    	}
    	g.setColor(Color.BLACK);
    	for(int i=0;i<=3;i++) {
    		g.drawLine(a+i*length, b, a+i*length, d);
    	}
    	g.drawString("ve", a+length+length/4, b+length*3/4);
    	g.drawString("vl", a+2*length+length/4, b+length*3/4);
    	font = new Font("宋体", Font.BOLD, 16);
    	g.setFont(font);
    	g.drawString("顶点", a, b+length*3/4);    
    	
    	if(parent.panel_paint.num1!=0)
    		drawNum(parent.vexlist_2,parent.panel_paint.num,parent.panel_paint.num1,g);
    }
    
    public void drawNum(int[][] vex_list, int num, int num1, Graphics g) {		//		绘制各顶点活动的ve、vl值
    	Color color_old=g.getColor();
    	Font font_old=g.getFont();
    	
    	Font font = new Font("宋体", Font.BOLD, 18);
    	int min=vex_list[parent.vexlist_1[0]][0], max=vex_list[parent.vexlist_1[parent.vexlist_1.length-1]][0];
    	g.setFont(font);
    	for(int i=0;i<G.vexnum;i++) {
    		if(num1==1) {
    			if(i<num) {
    				g.setColor(Color.BLUE);
    				g.drawString(String.valueOf(vex_list[parent.vexlist_1[i]][0]), a+length+length/4, b+length*(parent.vexlist_1[i]+2)-length/4);
    			}
    			else {
    				g.setColor(Color.RED);
    				g.drawString(String.valueOf(min), a+length+length/4, b+length*(parent.vexlist_1[i]+2)-length/4);
    			}
    		}
    		else if(num1==2) {
    			g.setColor(Color.BLUE);
    			g.drawString(String.valueOf(vex_list[parent.vexlist_1[i]][0]), a+length+length/4, b+length*(parent.vexlist_1[i]+2)-length/4);
    			if(i<num) {
    				g.setColor(Color.RED);
    				g.drawString(String.valueOf(max), a+2*length+length/4, b+length*(parent.vexlist_1[i]+2)-length/4);
    			}
    			else {
    				g.setColor(Color.BLUE);
    				g.drawString(String.valueOf(vex_list[parent.vexlist_1[i]][1]), a+2*length+length/4, b+length*(parent.vexlist_1[i]+2)-length/4);
    			}
    		}
    	}
    	g.setFont(font_old);
    	g.setColor(color_old);
    }
    
}
