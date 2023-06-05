package Chat_App;

import java.net.*;
import java.awt.Component;
import java.io.*;
import java.util.*;
import javax.swing.*;

class Client_Input_Thread  extends Thread
{

    public Chat_Client cl;
    public identity Client_ID,Server_ID;
    public BufferedReader cons;
    public DataInputStream ip;
    public Socket soc;
    public Alive alv;
    public int ord  = 0,pad=1;
    Client_Input_Thread(Chat_Client cl)
    {
        this.cl = cl;
        this.Client_ID = cl.id;
        this.Server_ID = new identity();
        cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            //System.out.println(cl.soc);
            ip = new DataInputStream(cl.soc.getInputStream());
            //System.out.println(ip);
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Client: "+e);
        }
    }
    Client_Input_Thread(Socket soc,Chat_Client cl)
    {
        this.soc = soc;
        this.cl = cl;
        this.Client_ID = cl.id;
        this.Server_ID = new identity();
        cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            System.out.println(soc);
            ip = new DataInputStream(soc.getInputStream());
            //alv = new Alive(soc);
            //alv.start();
        }
        catch(Exception e)
        {
            if(cl==null)
            {
                System.out.print("\nChat_Client: "+e);
            }
            else   
                System.out.println("\nChat_Client: "+"Client "+cl.id.fv.get("name")+" Disconnected");
        }
    }
    @Override
    public void run()
    {
        String t1,t2="",msg="";
        int n=0,n1=0;
        try
        {
            t1 = ip.readUTF();
            n =6;
            System.out.println(t1.substring(1,5));
            while(t1.substring(1,5) != "END" && t1.substring(1,5).equals("INFO"))
            {
                n = 6;
                System.out.println(t1);
                while(n<t1.length() && t1.charAt(n)!='<')
                {
                    //System.out.print("|"+t1.charAt(n));
                    t2+=t1.charAt(n++);
                }
                this.Server_ID.fv.put(this.Client_ID.flds[n1++],t2);
                t2 = "";
                t1 = ip.readUTF();
            }
            this.Server_ID.print();
            while(this.soc.isConnected())
            {
                t1 = ip.readUTF();
                //System.out.println("{"+t1+"}");
                if(t1.charAt(0) == '<')
                {
                    //System.out.println("got"+t1.substring(1, 7));
                    if(t1.substring(1, 7).equals("CLIENT"))
                    {
                        //System.out.println("got");
                        t2="";
                        msg="";
                        n = 14;
                        while(t1.charAt(n)!='>')
                        {
                            t2+=t1.charAt(n++);
                        }
                        n+=20;
                        while(t1.charAt(n)!='>')
                        msg+=t1.charAt(n++);
                        t2 = String.format("%"+-t2.length()+"s%s",t2," >  "+msg);
                        //System.out.println("{"+t2+"}");
                        //System.out.println("\n"+t2+">"+msg);
                        
                    }
                }
                else
                {
                    //System.out.println("\n"+this.Server_ID.fv.get("name")+">"+t1);
                    t2 = String.format("%"+-pad+"s%s",this.Server_ID.fv.get("name")," >  "+t1);
                }
                ((JTextArea)this.cl.comps[0]).setText(((JTextArea)this.cl.comps[0]).getText()+t2+"\n");
            }
            ip.close();
        }
        catch(Exception e)
        {
                System.out.print("\nChat_Client: "+e);
        }
    }
}

class Alive extends Thread      // Not Required, implemented to detect the Client Connection Termination.
{
    public Socket soc;
    public DataOutputStream ous;
    Alive(Socket soc)
    {
        try
        {
            this.soc = soc;
            ous = new DataOutputStream(this.soc.getOutputStream());
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Client: "+e);
        }
    }
    @Override
    public void run()
    {
        while(!soc.isClosed())
        {
            //System.out.println("meow");
            try
            {
                this.ous.writeByte(1);
                this.sleep(1000);
            }
            catch(Exception e)
            {
                System.out.println("\nChat_Client: "+e);
            }
        }
    }
}
public class Chat_Client
{
    public Component[] comps;
    public identity id;   
    public Socket soc;
    public Client_Input_Thread inp;
    public DataOutputStream ous;
    public int cmp;
    public Chat_Client(String Server_DNS,int Port,Component[] comps,int cmp,String args[])  throws IOException,InterruptedException
    {
        ///InetAddress address = InetAddress.getByName("0.tcp.in.ngrok.io");
        //System.out.println(address);
        this.comps = comps;
        this.cmp = cmp;
        id = new identity();
        //Scanner sc = new Scanner(System.in);
        id.Inp(args);
        id.print();
        Socket soc = new Socket(Server_DNS,Port);
        //System.out.println(soc);
        this.ous = new DataOutputStream(soc.getOutputStream());
        for(int j=0;j<5;j++)
        {
            this.ous.writeUTF("<INFO>"+this.id.fv.get(this.id.flds[j])+"<INFO>");
        }
        this.ous.writeUTF("<END>");
        String[] t = new String[10];
        //this.id.Inp(Cons_inp);
        //id.print();
        //DataInputStream read = new DataInputStream(soc.getInputStream());
        //DataOutputStream wrt = new DataOutputStream(soc.getOutputStream());
        String in="",out="";
        System.out.println("Stream Started");
        inp = new Client_Input_Thread(soc,this);
        inp.start();
        System.out.println("Server Disconnected");
        //read.close();
        //inp.close();
        //cons.close();
    }  
    public String Get_Client_Name()
    {
        return this.id.fv.get("name");
    }
    public void Write_Text(String txt)
    {
        try
        {
            this.ous.writeUTF(txt);
        }
        catch(Exception e)
        {
            System.out.println("\nChat_Client: "+e);
        }
    }  
}
