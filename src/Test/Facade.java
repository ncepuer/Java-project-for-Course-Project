package Test;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

/**********************主方法位于Facade类中(本类底部)**************************/

/**
 * @classname:DisplayGUI
 * @description:欢迎界面可选择图的类型及储存结构。演示界面可设计动画和代码演示分侧排布的界面，并加入菜单栏，菜单栏中包含有关图的各种功能操作。
 * @author mi
 */
class DisplayGUI implements ActionListener {		//		演示界面
	/*	声明变量	*/
	Go_Thread thread_go;
	PaintGraph panel_paint;
	Critical_ps ps;
	Save1GUI save1;
	Save2GUI save2;
	
	JFrame frame;
	JMenuBar menubar;
	JMenu menu_main, menu_function, menu_help;
	JMenuItem main_close, main_return, main_reload, fun[] = new JMenuItem[9],help_about;
	JList<String> list_src;
	DefaultListModel<String> dlm_src;
	JPanel panel_up, panel_down, panel_leftup, panel_rightup, panel_down_left, panel_rightup_up, panel_rightup_down, panel_critical;
	JButton jb_next, jb_go, jb_reset, jb_down, jb_up;
	JTextField jtf_speed;
	Font font_src, font_basis;
	String[][] Code = new String[10][];

	MGraph mg_src=null,mg=null;
	AdjGraph ag_src=null,ag=null;
	boolean flag_huilu=false;			//是否存在回路的标志
	boolean flag_go = true;
	boolean flag_next_or_go=true;		//按下按钮的标志：true=next,false=go
	int selection = 0,step=0,speed=200;
	int[] vexlist_1=null;
	int[][] vexlist_2=null;
	int begin=0,end=0;
	
	public DisplayGUI() {}
	
	public DisplayGUI(MGraph mg,AdjGraph ag) {
		this.Init_Code();
		this.Init_GUI();
		
		this.mg_src=mg;
		this.ag_src=ag;
		this.mg=this.mg_src.clone();
		this.ag=this.ag_src.clone();

		panel_paint=new PaintGraph(this);         //图的动画
		save1 = new Save1GUI(this); 
		save2=new Save2GUI(this);
		
		panel_rightup_up.add(panel_paint);
		panel_rightup_up.add(save1);
		panel_rightup_down.add(save2,BorderLayout.CENTER);
		
		thread_go=new Go_Thread(this);
		thread_go.start();
		
		frame.setVisible(true);
	}
	
	private void Init_GUI() {			//		演示界面初始化
		frame = new JFrame("演示界面 (点击'菜单-返回'回到欢迎界面)");
		frame.setSize(1200, 800);
		dlm_src = new DefaultListModel<String>();
		dlm_src.setSize(20);
		list_src = new JList<String>(dlm_src);
		list_src.setVisibleRowCount(25);
		this.setSource();
		jtf_speed = new JTextField(5);
		jtf_speed.setText(Integer.toString(speed));
		jtf_speed.setEditable(false);
		font_src = new Font("Times New Roman", Font.BOLD, 16);
		list_src.setFont(font_src);
		JScrollPane jsp_src = new JScrollPane(list_src);
		jb_next = new JButton("单步执行");
		jb_go = new JButton("连续执行");
		jb_reset = new JButton("重置");
		jb_up = new JButton("+");
		jb_down = new JButton("-");
		font_basis = new Font("宋体", Font.BOLD, 20);
		jb_next.setFont(font_basis);
		jb_go.setFont(font_basis);
		jb_reset.setFont(font_basis);

		menubar = new JMenuBar();
		menu_function = new JMenu("功能");
		menu_main = new JMenu("菜单");
		menu_help = new JMenu("帮助");
		main_reload = new JMenuItem("重新加载");
		main_return = new JMenuItem("返回");
		main_close = new JMenuItem("关闭");
		fun[0] = new JMenuItem("深度优先遍历");
		fun[1] = new JMenuItem("广度优先遍历");
		fun[2] = new JMenuItem("判断无向图是否有环");
		fun[3] = new JMenuItem("哈密尔顿链");
		fun[4] = new JMenuItem("最小生成树-Prim");
		fun[5] = new JMenuItem("最小生成树-Kruskal");
		fun[6] = new JMenuItem("最短路径-Dijkstra");
		fun[7] = new JMenuItem("判断有向图是否有回路");
		fun[8] = new JMenuItem("关键路径");
		help_about = new JMenuItem("关于");
		menubar.add(menu_main);
		menubar.add(menu_function);
		menubar.add(menu_help);
		menu_main.add(main_reload);
		menu_main.add(main_return);
		menu_main.addSeparator();
		menu_main.add(main_close);
		for (int i = 0; i < 9; i++)
			{	if(i==2)	continue;
			menu_function.add(fun[i]);	fun[i].setFont(font_basis);	}
		menu_help.add(help_about);
		menu_main.setFont(font_basis);
		menu_function.setFont(font_basis);
		menu_help.setFont(font_basis);
		main_reload.setFont(font_basis);
		main_return.setFont(font_basis);
		main_close.setFont(font_basis);
		help_about.setFont(font_basis);

		panel_up = new JPanel(new BorderLayout());
		panel_up.setBorder(new TitledBorder("演示区"));
		panel_down = new JPanel(new GridLayout(1, 4));
		panel_down.setBorder(new TitledBorder("操作区"));
		panel_down.setPreferredSize(new Dimension(0, 60));
		panel_down_left = new JPanel(new GridLayout(1,3));
		panel_leftup = new JPanel();
		panel_leftup.setBorder(new TitledBorder("核心代码区"));
		panel_leftup.setPreferredSize(new Dimension(450, 0));
		panel_rightup = new JPanel(new BorderLayout());
		panel_rightup.setBorder(new TitledBorder("动画演示区"));
		panel_rightup_up=new JPanel(new GridLayout(1,2));
		panel_rightup_down = new JPanel(new BorderLayout());
		panel_rightup_down.setPreferredSize(new Dimension(0, 285));
		
		frame.setLayout(new BorderLayout());
		frame.add(panel_up, BorderLayout.CENTER);
		frame.add(panel_down, BorderLayout.SOUTH);
		frame.setJMenuBar(menubar);

		panel_up.add(panel_leftup, BorderLayout.WEST);
		panel_up.add(panel_rightup, BorderLayout.CENTER);
		panel_down_left.add(jb_down);
		panel_down_left.add(jtf_speed);
		panel_down_left.add(jb_up);
		panel_down.add(panel_down_left);
		panel_down.add(jb_reset);
		panel_down.add(jb_next);
		panel_down.add(jb_go);
		panel_leftup.add(jsp_src);
		panel_rightup.add(panel_rightup_up,BorderLayout.CENTER);
		panel_rightup.add(panel_rightup_down,BorderLayout.SOUTH);


		main_reload.addActionListener(this);
		main_return.addActionListener(this);
		main_close.addActionListener(this);
		for (int i = 0; i < 9; i++)
			fun[i].addActionListener(this);
		help_about.addActionListener(this);
		jb_reset.addActionListener(this);
		jb_next.addActionListener(this);
		jb_go.addActionListener(this);
		jb_up.addActionListener(this);
		jb_down.addActionListener(this);
		
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == main_return) {
			frame.setVisible(false);
			new WelcomeGUI();
			frame.dispose();
		} else if (e.getSource() == main_reload) {
			frame.setVisible(false);
			frame.dispose();
			new DisplayGUI(this.mg_src,this.ag_src);
		} else if (e.getSource() == main_close) {
			System.exit(0);
		} else if (e.getSource() == help_about) {
			JOptionPane.showMessageDialog(frame, "<html><body><p align=\"center\"> 陈凯发 杜若聪 黎汶杰 杨炀<br>计算1801班", "华北电力大学", JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == fun[0]) {
			selection = 1;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			mg.DFS(0);
			vexlist_2=mg.dfs_result;
			for(int i=0;i<vexlist_2.length;i++) {
				for(int j=0;j<vexlist_2[i].length;j++)
					if(vexlist_2[i][j]!=-1)
						System.out.println(vexlist_2[i][j]+" ");
				System.out.println();
			}
			this.setSource();
			showSource();
		} else if (e.getSource() == fun[1]) {
			selection = 2;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			mg.BFS(0);
			vexlist_2=mg.bfs_result;
			for(int i=0;i<vexlist_2.length;i++) {
				for(int j=0;j<vexlist_2[i].length;j++)
					if(vexlist_2[i][j]!=-1)
						System.out.println(vexlist_2[i][j]+" ");
				System.out.println();
			}
			this.setSource();
			showSource();
		} else if (e.getSource() == fun[2]) {
			if(this.ag.type==2)
			{
				JOptionPane.showMessageDialog(frame, "请创建无向图！", "错误", JOptionPane.WARNING_MESSAGE);
				return;
			}
			selection = 3;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			this.setSource();
			showSource();
		} else if (e.getSource() == fun[3]) {
			selection = 4;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			vexlist_1=mg.Hamilton(mg);
			for(int i=0;i<vexlist_1.length;i++)
				System.out.println(vexlist_1[i]);
			this.setSource();
			showSource();
		} else if (e.getSource() == fun[4]) {
			selection = 5;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			mg.prim();
			vexlist_2=mg.prim_result;
			for(int i=0;i<vexlist_2.length;i++)
				System.out.println(vexlist_2[i][0]+","+vexlist_2[i][1]);
			this.setSource();
			showSource();
		} else if (e.getSource() == fun[5]) {
			selection = 6;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			vexlist_2=mg.Kruskal(mg);
			this.setSource();
			showSource();
		} else if (e.getSource() == fun[6]) {
			selection = 7;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			begin=0;
			vexlist_2=MGraph.Dijkstra(mg, begin);
			end=vexlist_2.length-1;
			this.setSource();
			showSource();
		} else if (e.getSource() == fun[7]) {
			if(this.ag.type!=2)
			{
				JOptionPane.showMessageDialog(frame, "请创建有向图！", "错误", JOptionPane.WARNING_MESSAGE);
				return;
			}
			selection = 8;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			vexlist_1=new int[ag.vexnum];
			for(int i=0;i<vexlist_1.length;i++)	vexlist_1[i]=-1;
			flag_huilu=AdjGraph.top_loop(ag, vexlist_1);
			this.setSource();
			showSource();
		} else if (e.getSource() == fun[8]) {
			if(this.ag.type!=2)
			{
				JOptionPane.showMessageDialog(frame, "请创建有向图！", "错误", JOptionPane.WARNING_MESSAGE);
				return;
			}
			selection = 9;
			vexlist_1=null;
			vexlist_2=null;
			this.reset();
			vexlist_1=new int[ag.vexnum];
			for(int i=0;i<vexlist_1.length;i++)	vexlist_1[i]=-1;
			flag_huilu=AdjGraph.top_loop(ag, vexlist_1);
			if(flag_huilu)
			{
				JOptionPane.showMessageDialog(frame, "此有向图存在回路！", "错误", JOptionPane.WARNING_MESSAGE);
				return;
			}
			ag=ag_src.clone();
			vexlist_2=AdjGraph.critical_path(ag);
			for(int i=0;i<ag.vexnum;i++)
				System.out.println(vexlist_2[i][0]+"  "+vexlist_2[i][1]);
			
			ps=new Critical_ps(this);
			panel_critical=new JPanel(new GridLayout());
			panel_critical.setPreferredSize(new Dimension(170, 0));
			panel_critical.add(ps);
			panel_rightup_down.add(panel_critical,BorderLayout.EAST);
			frame.setVisible(true);
			
			this.setSource();
			showSource();
		} else if (e.getSource() == jb_down) {
			speed-=100;
			jtf_speed.setText(Integer.toString(speed));
			if(speed<=0)	jb_down.setEnabled(false);
		} else if (e.getSource() == jb_up) {
			speed+=100;
			jtf_speed.setText(Integer.toString(speed));
			if(speed>0)	jb_down.setEnabled(true);
		} else if (e.getSource() == jb_reset) {
			this.reset();
			this.setSource();
		} else if (e.getSource() == jb_next) {
			flag_next_or_go=true;
			if(step==0) {
				this.panel_paint.reset();
				this.save2.repaint();
				if(this.selection==9 && this.panel_rightup_down.getComponentCount()>1)
					this.ps.repaint();
			}
			step=panel_paint.proceed(step);
			if(step==0) {
				JOptionPane.showMessageDialog(frame, this.panel_paint.result, "结果", JOptionPane.INFORMATION_MESSAGE);
			}
			showSource();
		} else if (e.getSource() == jb_go) {
			flag_next_or_go=false;
			set_input(!flag_go);
			if(step==0) {
				this.panel_paint.reset();
				this.save2.repaint();
				if(this.selection==9 && this.panel_rightup_down.getComponentCount()>1)
					this.ps.repaint();
			}
			if (flag_go == true) {
				thread_go.resume();
			}
			else {
				thread_go.suspend();
			}
			flag_go = !flag_go;
		}

	}
	
	public void reset() {		//		重置界面各组件的状态
		this.mg=this.mg_src.clone();
		this.ag=this.ag_src.clone();
		this.flag_go=true;
		this.flag_next_or_go=true;
		this.step=0;
		this.speed=200;
		this.set_input(true);
		this.jtf_speed.setText(Integer.toString(speed));
		this.list_src.clearSelection();
		if(this.selection!=9 && this.panel_rightup_down.getComponentCount()>1)
		{
			this.panel_rightup_down.remove(panel_rightup_down.getComponentCount()-1);
			this.frame.setVisible(true);
		}
		if(this.selection==9 && this.panel_rightup_down.getComponentCount()>1)
			this.ps.repaint();
		this.panel_paint.reset();
		this.save2.repaint();
		this.thread_go=new Go_Thread(this);
		thread_go.start();
	}
	
	public void set_input(boolean flag) {		//		设置按钮的状态
		if(flag) {
			jb_go.setText("连续执行");
			jb_reset.setEnabled(true);
			jb_next.setEnabled(true);
			jb_up.setEnabled(true);
			jb_down.setEnabled(true);
		}
		else {
			jb_go.setText("暂停");
			jb_reset.setEnabled(false);
			jb_next.setEnabled(false);
			jb_up.setEnabled(false);
			jb_down.setEnabled(false);
		}
	}

	public void setSource() {		//	根据selection的值来设置展示的算法
		dlm_src.clear();
		for (int i = 0; i < Code[selection].length; i++)
			dlm_src.addElement(Code[selection][i]);
		list_src.setModel(dlm_src);
	}
	
	public void showSource() {		//	对应的代码行高亮
		list_src.setSelectedIndex(step);
	}
	
	public void showSource(int row) {
		list_src.setSelectedIndex(row);
	}

	private void Init_Code() {		//	代码的存储初始化
		Code[0] = new String[] {
				"  Ready...                                                                      " };
		
		Code[1] = new String[] { "  DFS:                                                         ",
				"void traver(Mgraph g) {", 
				"        int visit[maxsize];", 
				"        for(int i=0;i<g.vexnum;i++)",
				"                visit[i]=0;", 
				"        for(int k=0;k<g.vexnum;k++)", 
				"                if(visit[k]==0)",
				"                        df_traver(g,k,visit);", 
				"}", 
				"void df_traver(Mgraph g,int i,int visit[]) {",
				"        printf(\"%c\",g.vex[i]); visit[i]=1;", 
				"        for(int j=0;j<g.vexnum;j++)",
				"                if(g.arcs[i][j]>0 && g.arcs[i][j]<1000 && visit[j]==0)",
				"                        df_traver(g,i,visit);", 
				"}", };
		
		Code[2] = new String[] { "  BFS:                                                         ",
				"void BFS(MGraph g,int x) {", 
				"        for(int i=0;i<vexnum;i++)", 
				"                bfs_visit[i]=0;",
				"        for(int i=0;i<vexnum;i++)", 
				"                if(bfs_visit[i]==0)",
				"                        bf_travel(g,i,bfs_visit);", 
				"}",
				"void bf_travel(MGraph g,int v,int bfs_visit[]) {", 
				"        int x; queue q;",
				"        bfs_visit[v]=1; bfs_result=bfs_result+vex[v]+\"\\t\";", 
				"        initQueue(q);  EnQueue(q,v);",
				"        while(QueueEmpty(q)!=null) {", 
				"                x=Dequeue(q);",
				"                for(int i=0;i<vexnum;i++)",
				"                        if((arc[x][i]!=0||arc[i][x]!=0)&&bfs_visit[i]!=0) {",
				"                                bfs_result=bfs_result+vex[i]+\"\\t\";",
				"                                EnQueue(q);", 
				"                                bfs_visit[i]=1;",
				"                        }", 
				"        }", 
				"}" };
		
		Code[3] = new String[] { "  DFS_Huan:                                                     ",
				"public void dfs_huan(MGraph G) {", 
				"        int i=0,visit[]=new int[G.vexnum];",
				"        for(int i=0;i<G.vexnum;i++)", 
				"                visit[i]=0;", 
				"        dfs_(G,visit,i);", "}",
				"public void dfs_(MGraph G,int[] visit,int i) {", 
				"        visit[i]=1;", 
				"        int di=0,int vi=0; ",
				"        for(int j=0;j<G.vexnum;j++) {", 
				"                if(G.arc[i][j]!=0 && G.arc[i][j]!=inf) {",
				"                        di++;", 
				"                        if(visit[j]==1)",
				"                                vi++;", 
				"                }", 
				"        }",
				"        if(vi==di && di!=1) {", 
				"                System.out.println(\"There is a Huan!\");  return;",
				"        }", 
				"        for(int j=0;j<G.vexnum;j++) {",
				"                if(G.arc[i][j]!=0 && G.arc[i][j]!=inf && visit[j]==0)",
				"                        dfs_(G,visit,j);", 
				"        }", 
				"}" };
		
		Code[4] = new String[] { "  Hamilton:                                                    ",
				"void Hamilto(MGraph g,int v0) {", 
				"        int n=0,visited[MAX]={0},path[MAX];",
				"        Dfs(g,v0,&n,visited,path);", 
				"}", 
				"void Dfs(MGraph g,int v,int *n,int visited[],int path[]) {",
				"        visited[v]=1;", 
				"        path[(*n)++]=v;", 
				"        if(*n==g.vexnum) {",
				"                printf(\"\\n\");", 
				"                for(i=0;i<g.vexnum;i++)",
				"                        printf(\"%c \",g.vex[path[i]]);", 
				"        }",
				"        for(i=0;i<g.vexnum;i++)",
				"                if(visited[i]==0 && g.arc[v][i]!=0 && g.arc[v][i]<inf)",
				"                        Dfs(g,i,n,visited,path);", 
				"        visited[v]=0;  (*n)--;", 
				"}" };
		
		Code[5] = new String[] { "  Prim:                                                        ",
				"void prim(MGraph g) {", 
				"        int min,i,j,k;", 
				"        for(i=0;i<g.vexnum;i++) {",
				"                low[i].vi=0;", "                low[i].weigth=g.arcs[0][i];", 
				"        }",
				"        for(i=0;i<g.vexnum;i++) {", 
				"                min=inf;",
				"                for(j=0;j<g.vexnum;j++)",
				"                        if(low[j].weigth<min&&low[j].weigth!=0) {",
				"                                min=low[j].weigth;	k=j;", 
				"                        }",
				"                low[k].weigth=0;", 
				"                for(j=0;j<g.vexnum;j++)",
				"                        if(g.arcs[k][j]<low[j].weigth) {",
				"                                low[j].weigth=g.arcs[k][j];",
				"                                low[j].vi=k;", 
				"                        }", 
				"        }", 
				"}" };
		
		Code[6] = new String[] { "  Kruskal:                                                     ",
				"void kruskal(MGraph g) {", 
				"        int vs[MAX],visited[MAX][MAX]={0};",
				"        Edge e0,*e=(Edge *)malloc(g.arcnum*sizeof(Edge));", 
				"        for(v=0,i=0;v<g.vexnum;v++) {",
				"                for(w=0;w<g.vexnum;w++)",
				"                        if(g.arc[v][w]!=0 && g.arc[v][w]<inf && visited[v][w]==0) { ",
				"                                e[i].v=v;e[i].w=w;e[i].weigth=g.arc[v][w];i++;",
				"                                visited[v][w]=1;visited[w][v]=1;", 
				"                        }",
				"        }", 
				"        for(i=0;i<g.arcnum;i++)", 
				"                for(j=0;j<g.arcnum-i-1;j++)",
				"                        if(e[j].weigth>e[j+1].weigth) {",
				"                                e0=e[j];  e[j]=e[j+1];  e[j+1]=e0;", 
				"                        }",
				"        for(i=0;i<g.vexnum;i++)", 
				"                vs[i]=i;",
				"        for(j=0,k=1;k<g.vexnum;j++) {",
				"                s1=vs[e[j].v]; s2=vs[e[j].w];",
				"                if(s1!=s2) {",
				"                        printf(\"%c-%c......%d\\n\",g.vex[e[j].v],g.vex[e[j].w],e[j].weigth);",
				"                        k++;",
				"                        for(i=0;i<g.vexnum;i++)",
				"                        if(vs[i]==s2)  vs[i]=s1;",
				"                }", 
				"        }", 
				"}" };
		
		Code[7] = new String[] { "  Dijkstra:                                                    ",
				"void dijkstra(MGraph g,int v) {", 
				"        int i,j,k,min,flag=true,dist[MAX],path[MAX][MAX];",
				"        for(i=0;i<g.vexnum;i++)", 
				"                for(j=0;j<g.vexnum;j++)",
				"                        path[i][j]=-1;", 
				"        for(i=0;i<g.vexnum;i++) {",
				"                dist[i]=g.arc[v][i];", 
				"                if(g.arc[v][i]!=0 && g.arc[v][i]<inf) {",
				"                        path[i][0]=v;	path[i][1]=i;", 
				"                }", 
				"        }",
				"        while(flag) {", 
				"                min=inf;", 
				"                for(i=0;i<g.vexnum;i++)",
				"                        if(dist[i]!=0 && dist[i]<min) {",
				"                                k=i;min=dist[i];", 
				"                        }",
				"                for(i=0;i<g.vexnum;i++)", 
				"                        if(dist[k]+g.arc[k][i]<dist[i]) {",
				"                                dist[i]=dist[k]+g.arc[k][i];",
				"                                for(j=0;j<g.vexnum;j++)  path[i][j]=path[k][j];",
				"                                for(j=0;j<g.vexnum&&path[i][j]!=-1;j++)  ;",
				"                                path[i][j]=i;", 
				"                        }",
				"                dist[k]=0;  flag=false;", 
				"                for(i=0;i<g.vexnum;i++)",
				"                        if(dist[i]!=0 && dist[i]<inf)", 
				"                                flag=true;",
				"        }", 
				"}" };
		
		Code[8] = new String[] { "  Top_HuiLu:                                                  ",
				"int top_huilu(Graph g) {	", 
				"        int i,v,w,n=0;", 
				"        ArcNode *p;",
				"        for(i=0;i<g.vexnum;i++)", 
				"                if(g.Alist[i].num==0)",
				"                        push(&S,i);", 
				"        while(!Empty(S)) {",
				"                pop(&S,&v);	n++;", 
				"                p=g.Alist[v].firstarc;",
				"                while(p) {", 
				"                        w=p->vex;	g.Alist[w].num--;",
				"                        if(g.Alist[w].num==0)", 
				"                                push(&S,w);",
				"                        p=p->nextarc;", 
				"                }", 
				"        }",
				"        if(n<g.vexnum)  return 0;", 
				"        else  return 1;", 
				"}" };
		
		Code[9] = new String[] {
				"  Critical Path:                                                               ",
				"void critical_path(Graph g) {	", 
				"        int i,v,w,ve[MAX],vl[MAX]; ", 
				"        ArcNode *p;",
				"        for(i=0;i<g.vexnum;i++)", 
				"                ve[i]=0;	", 
				"        for(i=0;i<g.vexnum;i++)",
				"                if(g.Alist[i].num==0)", 
				"                        push(&S1,i);",
				"        while(!Empty(S1)) {	", 
				"                pop(&S1,&v);  push(&S2,v);",
				"                p=g.Alist[v].firstarc;", 
				"                while(p!=NULL) {	",
				"                        w=p->vex;  g.Alist[w].num--;	",
				"                        if(g.Alist[w].num==0)", 
				"                                push(&S1,w);	",
				"                        if(ve[w]<ve[v]+p->weight)",
				"                                ve[w]=ve[v]+p->weight;", 
				"                        p=p->nextarc;",
				"                }	", 
				"        }	", 
				"        for(i=0;i<g.vexnum;i++)",
				"                vl[i]=ve[g.vexnum-1];	", 
				"        while(!Empty(S2)) {	",
				"                pop(&S2,&v);  p=g.Alist[v].firstarc;	", 
				"                while(p!=NULL)	{	",
				"                        w=p->vex;	", 
				"                        if(vl[v]>vl[w]-p->weight)",
				"                                vl[v]=vl[w]-p->weight;	", 
				"                        p=p->nextarc;",
				"                }	", 
				"        }	", 
				"        for(i=0;i<g.vexnum;i++)",
				"                if(ve[i]==vl[i])",
				"                        printf(g.Alist[i].data)",
				"}" };
	}


}

class My_frame extends JFrame {
	 public My_frame(String title) {
		 this.setTitle(title);
		 setBak(); //调用背景方法
		 Container c = getContentPane(); //获取JFrame面板
	    JPanel jp = new JPanel(); //创建个JPanel
	    jp.setOpaque(false); //把JPanel设置为透明 这样就不会遮住后面的背景 这样就能在JPanel随意加组件了
	    c.add(jp);
	    setSize(1200, 800);
	    setVisible(true);    
	    }

	 public void setBak(){
	    ((JPanel)this.getContentPane()).setOpaque(false);
	    ImageIcon img = new ImageIcon(getClass().getResource("/photo.jpg")); //添加图片
	    JLabel background = new JLabel(img); 
	    this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
	    background.setBounds(0, 0, 1200,800);
	 }
	}

class WelcomeGUI implements ActionListener {		//		欢迎界面
	JFrame welcomeFrame;
	JLabel labelText, labelAuthor;
	JPanel downpanel;

	JRadioButton jrb_yes,jrb_no;
	JLabel lbnode,lbedge;
	JTextField tfnode,tfedge;
	JButton jb_input,jb_done;
	JPanel tffield,nodepanel,edgepanel,labelpanel;
	
	int num=0;
	String[][] edge_String=new String[20][];
	Edge[] edge=null;
	String[] vexname=null;

	WelcomeGUI() {

		welcomeFrame = new My_frame("欢迎使用");
		welcomeFrame.setSize(1200, 800);
		labelText = new JLabel();
		labelText.setText("图的存储与操作动画演示系统");
		Font font = new Font("华文彩云",1, 66);
		labelText.setFont(font);
		labelText.setHorizontalAlignment(JLabel.CENTER); // 文本居中
		labelAuthor = new JLabel();

		labelAuthor.setText("<html><body><p align=\"center\">作者信息 : 陈凯发 杜若聪 黎汶杰 杨炀<br>华北电力大学 计算1801班");
		labelAuthor.setFont(new Font("幼圆", 1, 17));
		labelAuthor.setHorizontalAlignment(JLabel.CENTER);
		labelAuthor.setOpaque(false);

		downpanel = new JPanel();
		downpanel.setLayout(new BorderLayout(2, 10));

		jb_input=new JButton("输入");jb_input.addActionListener(this);
		jb_input.setBackground(null);
		jb_input.setOpaque(false);
		jb_done=new JButton("完成");jb_done.addActionListener(this);
		tfnode=new JTextField(30);
		tfedge=new JTextField(30);
		tfnode.setOpaque(false);
		tfedge.setOpaque(false);
		lbnode=new JLabel("结点信息：");
		lbedge=new JLabel("边的权值：");
		jrb_yes=new JRadioButton("有向图");
		jrb_no=new JRadioButton("无向图");
		ButtonGroup bg=new ButtonGroup();
		bg.add(jrb_yes);	bg.add(jrb_no);
		jrb_no.setSelected(true);
		jrb_yes.setOpaque(false);
		jrb_no.setOpaque(false);
		
		tffield=new JPanel(new BorderLayout());
		nodepanel=new JPanel(new FlowLayout());
		edgepanel=new JPanel(new FlowLayout());
		labelpanel=new JPanel(new FlowLayout());
		nodepanel.add(lbnode);
		nodepanel.add(tfnode);
		nodepanel.add(jrb_yes);
		nodepanel.add(jb_input);
		nodepanel.setOpaque(false);
		edgepanel.add(lbedge);
		edgepanel.add(tfedge);
		edgepanel.add(jrb_no);
		edgepanel.add(jb_done);
		edgepanel.setOpaque(false);
		tffield.add(nodepanel,BorderLayout.CENTER);
		tffield.add(edgepanel,BorderLayout.SOUTH);
		tffield.setOpaque(false);
		labelpanel.add(labelAuthor);
		labelpanel.setOpaque(false);
		
		edgepanel.setPreferredSize(new Dimension(0, 40));
		
		downpanel.add(tffield,BorderLayout.CENTER);
		downpanel.add(labelAuthor, BorderLayout.SOUTH);
		downpanel.setOpaque(false);

		welcomeFrame.setLayout(new BorderLayout());
		welcomeFrame.add(labelText, BorderLayout.CENTER);
		welcomeFrame.add(downpanel, BorderLayout.SOUTH);
		
		welcomeFrame.setResizable(false);
		welcomeFrame.setVisible(true);
		welcomeFrame.setLocationRelativeTo(null);
		welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb_input) {
			String str=tfedge.getText();
			str.trim();
			String[] newstr=str.split("\\s+");
			if(newstr.length==3)
				edge_String[num++]=newstr;
			tfedge.setText("");
		}
		else if(e.getSource()==jb_done) {
			MGraph mg;
			AdjGraph ag;	
			if(tfnode.getText().trim().equals("")) {
				edge = new Edge[8];
				edge[0] = new Edge(0, 1, 3);
				edge[1] = new Edge(0, 2, 3);
				edge[2] = new Edge(1, 3, 2);
				edge[3] = new Edge(1, 4, 3);
				edge[4] = new Edge(2, 3, 3);
				edge[5] = new Edge(2, 5, 3);
				edge[6] = new Edge(3, 5, 2);
				edge[7] = new Edge(4, 5, 1);
				vexname = new String[] {"v1", "v2", "v3", "v4", "v5", "v6"};
			}
			else {
				String str_name=tfnode.getText();
				String str=tfedge.getText();
				str.trim();
				str_name.trim();
				vexname=str_name.split("\\s+");
				String[] newstr=str.split("\\s+");
				if(newstr.length==3)
					edge_String[num++]=newstr;
				edge=new Edge[num];
				for(int i=0;i<num;i++)
					edge[i]=new Edge(Integer.valueOf(edge_String[i][0]),Integer.valueOf(edge_String[i][1]),Integer.valueOf(edge_String[i][2]));
			}
			if(jrb_yes.isSelected()) {
				mg=new MGraph(vexname,edge,2);
				ag=new AdjGraph(vexname,edge,2);
				welcomeFrame.setVisible(false);
				new DisplayGUI(mg,ag);
				welcomeFrame.dispose();
			}
			else {
				mg=new MGraph(vexname,edge,1);
				ag=new AdjGraph(vexname,edge,1);
				welcomeFrame.setVisible(false);
				new DisplayGUI(mg,ag);
				welcomeFrame.dispose();
			}
		}
		
	}

}

public class Facade {

	public static void main(String[] args) {
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceDustLookAndFeel");		//	设置UI风格
				} catch (Exception e) {
					e.printStackTrace();
				}
				new WelcomeGUI();
			}
		});
	}

}
