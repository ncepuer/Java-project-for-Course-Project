package Test;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**********************主方法位于Facade类中*************************/

public class Go_Thread extends Thread {				/**************已修改****************/
	private DisplayGUI parent;

	Go_Thread(DisplayGUI p) {
		this.parent = p;
	}

	@Override
	public void run() {// 创建一个线程并运行
		this.suspend();
		while (true) {
			this.parent.step = this.parent.panel_paint.proceed(this.parent.step);	// 连续执行
			this.parent.showSource();	//	代码区高亮
			try {
				Thread.sleep(this.parent.speed);
			} catch (InterruptedException exception) {
				System.err.println("Exception:" + exception.toString());
			}
			if (this.parent.step == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException exception) {
					System.err.println("Exception:" + exception.toString());
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						parent.set_input(true);
						parent.panel_paint.validate();
						JOptionPane.showMessageDialog(parent.frame, parent.panel_paint.result, "结果", JOptionPane.INFORMATION_MESSAGE);
						
					}
				});
				this.suspend();
			}
		}
	}
}
