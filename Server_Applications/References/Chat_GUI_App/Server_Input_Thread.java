package Chat_App;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;


public class Server_Input_Thread  extends Thread
{
 
    public int ord;
    public Socket soc;
    public Client_Connection cl;
    public BufferedReader cons;
    public DataInputStream ip;
    public Server_Input_Thread(Socket soc,Client_Connection cl)         // the Constructors have to be declared public, inorder to initialize the Class
    {
        this.ord = 1;
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
                System.out.print("\nServer_Input_Thread: "+e);
            }
            else   
            {
                System.out.println("\nServer_Input_Thread: "+"Client "+cl.id.fv.get("name")+" Disconnected");
            }
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
            //System.out.println(t1.substring(1,5));
            while(t1.substring(1, 5) != "END" && t1.substring(1,5).equals("INFO"))
            {
                n = 6;
                //System.out.println(t1);
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
            while(soc.isConnected() && ord == 1)
            {
                t1 = ip.readUTF();
                if(t1.length()>0)
                {
                    int l =0;
                    for(int i=0;i<this.cl.cs.Clntnum;i++)
                    {
                        if(this.cl.cs.Clnts[i].id.fv.get("name").length()>l);
                            l = this.cl.cs.Clnts[i].id.fv.get("name").length();
                    }
                    if(l<this.cl.cs.id.fv.get("name").length())
                        l = this.cl.cs.id.fv.get("name").length();
                    // NOTE:
                    /* 
                            Normal Fonts, such as Helvetica, or Times New Roman, have variable Lengths for the font Letters, hence, when used to print a formatted String,
                            with Fixed Length, causes discrepancy, in the Length, even though the Number of Characters printed, still same, due to Difference in the Letter widths.

                            To solve this Problem, use fonts, which are MONOSPACE, These Fonts ensure that all the Letters are of Same Size.
                    */
                    t2 = String.format("%"+-l+"s%s",this.cl.id.fv.get("name")," >  "+t1);
                    //System.out.println("{"+t2+"}");
                    ((JTextArea)this.cl.cs.comps[1]).setText(((JTextArea)this.cl.cs.comps[1]).getText()+t2+"\n");
                    //System.out.println("\n"+this.cl.id.fv.get("name")+">"+t1);
                    //this.cl.cs.hi.jta[0].setText("\n"+this.cl.id.fv.get("name")+">"+t1);
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
            }
            ip.close();
        }
        catch(Exception e)
        {
            if(cl==null)
            {
                System.out.print("\nServer_Input_Thread: "+e);
            }
            else   
            {
                System.out.println("\nServer_Input_Thread: "+"Client "+cl.id.fv.get("name")+" Disconnected");
                this.cl.Cons = false;       // to inform the Closing of the Socket Connection, thorugh Flag Variable.   
            }
        }
    }
}
