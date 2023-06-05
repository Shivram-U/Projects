import java.io.*;
import java.net.*;
import java.util.*;

class identity
{
    public String[] flds={"name","id","Language","Host Location","Nationality"};
    public HashMap<String,String> fv;
    public identity(String[] args)
    {
        fv = new HashMap<String,String>();
        for(int i=0;i<5;i++)
            fv.put(flds[i],args[i]);
    }
    public identity()
    {
        fv = new HashMap<String,String>();
    }
    public void Inp(Scanner Cons_inp)
    {
        System.out.print("Enter Name:");
        fv.put(flds[0],Cons_inp.next());
        System.out.print("Enter ID:");
        fv.put(flds[1],Cons_inp.next());
        System.out.print("Enter Location:");
        fv.put(flds[2],Cons_inp.next());
        System.out.print("Enter Language:");
        fv.put(flds[3],Cons_inp.next());
        System.out.print("Enter Nationality:");
        fv.put(flds[4],Cons_inp.next());
    }
    public void print()
    {
        for(int i=0;i<5;i++)
        {
            System.out.println(this.flds[i]+":"+this.fv.get(this.flds[i]));
        }
    }
}

class Server_Input_Thread  extends Thread
{
    public Socket soc;
    public Client_Connection cl;
    public BufferedReader cons;
    public DataInputStream ip;
    Server_Input_Thread(Socket soc,Client_Connection cl)
    {
        this.soc = soc;
        this.cl = cl;
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
                System.out.println("\nClient "+cl.id.fv.get("name")+" Disconnected");
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
            while(t1.substring(1, 5) != "END" && t1.substring(1,5).equals("INFO"))
            {
                n = 6;
                System.out.println(t1);
                while(n<t1.length() && t1.charAt(n)!='<')
                {
                    //System.out.print("|"+t1.charAt(n));
                    t2+=t1.charAt(n++);
                }
                this.cl.id.fv.put(this.cl.id.flds[n1++],t2);
                t2 = "";
                t1 = ip.readUTF();
            }
            this.cl.id.print();
            while(soc.isConnected())
            {
                t1 = ip.readUTF();
                System.out.println("\n"+this.cl.id.fv.get("name")+">"+t1);
                for(int i=0;i<this.cl.cs.Clntnum;i++)
                {
                    //System.out.println("%%");
                    if(this.cl.id.fv.get("id")!=this.cl.cs.Clnts[i].id.fv.get("id"))
                    {
                        this.cl.cs.Clnts[i].ops.writeUTF("<"+"CLIENT"+" Name= "+this.cl.id.fv.get("name")+">"+"<"+"MESSAGE"+" Content= "+t1+">");
                        this.cl.cs.Clnts[i].ops.flush();
                    }
                }
            }
            ip.close();
        }
        catch(Exception e)
        {
            if(cl==null)
            {
                System.out.print(e);
            }
            else   
                System.out.println("\nClient "+cl.id.fv.get("name")+" Disconnected");
        }
    }
}

class Server_Output_Thread     extends Thread
{
    public Socket soc;
    public int Clntnum =0;
    public Chat_Server cs;
    public BufferedReader cons;
    public DataOutputStream ops[];
    Server_Output_Thread(Chat_Server cs)
    {
        this.cs = cs;
        this.ops = new DataOutputStream[cs.Max_Clnt];
        this.cons = cs.cons;
    }
    @Override
    public void run()
    {
        String t1="";
        try
        { 
            while(t1!="Disconnect")
            {
                t1 = cons.readLine();
                for(int i=0;i<cs.Clntnum;i++)
                {
                    this.cs.Clnts[i].ops.writeUTF(t1);
                }
            }
         //   op.close();
        }
        catch(Exception e)
        {
                System.out.print(e);
        }
    }
}

class Client_Connection
{
    public DataOutputStream ops;
    public Chat_Server cs;
    public identity id;   
    public Socket soc;
    public Server_Input_Thread inp;

    public Client_Connection(Socket soc,Chat_Server cs)  
    {
        // Variables Intitialisation:
        this.cs = cs;
        this.soc = soc;
        try
        {
            this.ops = new DataOutputStream(soc.getOutputStream());
            for(int j=0;j<5;j++)
            {
                this.ops.writeUTF("<INFO>"+this.cs.id.fv.get(this.cs.id.flds[j])+"<INFO>");
            }
            this.ops.writeUTF("<END>");
            String[] t = new String[10];
            this.id = new identity();
            //this.id.Inp(Cons_inp);
            //id.print();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        Start_Stream();
    }
    public void Start_Stream()
    {
        System.out.println("Stream Started");
        inp = new Server_Input_Thread(soc,this);
        inp.start();
    }
}

public class Chat_Server
{
    public identity id;
    public DataInputStream read;
    public BufferedReader cons;
    public Scanner Cons_inp;
    public String ip,op; 
    public int Port,Max_Clnt=0,Clntnum=0; 
    public InetAddress Server_IP;
    public Client_Connection[] Clnts = new Client_Connection[10]; 
    public ServerSocket servp;
    public Server_Output_Thread oup;
    Socket temp;
    public Chat_Server(String Server_DNS,int Port)  
    {
        // Variables Intitialisation:
        this.Port = Port;
        try
        {
            //Server_IP = InetAddress.getByName(Server_DNS);
            //Server_IP="127.0.0.1";
            servp = new ServerSocket(this.Port);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        read = new DataInputStream(System.in);
        Cons_inp = new Scanner(System.in);
        cons = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Maximum Client Count:");
        Max_Clnt = Cons_inp.nextInt();
        Clnts = new Client_Connection[Max_Clnt];
        try
        {
            this.id = new identity();
            id.Inp(Cons_inp);
            id.print();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        Start_Server();
    }
    public void Start_Server()
    {
        try
        {
            System.out.println("m");
            temp = servp.accept();
            System.out.println("m");
            Clnts[Clntnum++] = new Client_Connection(temp,this);
            oup = new Server_Output_Thread(this);
            oup.start();
        }
        catch(Exception e)
        {
            System.out.print(e);
        }
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
                System.out.println(e);
            }
        }
    }
}   
