package Chat_App;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;
//import Network_Interface.Chat_Server;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

public class Host_Interface    implements ActionListener,WindowListener
{
    public Chat_Server cs = null;
    public Chat_Client cl = null;
    public Container cont;
    public Component[] comps;
    public JFrame jf;
    public jsn jn;
    public JLabel[] jl;
    public JTextField[] jt;
    public JTextArea[] jta;
    public JTabbedPane jtp;
    public JButton[] jb;
    public JScrollPane[] jsp;
    public JPanel[] jp;
    public Font fnt;
    public int Screen_Width,Screen_Height,cmps=0;
    public String[] Tabs = {"Host Server","User Details","Connection Details","Clients Details","Console","Terminal"},
                    hd = {"Name","Identity","Language","Host Location","Nationality"},
                    clnt={"Connection Server IP:","Server Port:"},
                    bts = {"Start Server","Stop Server","Update Server Configuration","Update User Details","Connect","Send"},
                    flds={"name","id","Language","Host Location","Nationality"};
    public String vals[] = {"","","","",""};
    public String t;
    public JSONObject jsn1,jsn2;
    public static void main(String[] args)
    {
        Host_Interface serv = new Host_Interface();
    }
    public Host_Interface()
    {
        //Customized_Screen_Dev.MW  wind = new  Customized_Screen_Dev.MW();
        jn = new jsn();
        comps = new Component[10];
        jf = new JFrame();
        jf.setLayout(null);             // The Layout has to be specified before the Bounds of the Components to be specified,
        this.cont = jf.getContentPane();
        jl = new JLabel[15];
        jt = new JTextField[15];
        jta = new JTextArea[3];
        jp = new JPanel[10];
        jsp = new JScrollPane[10];
        jb = new JButton[10];
        vals = new String[5];
        jtp = new JTabbedPane();
        
        this.Values_Init();
        this.Add_Components();
    }
    public void Values_Init()
    {
        // Values:
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            this.Screen_Width = (int)size.getWidth();
            this.Screen_Height = (int)size.getHeight();
        fnt = new Font("Helvetica",Font.PLAIN,20);
    }
    public void Add_Components()
    {
        // Components Initialisation:
            jl[0] = new JLabel("General Chat Host Interface");
            jl[1] = new JLabel("Server IP:");
            jl[2] = new JLabel("Application Port:");
            jl[3] = new JLabel("Maximum Client Count:");
            for(int i=0;i<6;i++)
            {
                jb[i] = new JButton(this.bts[i]);
            }
            for(int i=0;i<11;i++)
            {
                jt[i] = new JTextField();
            }
            jta[0] = new JTextArea();
            jsp[0] = new JScrollPane(jta[0]);
        // Components Inclusion
            this.jf.add(jl[0]);
            for(int i=0;i<6;i++)
            {
                jp[i] = new JPanel();
                jp[i].setLayout(null);
                jtp.add(Tabs[i],jp[i]);
            }
            this.jf.add(jtp);
           
            for(int i=1;i<4;i++)
            {
                this.jp[0].add(jl[i]);
            }

            for(int i=4;i<9;i++)
            {
                jl[i] = new JLabel(this.hd[i-4]);
                jp[1].add(jl[i]);
            }
            jl[9] = new JLabel(this.clnt[0]);
            jl[10] = new JLabel(this.clnt[1]);
            jp[2].add(jl[9]);
            jp[2].add(jl[10]);
            jp[2].add(jt[8]);
            jp[2].add(jt[9]);
            jp[2].add(jb[4]);
            
            for(int i=0;i<3;i++)
                jp[0].add(jb[i]);
            jp[1].add(jb[3]);
            for(int i=3;i<8;i++)
            {
                this.jp[1].add(jt[i]);
            }

            for(int i=0;i<3;i++)
                this.jp[0].add(jt[i]);
            jl[11] = new JLabel("Chat Console:");
            this.jp[4].add(jl[11]);
            this.jp[4].add(jsp[0]);
            this.jp[4].add(jt[10]);
            jt[10].setEnabled(false);
            this.jp[4].add(jb[5]);
            jb[5].setEnabled(false);
            this.Position_Components();
            //System.out.println(jp[0].getComponent(0));
    }
    public void Position_Components()
    {
        jl[0].setBounds(0,0,this.Screen_Width,35);
        jl[1].setBounds(200,150,300,50);
        jl[2].setBounds(200,220,300,50);
        jl[3].setBounds(200,290,300,50);
        for(int i=4,y = 160;i<9;i++,y+=70)
        {
            jl[i].setBounds(200,y,300,50);
            jt[i-1].setBounds(500,y,300,50);
        }
        jb[0].setBounds(600,500,300,40);
        jb[1].setBounds(600,550,300,40);
        jb[2].setBounds(600,600,300,40);
        jb[3].setBounds(600,600,300,40);
        jb[4].setBounds(600,500,300,40);
        jb[5].setBounds(this.Screen_Width-100,this.Screen_Height-185,100,50);
        jt[0].setBounds(500,150,400,50); 
        jt[1].setBounds(500,220,400,50); 
        jt[2].setBounds(500,290,400,50); 
        jl[9].setBounds(200, 130,400,50);
        jl[10].setBounds(200, 190,400,50);
        jt[8].setBounds(600,130, 300,50);
        jt[9].setBounds(600,190, 300,50);
        jl[11].setBounds(0,0,this.Screen_Width,50);
        jt[10].setBounds(0, Screen_Height-185, Screen_Width-100, 50);
        jsp[0].setBounds(0,51,this.Screen_Width,this.Screen_Height-237);
        jtp.setBounds(0,36,this.Screen_Width, this.Screen_Height);
        this.Configure_Components();
    }
    public void Configure_Components()  
    {
        // jta[0].setText("mwowqwqw");
        // Main Window Configuration
            jf.setTitle("   General Chat  ");
            jf.setLocation(0,0);
            //jf.setSize(1000,700);
            jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
            jf.setResizable(true);                         // Will Not Work, Since the Resizing Provision, provided by the OS, is removed, we have to Define Our Own Algorithm for Resizing.
            jf.setVisible(true);
            
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cont.setBackground(new Color(0,0,0));
            cont.setLayout(null);
        
        this.jtp.setBackground(new Color(40,40,40));
        this.jtp.setForeground(new Color(0,255,255));
        for(int i=0;i<6;i++)
        {
            this.jp[i].setBackground(new Color(0,0,0));
        }
        try
        {
            //System.out.println(jn.Read_json("Server_Config").get("Port"));
            //jt[0].setText("0");
            //jt[1].setText("1");
            jsn1 = jn.Read_json("Resources\\Server_Config");
            jt[0].setText((String)(jsn1.get("Server_IP")));
            //jt[1].setText((String)(jn.Read_json("Server_Config").get("Server_IP")));
            jt[1].setText(String.valueOf((jsn1.get("Port"))));
            jt[2].setText(String.valueOf((jsn1.get("Max_Client_Count"))));
            
            jsn1 = jn.Read_json("Resources\\Connection");
            jt[8].setText(String.valueOf(jsn1.get("Server_IP")));
            jt[9].setText(String.valueOf(jsn1.get("Port")));
            jsn1 = jn.Read_json("Resources\\User");
            for(int i=0;i<5;i++)
            {
                t = String.valueOf(jsn1.get(this.flds[i]));
                jt[i+3].setText(String.valueOf(t));
                this.vals[i] = String.valueOf(t);
            }
        }
        catch(Exception e)
        {
            System.out.println("\nHost_Interface: "+e);
        }

        for(int i=0;i<12;i++)
        {
            jl[i].setOpaque( true);
            jl[i].setHorizontalAlignment(SwingConstants.CENTER);
            jl[i].setBorder(null);
            jl[i].setBackground(new Color(30,30,30));
            jl[i].setForeground(new Color(0,255,255));
            jl[i].setFont(fnt);
        }
        for(int i=0;i<11;i++)
        {
            jt[i].setBackground(new Color(40,40,40));
            jt[i].setFont(fnt);
            jt[i].setForeground(new Color(0,255,255));
            jt[i].setBorder(null);
            jt[i].setHorizontalAlignment(SwingConstants.CENTER);
        }

        for(int i=0;i<6;i++)
        {
            jb[i].setFont(fnt);
            jb[i].setHorizontalAlignment(SwingConstants.CENTER);
            jb[i].setBackground(new Color(40,40,40));
            jb[i].setForeground(new Color(0,255,255));
        }

        jl[0].setHorizontalAlignment(SwingConstants.CENTER);
        jl[0].setOpaque(true);
        jl[0].setBackground(new Color(40,40,40));
        jl[0].setFont(new Font("Helvetica",Font.PLAIN,25));
        
        jl[11].setHorizontalAlignment(SwingConstants.CENTER);
        jl[11].setOpaque(true);
        jl[11].setBackground(new Color(30,30,30));
        jl[11].setForeground(new Color(0,255,255));
        jl[11].setFont(new Font("Helvetica",Font.PLAIN,25));

        jta[0].setFont(new Font("Courier New",Font.PLAIN,20));
        jta[0].setBackground(new Color(0,10,10));
        jta[0].setEditable(false);
        jta[0].setForeground(new Color(0,255,255));
        this.Add_Events();
    }
    public void Add_Events()
    {
        jf.addWindowListener(this);
        for(int i=0;i<6;i++)
            jb[i].addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getActionCommand() == "Send")
        {
            if(this.cs != null)
            {   
                this.cs.Update();
                if(this.cs.Clntnum>0)
                {
                    try
                    {
                        //this.oup = new Server_Output_Thread(cs,((JTextField)this.jp[4].getComponent(1)).getText());
                        String t1="",t2="";
                        int l;
                        l = this.cs.Max_Length();
                        t1 = ((JTextField)this.jp[4].getComponent(2)).getText();
                        //this.oup = new Server_Output_Thread(this.cs,t1);
                        t2 = String.format("%"+-l+"s%s",this.cs.Get_Host_Name()," >  "+t1);
                        (this.jta[0]).setText((this.jta[0]).getText()+t2+"\n");
                        this.cs.Write_Text(t1);
                    }
                    catch(Exception e1)
                    {
                        System.out.println("\nHost_Interface: "+e1);
                    }
                    //System.out.println("done");
                }
                //System.out.println("got");
            }
            else if(cl!=null)
            {
                //System.out.println("C1");
                String t1="",t2="";
                int l;
                //l = this.cl.cs.Max_Length();
                t1 = ((JTextField)this.jp[4].getComponent(2)).getText();
                //this.oup = new Server_Output_Thread(this.cs,t1);
                t2 = String.format("%"+-20+"s%s",this.cl.Get_Client_Name()," >  "+t1);
                (this.jta[0]).setText((this.jta[0]).getText()+t2+"\n");
                this.cl.Write_Text(t1);
                //System.out.println("C2");
            }
        }
        else if(e.getActionCommand() == "Start Server")
        {
            if(cs == null && vals[1]!=null && vals[2]!=null && this.jb[3].getText()!=null)
            {
                this.comps[0] = jt[2];
                this.comps[1] = jta[0]; 
                this.comps[2] = jt[10]; 
                cs = new Chat_Server("localhost", 3333,this.comps,2,this.vals);
                //this.oup = new Server_Output_Thread(this.cs);
                cs.start();
                if(cs!=null)
                {
                    this.jt[10].setText("Hi im Server");
                    this.jb[5].setEnabled(true);
                    this.jt[10].setEnabled(true);
                }
            }
        }
        else if(e.getActionCommand() == "Connect")
        {
            if(cl == null)
            {
                try
                {
                    comps[0] = jta[0];
                    cl = new Chat_Client("localhost",3333,this.comps,1,this.vals);
                    if(cl!=null)
                    {
                        this.jt[10].setText("Hi im Client");
                        this.jb[5].setEnabled(true);
                        this.jt[10].setEnabled(true);
                    }
                }
                catch(Exception e1)
                {
                    System.out.println("\nHost_Interface: "+e1);
                }
            }
        }
        else if(e.getActionCommand() == "Stop Server")
        {
            
        }
        else if(e.getActionCommand() == "Update Server Configuration")
        {
        }
        else if(e.getActionCommand() == "Update User Details")
        {
            for(int i=3;i<8;i++)
            {
                this.vals[i-3] = this.jt[i].getText();
            }
            // System.out.println(this.vals[0]);
            // System.out.println(this.vals[1]);
            // System.out.println(this.vals[2]);
        }
        
    }
    @Override
    public void windowOpened(WindowEvent e) {
        
    }
    @Override
    public void windowClosing(WindowEvent e) {

    }
    @Override
    public void windowClosed(WindowEvent e) {
    }
    @Override
    public void windowIconified(WindowEvent e) {
    }
    @Override
    public void windowDeiconified(WindowEvent e) {
    }
    @Override
    public void windowActivated(WindowEvent e) {
    }
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}

class jsn
{
    public static void main(String[] args)
    {
        System.out.println("work");
    }
    public JSONObject Read_json(String filename)   throws Exception
    {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("D:\\Shivram_U\\Sources\\Software\\Project_Sys_WorkSpace\\Android_App_Projects\\General_Chat\\Server_Applications\\References\\Chat_GUI_App\\"+filename+".json"));
        JSONObject jsonObject =  (JSONObject) obj;
        return jsonObject;
    }
    public void Write_Json(int file,String args[])  throws Exception
    {
        if(file == 1)
        {
            JSONObject jn = new JSONObject();
            jn.put("Server_IP",args[0]);
            jn.put("Port",Integer.parseInt(args[1]));
            PrintWriter pw = new PrintWriter("Server_Config.json");
            pw.write(jn.toJSONString());
            pw.flush();
            pw.close();
        }
    }
}