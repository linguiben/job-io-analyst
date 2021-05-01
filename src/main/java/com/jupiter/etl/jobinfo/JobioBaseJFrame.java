package com.jupiter.etl.jobinfo;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;
import com.jgraph.layout.organic.JGraphOrganicLayout;
import com.jgraph.layout.tree.JGraphCompactTreeLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;
import com.jgraph.layout.tree.JGraphTreeLayout;
import com.jupiter.etl.jobinfo.entities.Job;
import com.jupiter.etl.jobinfo.entities.JobEdge;
import com.jupiter.etl.schedule.Monitor;
import com.jupiter.mybatis.dao.DBUnit;
import com.jupiter.mybatis.po.User;

/**
 * @Description: 
 * @version: 
 * @author: Jupiter.Lin 2021-4-23 下午8:35:23
 */
public abstract class JobioBaseJFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public DesignerGraph graph;
	public User user = new User();
	public javax.swing.JScrollPane jScrollPane1;
	List<String> strNotExists = new ArrayList<String>(); //记录输入错误(不存在的job/file)
	
	public void gotoFocus(JScrollPane jScrollPane1,DesignerGraph graph) {
		jScrollPane1.getHorizontalScrollBar().setValue((int) (graph.getFocusX() * graph.getScale() - jScrollPane1.getSize().width / 3));
		jScrollPane1.getVerticalScrollBar().setValue((int) (graph.getFocusY() * graph.getScale() - jScrollPane1.getSize().height / 2));
		//System.out.println("w:"+(graph.getFocusX() * graph.getScale() - jScrollPane1.getSize().width / 3)+"\t"+"h:"+((int) (graph.getFocusY() * graph.getScale() - jScrollPane1.getSize().height / 2)));
		//System.out.println("Width:"+jScrollPane1.getHorizontalScrollBar().getValue()+"\t"+"Height"+jScrollPane1.getVerticalScrollBar().getValue());
	}

	//测试
	public void sampleAnalyze(DesignerGraph graph) {
		ArrayList<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
		Job jCell1 = new Job("start", 1, "");
		Job jCell2 = new Job("1234", 1, "");
		JobEdge jEdge = new JobEdge("", "");
		cells.add(jCell1);
		cells.add(jCell2);
		cells.add(jEdge);
		GraphConstants.setBounds(jCell1.getAttributes(), new Rectangle2D.Double(20, 20, 35, 20));
		GraphConstants.setBounds(jCell2.getAttributes(), new Rectangle2D.Double(80, 80, 35, 20));
		jEdge.setSource(jCell1.getChildAt(0));
		jEdge.setTarget(jCell2.getChildAt(0));
		GraphConstants.setLineEnd(jEdge.getAttributes(), GraphConstants.ARROW_CLASSIC);
		graph.getGraphLayoutCache().insert(cells.toArray());
		//graph.getGraphLayoutCache().insert(c2.toArray());
	}
	
	//帮助
		public void showHelper() {
			/*Object[] group = graph.getSelectionCells();
			graph.getGraphLayoutCache().collapse(group);
			graph.getGraphLayoutCache().update();
			graph.getGraphLayoutCache().reload();*/

			JOptionPane.showMessageDialog(null, "注：jobname留空则为血缘分析，否则为影响分析\n\n" //
					+ "快捷键:\n" //
					+ "复制：ctrl+鼠标拖曳\n" //
					+ "缩放：ctrl+滚轮\n" //
					+ "强选：alt+左键\n" //
					+ "删除：del                    带连接删除:shift+del\n"//
					+ "消色：双击\n" //
					+ "调度状态(jobstatus): 1000-排队(gray) 100-ready(white) 0-开始(running/blue) \n"
					+ "                    1-成功(green) 2-警告(yellow) 3-失败(red) 4-失败跳过(orange)\n" + "地方太小写不下了...\n"//
					+ "若布局较乱，多尝试几次其他布局方式。\n" //
					+ "\n作者：︶ㄣ木木ヾ王子,2018-01,V2.2", "jbio......", -1);
		}
		
		public List<String> getNotExistsJobs(String stype,String jobstrs){
			String txt = jobstrs.replace(" ", "");
			String[] ls = jobstrs.replace(" ", "").split(",");
			strNotExists.clear();
			strNotExists = new ArrayList<String>(Arrays.asList(ls));
			strNotExists.removeAll(DBUnit.checkNameEixts(stype, txt));
			return strNotExists;
		}
		
		//启动调度窗口
		public void showMonitor() {
			Monitor monitor = new Monitor();
			monitor.setLocationRelativeTo(getParent());
			monitor.setVisible(true);
		}
		
		//默认布局
		public void defaultLayout() {
			graph.defaultLayout();
			/*jScrollPane1.resize(1500, 3000);
			this.resize(1000, 500);*/
		}
		
		//重新布局
		public void rLayout(int arg) {
			JGraphFacade facade = new JGraphFacade(graph); // Pass the facade the JGraph instance
			JGraphLayout layout = null;
			JGraphTreeLayout treeLayout = null;
			JGraphCompactTreeLayout jctl = null;
			JGraphOrganicLayout jol = null;
			//System.out.println("arg = " + arg);
			switch (arg) {
			//new JGraphRadialTreeLayout();// new JGraphCompactTreeLayout();//new JGraphFastOrganicLayout(); 
			case 1:
				layout = new JGraphFastOrganicLayout();
				break; //快速
			case 2:
				jol = new JGraphOrganicLayout();
				jol.setApproxNodeDimensions(true);
				layout = jol;
				break; //有机
			case 3:
				treeLayout = new JGraphTreeLayout();
				treeLayout.setAlignment(SwingConstants.BOTTOM);
				treeLayout.setLevelDistance(80.0);
				treeLayout.setNodeDistance(2);
				//treeLayout.setRouteTreeEdges(true);
				layout = treeLayout;
				break; //树形布局
			case 4:
				layout = new JGraphRadialTreeLayout();
				break; //放射布局
			case 5:
				jctl = new JGraphCompactTreeLayout();
				jctl.setPositionMultipleTrees(true);
				layout = jctl;
				break; //简洁树形
			case 6:
				layout = new JGraphHierarchicalLayout();
				break; //曲线
			}
			layout.run(facade); // Run the layout on the facade. Note that layouts do not implement the Runnable interface, to avoid confusion
			Map<?, ?> nested = facade.createNestedMap(true, true); // Obtain a map of the resulting attribute changes from the facade
			graph.getGraphLayoutCache().edit(nested); // Apply the results to the actual graph
		}
		
		
		
}
