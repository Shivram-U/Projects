import java.net.*;
import java.io.*;
import java.util.*;

class Client_Input_Thread  extends Thread
{
    public Socket soc;
    public identity Client_ID,Server_ID;
    public BufferedReader cons;
    public DataInputStream ip;
    Client_Input_Thread(Socket soc,identity id)
    {
        this.soc = soc;
        this.Client_ID = id;
        this.Server_ID = new identity();
        cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            ip = new DataInputStream(soc.getInputStream());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    Client_Input_Thread(Socket soc,Client_Connection cl)
    {
        this.soc = soc;
        //this.cl = cl;
        cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            ip = new DataInputStream(soc.getInputStream());
        }
        catch(Exception e)
        {
            if(cl==null)
            {
                System.out.print(e);
            }
            else   
                System.out.println("Client "+cl.id.fv.get("name")+" Disconnected");
        }
    }
    @Override
    public void run()
    {
        String t1,t2="";
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
                    // System.out.print("|"+t1.charAt(n));
                    t2+=t1.charAt(n++);
                }
                this.Server_ID.fv.put(this.Client_ID.flds[n1++],t2);
                t2 = "";
                t1 = ip.readUTF();
            }
            this.Server_ID.print();
            while(soc.isConnected())
            {
                t1 = ip.readUTF();
                System.out.println("\n"+this.Server_ID.fv.get("name")+">"+t1);
            }
            ip.close();
        }
        catch(Exception e)
        {
                System.out.print(e);
        }
    }
}

class Client_Output_Thread     extends Thread
{
    public Socket soc;
    public identity Client_ID,Server_ID;
    public BufferedReader cons;
    public DataOutputStream op;
    Client_Output_Thread(Socket soc,identity id)
    {
        this.soc = soc;
        this.Client_ID = id;
        cons = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            op = new DataOutputStream(soc.getOutputStream());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    @Override
    public void run()
    {
        String t1;
        try
        {
            for(int i=0;i<5;i++)
            {
                op.writeUTF("<INFO>"+this.Client_ID.fv.get(this.Client_ID.flds[i])+"<INFO>");
            }
            op.writeUTF("<END>");
            while(soc.isConnected())
            {
                t1 = cons.readLine();
                op.writeUTF(t1);
                op.flush();
                //System.out.println(t1);
            }
            op.close();
        }
        catch(Exception e)
        { 
            System.out.println(e);
        }
    }
}


public class Chat_Client
{
    static public identity id;   
    static public Socket soc;
    static public Client_Input_Thread inp;
    static public Client_Output_Thread oup;
    public static void main(String args[])  throws IOException,InterruptedException
    {
        ///InetAddress address = InetAddress.getByName("0.tcp.in.ngrok.io");
        //System.out.println(address);
        id = new identity();
        Scanner sc = new Scanner(System.in);
        id.Inp(sc);
        Socket soc = new Socket("localhost",3333);
        DataInputStream read = new DataInputStream(soc.getInputStream());
        DataOutputStream wrt = new DataOutputStream(soc.getOutputStream());
        String in="",out="";
        System.out.println("m");
        System.out.println("Stream Started");
        inp = new Client_Input_Thread(soc,id);
        inp.start();
        oup = new Client_Output_Thread(soc,id);
        oup.start();
        System.out.println("Server Disconnected");
        //read.close();
        //inp.close();
        //cons.close();
    }    
}
