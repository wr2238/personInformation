package com.lu;

import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;


public class Panel extends JPanel {
	private String name;
	private int age;
	private boolean marital = true, succ = false;
	private JButton jbt1, jbt2, jbt3;
	private JFrame jf;
	private QueryDialog queryDialog;
	private InsertDialog insertDialog;
	private DeleteDialog deleteDialog;
	private TextSQL tsql;
	private ResultSet rs;
	private StringBuffer ErrorMessage;
	
	public Panel(JFrame jf) {
		this.jf = jf;
		setLayout(null);
		jbt1 = new JButton("增添信息");
		jbt1.setBounds(200, 30, 100, 30);
		add(jbt1);
		jbt1.addActionListener(new AL());
		jbt2 = new JButton("查询数据");
		jbt2.setBounds(200, 80, 100, 30);
		add(jbt2);
		jbt2.addActionListener(new AL());
		jbt3 = new JButton("删除记录");
		jbt3.setBounds(200, 130, 100, 30);
		add(jbt3);
		jbt3.addActionListener(new AL());
	}
	private void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
	}
	class AL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("查询数据")) {
				tsql = new TextSQL();
				rs = tsql.getQuery();
				if (rs == null) {
					String ErrorMessage = new String(tsql.getErrorMessage());
					showMessage("信息查询失败！\n" + ErrorMessage);
				} else {
					StringBuffer text = SQLParser.getText(rs);
					String info = new String(text);
					queryDialog = new QueryDialog("信息查询", info);
				}
			} else if (e.getActionCommand().equals("删除记录")) {
				deleteDialog = new DeleteDialog();
			}else if(e.getActionCommand().equals("增添信息")) {
				insertDialog = new InsertDialog();
			}
		}
	}
	class InputException extends Exception {
		InputException(String err) {
			super(err);
		}
	}
	class QueryDialog extends JDialog {
		private JTextArea jta;
		QueryDialog(String title, String info) {
			super(jf, title);
			JTextArea jta = new JTextArea(5, 4);
			add(new JScrollPane(jta));
			jta.setText(info);
			setBounds(470, 200, 400, 300);
			setModal(true);
			setVisible(true);
		}
	}
	class InsertDialog extends JDialog implements ActionListener, ItemListener {
		private JButton djbt1,djbt2;
		private JTextField jtf1, jtf2, jft3;
		private JLabel jl1, jl2, jl3;
		private JRadioButton jrb1, jrb2;
		private ButtonGroup gp;
		public InsertDialog() {
			jl1 = new JLabel("姓名：");
			jl2 = new JLabel("年龄：");
			jtf1 = new JTextField(5);
			jtf2 = new JTextField(5);
			jl3 = new JLabel("婚姻状况：");
			jrb1 = new JRadioButton("已婚");
			jrb2 = new JRadioButton("未婚");
			djbt1 = new JButton("确定");
			djbt2 = new JButton("取消");
			djbt1.addActionListener(this);
			djbt2.addActionListener(this);
			add(jl1);
			jl1.setBounds(150, 20, 80, 50);
			add(jtf1);
			jtf1.setBounds(200, 30, 100, 25);
			add(jl2);
			jl2.setBounds(150, 60, 80, 50);
			add(jtf2);
			jtf2.setBounds(200, 75, 100, 25);
			add(jl3);
			jl3.setBounds(125, 100, 80, 50);
			add(jl3);
			jrb1.setBounds(200, 100, 70, 50);
			add(jrb1);
			jrb2.setBounds(280, 100, 70, 50);
			add(jrb2);
			djbt1.setBounds(130,160,80,27);
			add(djbt1);
			djbt2.setBounds(240,160,80,27);
			add(djbt2);
			gp = new ButtonGroup();
			gp.add(jrb1);
			jrb1.setSelected(true);
			gp.add(jrb2);
			jrb1.addItemListener(this);
			jrb2.addItemListener(this);
			setLayout(null);
			setVisible(true);
			setBounds(420,200,470,260);
		}


		private boolean executeSQL() {
			tsql = new TextSQL();
			tsql.setInfo(name, age, marital);
			succ = tsql.insertSQL();
			return succ;
		}
		private boolean onlyChinese(String inputName) {
			String regex = "^[\\u4e00-\\u9fa5]*$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(inputName);
			if (matcher.find() == true) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String inputName = "";
			String inputAge = "";
			inputName = jtf1.getText();
			inputAge = jtf2.getText();
			if (e.getActionCommand().equals("确定")) {
				try {
					if (inputName == null || inputName.equals("")) {
						throw new InputException("姓名不能为空！");
					} else if (inputAge == null || inputAge.equals("")) {
						throw new InputException("年龄不能为空！");
					} else if (!onlyChinese(inputName)) {
						throw new InputException("姓名只能输入汉字！");
					} else if (inputName.length() > 5) {
						throw new InputException("名字过长，最多五个字！");
					} else {// 筛选合格，进入数据库执行阶段
						name = inputName;
						try {// 一有异常，停止执行
							age = Integer.parseInt(inputAge);
							if (age <= 0 || age >= 100) {
								throw new InputException("年龄必须在1~99之间!");
							}
							succ = executeSQL();
							if (succ == true) {
								showMessage("添加成功！");
							} else if (succ == false) {
								String ErrorMessage = new String(tsql.getErrorMessage());
								showMessage("信息查询失败！\n" + ErrorMessage);
								showMessage("执行失败！");
							}
						} catch (InputException err) {
							showMessage(err.getLocalizedMessage());
						} catch (NumberFormatException err) {
							showMessage("年龄必须是整数！");
						}
					}
				} catch (InputException err) {
					showMessage(err.getLocalizedMessage());
				}
			}else if(e.getActionCommand().equals("取消")) {
				setVisible(false);
			}
		}

		public void itemStateChanged(ItemEvent e) {
			if ((JRadioButton) e.getSource() == jrb1) {
				marital = true;
			} else if ((JRadioButton) e.getSource() == jrb2) {
				marital = false;
			}
		}
	}
	class DeleteDialog extends JDialog implements ActionListener {
		JLabel jl;
		JTextField djtf1;
		JButton jbt1, jbt2;
		DeleteDialog() {
			jl = new JLabel("请输入要删除的编号：");
			djtf1 = new JTextField(10);
			jbt1 = new JButton("确定删除");
			jbt1.addActionListener(this);
			jbt2 = new JButton("取消");
			jbt2.addActionListener(this);
			setLayout(new FlowLayout());
			add(jl);
			add(djtf1);
			add(jbt1);
			add(jbt2);
			setBounds(490, 300, 300, 130);
			setModal(true);
			setVisible(true);
		}
		private boolean deleteSQL(int id) {
			tsql = new TextSQL();
			boolean succ = tsql.deleteSQL(id);
			ErrorMessage = tsql.getErrorMessage();
			return succ;
		}
		public void actionPerformed(ActionEvent e) {
			String input = djtf1.getText();
			boolean deleteSucc = false;
			if (e.getActionCommand().equals("确定删除")) {
				try {
					int deleteId = Integer.parseInt(input);
					if (deleteId <= 0) {
						throw new InputException("输入数字不能小于1！");
					}else {
						deleteSucc = deleteSQL(deleteId);
						if(deleteSucc) {
							showMessage("删除成功！");
						}else {
							showMessage("删除失败！\n"+new String(ErrorMessage));
						}
					}
				} catch (NumberFormatException err) {
					showMessage("只能输入整数！");
				} catch (InputException err) {
					showMessage(err.getLocalizedMessage());
				}
			} else if (e.getActionCommand().equals("取消")) {
				setVisible(false);
			}
		}
	}
}
class JF extends JFrame {
	JF() {
		super("person db");
		Panel panel = new Panel(this);
		setContentPane(panel);
		setBounds(400, 250, 500, 220);
		setResizable(false);
		setVisible(true);
	}
}