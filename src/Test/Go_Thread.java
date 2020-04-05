package Test;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**********************������λ��Facade����*************************/

public class Go_Thread extends Thread {				/**************���޸�****************/
	private DisplayGUI parent;

	Go_Thread(DisplayGUI p) {
		this.parent = p;
	}

	@Override
	public void run() {// ����һ���̲߳�����
		this.suspend();
		while (true) {
			this.parent.step = this.parent.panel_paint.proceed(this.parent.step);	// ����ִ��
			this.parent.showSource();	//	����������
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
						JOptionPane.showMessageDialog(parent.frame, parent.panel_paint.result, "���", JOptionPane.INFORMATION_MESSAGE);
						
					}
				});
				this.suspend();
			}
		}
	}
}
