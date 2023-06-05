package Chat_App;

import java.awt.Component;
import java.io.*;
import java.net.*;
import java.util.*; 

import javax.swing.*;


public class Chat_Server    extends Thread
{
    public Component[] comps;
    public identity id;
    public DataInputStream read;
    public BufferedReader cons;
    public Scanner Cons_inp;
    public String ip,op; 
    public int Port,Max_Clnt=0,Clntnum=0,cmp=0; 
    public InetAddress Server_IP;
    public Client_Connection[] Clnts = new Client_Connection[10]; 
    public ServerSocket servp;
    
    Socket temp;
    public Chat_Server(String Server_DNS,int Port,Component[] cmps,int cmp,String[] args)  
    {
        // Variables Intitialisation:
        this.Port = Port;
        this.comps = cmps;
        this.cmp = cmp;
        this.cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            this.id = new identity();
            //id.Inp(Cons_inp);
            id.Inp(args);
            id.print();
            //System.out.println(this.hi.Tabs[0]);
            this.Max_Clnt = Integer.parseInt(((JTextField)comps[0]).getText());
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
    }
    public void Update()            // to update the Number of Clients, who are Connected. 
    {
        // Reference https://www.alpharithms.com/detecting-client-disconnections-java-sockets-091416/
        // NOTE:    No Additional Mechanism is required to detect the Termination of Client Connection, since it the Input-Stream Thread is Already runnning.
        //          if any termination occurs, the Input-Stream Thread, will automatically acknowledge, with an Exception, that Exception can be used to detect the Client Connection Termination, using a Flag Variable.
        int t = 0;
        try
        {
            for(int i=0;i<Clntnum;i++)
            {
                //if(this.Clnts[i].soc.isClosed())        // Do not use the .isClosed() method to detect Client Siode Disconnection, it will oly detect Server Side Disconnection
                //if(this.Clnts[i].soc.getInputStream().read() == -1)
                if(this.Clnts[i] != null && this.Clnts[i].Cons)
                {
                    //System.out.println("Client "+this.Clnts[i].id.fv.get("name")+" Status: Connected");
                    t++;
                }
                else
                {
                    System.out.println("Client "+this.Clnts[i].id.fv.get("name")+" Status: Disconnected");
                    this.Clnts[i].soc.close();
                    int t1 =i;
                    while(t1<=Clntnum-1)
                    {
                        this.Clnts[t1] = this.Clnts[t1+1];
                        t1++;
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
        this.Clntnum = t;
        //System.out.println("Updated Client Count:"+this.Clntnum);
    }
    public void Write_Text(String txt)  throws IOException
    {
        for(int i=0;i<this.Clntnum;i++)
        {
            this.Clnts[i].ops.writeUTF(txt);
        }
    }
    @Override
    public void run()
    {
        try
        {
            // Server_IP = InetAddress.getByName(Server_DNS);
            // Server_IP = "127.0.0.1";
            servp = new ServerSocket(this.Port);
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
        // cons = new BufferedReader(new InputStreamReader(System.in));
        Clnts = new Client_Connection[Max_Clnt];
        try
        {
            System.out.println("m");
            temp = servp.accept();
            System.out.println("m");
            Clnts[Clntnum++] = new Client_Connection(temp,this);
        }
        catch(Exception e)
        {
            System.out.print("\nChat_Server: "+e);
        }
        System.out.println("Server Online");
        while(Clntnum<Max_Clnt)
        {
            try
            {
                System.out.println("m");
                temp = servp.accept();
                System.out.println("m");
                Clnts[Clntnum++] = new Client_Connection(temp,this);
            }
            catch(Exception e)
            {
                System.out.println("\nChat_Server: "+e);
            }
        }
    }
    public String Get_Host_Name()
    {
        return this.id.fv.get("name");
    }
    public int Max_Length()
    {
        int l=0,t;
        for(int i=0;i<this.Clntnum;i++)
        {
            t = this.Clnts[i].id.fv.get("name").length();
            if(t>l)
            {
                l = t;
            }
        }
        return l;
    }
    public void Stop_Server()
    {
        try
        {
            for(int i=0;i<Clntnum;i++)
            {
                this.Clnts[i].inp.ip.close();
                this.Clnts[i].inp.ord = 0;
                this.Clnts[i].ops.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Server: "+e);
        }
        System.out.println("Server Offline");
    }
}   
