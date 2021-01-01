/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.BDH;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
/**
 *
 * @author BNC
 */
public class thongkehocluchocsinh {
  private static PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Học sinh giỏi", new Double(200));
        dataset.setValue("Học sinh khá", new Double(560));
        dataset.setValue("Học sinh trung bình", new Double(640));
        dataset.setValue("Các loại khác", new Double(30));
        return dataset;
    }

    private static JFreeChart createPieChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Thành tích học tập năm học 2018-2019".toUpperCase(),
                dataset, true, true, true);
        return chart;
    }

    public static void main(String[] args) {
        JFreeChart pieChart = createPieChart(createDataset());
        ChartPanel chartPanel = new ChartPanel(pieChart);
        JFrame frame = new JFrame();
        frame.add(chartPanel);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
