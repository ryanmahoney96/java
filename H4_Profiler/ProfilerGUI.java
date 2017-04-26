
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;

//this will become a gui frame that displays all of the information tracked by the Profiler (the "report")
public class ProfilerGUI extends JFrame {

    //hold the hasmaps from the the Profiler
    private HashMap <String, ArrayList <Duration>> timers;
    private HashMap <String, Integer> counters;

    JPanel mainPanel;
    JTabbedPane tabPane;
    JPanel timersTab;
    JPanel countersTab;

    public ProfilerGUI (HashMap <String, ArrayList <Duration>> t, HashMap <String, Integer> c) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        super("Profiler Report");

        timers = t;
        counters = c;

        //the main panel will have tabs that can be clicked through to display different views of the GUI
        tabPane = new JTabbedPane();

        fillTimersTab();
        fillCountersTab();

        JScrollPane timers_scrollpane = new JScrollPane(timersTab);

        JScrollPane counters_scrollpane = new JScrollPane(countersTab);

        tabPane.addTab("Timers", timers_scrollpane);
        tabPane.addTab("Counters", counters_scrollpane);
        tabPane.setBackground(Color.WHITE);
        tabPane.setForeground(Color.BLACK);        

        getContentPane().add(tabPane);

        setSize(925, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
    
        //sets the frame in the middle of the screen
        setLocationRelativeTo(null);
        
        setVisible(true);
    }

    //basic method for filling a tab in the GUI
    private JPanel makeTab(String text){
        JPanel panel = new JPanel(false);
        panel.setBackground(Color.WHITE);
        return panel;
    }

    //fills the timers tab of the GUI with elements
    private void fillTimersTab (){

        timersTab = makeTab("Timers");
        
        //holds all the panels that will hold the timer reports
        ArrayList <JPanel> panels = new ArrayList <JPanel>();

        //these are used to iterate through the hashmap
        Set set;
        Iterator it;

        JPanel timerInformationPanel;

        set = timers.entrySet();
        it = set.iterator();

        //while there are entries in the hashmap, iterate through
        while(it.hasNext()){
            timerInformationPanel = new JPanel(new BorderLayout());
            timerInformationPanel.setBackground(Color.BLACK);

            Map.Entry hashMapEntry = (Map.Entry)it.next();
            
            //the main title and stats of this timer reports
            String keyString = "<html><h2><u>Timer Record: \"" + hashMapEntry.getKey().toString() + "\"</u></h2></html>";

            JPanel titleBar = new JPanel(new GridLayout(4, 1));

            JLabel keyName = new JLabel(keyString, SwingConstants.CENTER);
            keyName.setForeground(Color.WHITE);
            
            titleBar.add(keyName);
            titleBar.setBackground(Color.BLACK);

            //System.out.println("Hashmap Key: \""+ hashMapEntry.getKey() + "\"\nTimer Values: ");

            //   ***S T Y L E***
            JLabel colorBorderEAST = new JLabel("   ");
            colorBorderEAST.setBackground(Color.DARK_GRAY);
            JLabel colorBorderWEST = new JLabel("   ");
            colorBorderEAST.setBackground(Color.DARK_GRAY);

            //holds the duration information for ease of display
            ArrayList <Duration> tempDurations = (ArrayList <Duration>)(hashMapEntry.getValue());

            JPanel grid = new JPanel(new GridLayout(tempDurations.size() + 1, 4));
            grid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            grid.setBackground(Color.WHITE);

            grid.add(new JLabel());
            /*grid.add(new JLabel("<html><h2>Start Time</h2></html>", SwingConstants.CENTER));
            grid.add(new JLabel("<html><h2>Stop Time</h2></html>", SwingConstants.CENTER));*/
            grid.add(new JLabel("<html><h2>Timer Duration</h2></html>", SwingConstants.CENTER));
            grid.add(new JLabel("<html><h2>Start Message</h2></html>", SwingConstants.CENTER));
            grid.add(new JLabel("<html><h2>Stop Message</h2></html>", SwingConstants.CENTER));

            //these hold the statistics information
            long average = 0;
            long least = Long.MAX_VALUE;
            long most = 0;
            long totalTime = 0;

            //iterate through the Duration information in each ArrayList
            for(int g = 0; g < tempDurations.size(); g++){
                totalTime = tempDurations.get(g).getDifference();

                String fullTimerInfo = "Start Time: " + tempDurations.get(g).getStart() + ", End Time: " + tempDurations.get(g).getEnd() + ", Difference: " + totalTime;
                //+ nanoToSecondsString(totalTime)

                grid.add(gridLabelInit("#" + (g + 1)));
                /*grid.add(gridLabelInit(tempDurations.get(g).getStart() + "ns"));
                grid.add(gridLabelInit(tempDurations.get(g).getEnd() + "ns"));*/
                grid.add(gridLabelInit(totalTime + "ns"));
                grid.add(gridLabelInit(tempDurations.get(g).getStartMessage()));
                grid.add(gridLabelInit(tempDurations.get(g).getEndMessage()));

                System.out.println(fullTimerInfo);

                average += totalTime;

                //used to determine the new shortest and longest timer values
                if(totalTime < least){
                    least = totalTime;
                }

                if(totalTime > most){
                    most = totalTime;
                }
            }

            //the average time it took (in nanoseconds) to take the time on this timer
            average = average / tempDurations.size();

            titleBar.add(statLabelInit("<html><h3>Average Time: " + average + "ns</h3></html>"));
            titleBar.add(statLabelInit("<html><h3>Shortest Time: " + least + "ns</h3></html>"));
            titleBar.add(statLabelInit("<html><h3>Longest Time: " + most + "ns</h3></html>"));

            timerInformationPanel.add(titleBar, BorderLayout.NORTH);
            timerInformationPanel.add(colorBorderEAST, BorderLayout.EAST);
            timerInformationPanel.add(colorBorderWEST, BorderLayout.WEST);
            timerInformationPanel.add(grid, BorderLayout.CENTER);

            System.out.println("Statistics:\nAverage Time: " + average);
            System.out.println("Shortest Time: " + least);
            System.out.println("Longest Time: " + most + "\n-----");

            panels.add(timerInformationPanel);
        }

        timersTab.setLayout(new GridLayout(panels.size(), 1));

        //add all of the timers' full panels/information to the timers tab
        for(JPanel j : panels){
            timersTab.add(j);
        }

    }  

    //initializes the labels in the grid that displays the timer stats
    private JLabel gridLabelInit (String content){
        JLabel label = new JLabel(content, SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return label;
    }

    //initializes the stats label at the top of each timer report
    private JLabel statLabelInit (String content){
        String string = content;
        JLabel label = new JLabel(string, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        return label;
    }

    //works similarly to to the timers tab, with less information needed
    private void fillCountersTab (){

        countersTab = makeTab("Counters");
        ArrayList <JPanel> panels = new ArrayList <JPanel>();

        //used to iterate through every value in the hashmap 
        Set set;
        Iterator it;
        JPanel counterInformationPanel;

        set = counters.entrySet();
        it = set.iterator();

        //while there are entries in the hashmap, iterate through
        while(it.hasNext()){
            counterInformationPanel = new JPanel(new BorderLayout());
            counterInformationPanel.setBackground(Color.BLACK);

            Map.Entry hashMapEntry = (Map.Entry)it.next();
            
            String keyString = "<html><h2>Counter Record: \"" + hashMapEntry.getKey().toString() + "\"</h2></html>";

            JLabel keyName = new JLabel(keyString, SwingConstants.CENTER);
            keyName.setForeground(Color.WHITE);

            //System.out.println("Hashmap Key: \""+ hashMapEntry.getKey() + "\"\nTimer Values: ");
            counterInformationPanel.add(keyName, BorderLayout.NORTH);

            JLabel colorBorderEAST = new JLabel("  ");
            colorBorderEAST.setBackground(Color.DARK_GRAY);
            JLabel colorBorderWEST = new JLabel("  ");
            colorBorderEAST.setBackground(Color.DARK_GRAY);

            counterInformationPanel.add(colorBorderEAST, BorderLayout.EAST);
            counterInformationPanel.add(colorBorderWEST, BorderLayout.WEST);

            //the number of times this counter was called/incremented 
            int tempCount = (int)hashMapEntry.getValue();

            JPanel grid = new JPanel(new GridLayout(1, 2));
            grid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            grid.setBackground(Color.WHITE);

            String callCountString = "<html><h3>" + tempCount + "</h3></html>";
            grid.add(new JLabel("<html><h3>Call Count: </h3></html>", SwingConstants.CENTER));
            grid.add(new JLabel(callCountString, SwingConstants.CENTER));

            counterInformationPanel.add(grid);

            panels.add(counterInformationPanel);
        }

        countersTab.setLayout(new GridLayout(panels.size(), 1));

        //adds all of the panels collected for each named counter into the counters tab of the GUI
        for(JPanel j : panels){
            countersTab.add(j);
        }
    }

    //can convert the time-longs into seconds
    private double nanoToSeconds (long time){
        return (double)(time * 0.000000001);
    }
    private String nanoToSecondsString (long time){
        return (" (" + nanoToSeconds(time) + " Seconds)");
    }

}