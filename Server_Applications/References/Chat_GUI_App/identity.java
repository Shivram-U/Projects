package Chat_App;

import java.awt.Component;
import java.io.*;
import java.net.*;
import java.util.*; 

public class identity
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
    public void Inp(String[] args)
    {
        for(int i=0;i<5;i++)
            fv.put(flds[i],args[i]);
    }
    public void print()
    {
        for(int i=0;i<5;i++)
        {
            System.out.println(this.flds[i]+":"+this.fv.get(this.flds[i]));
        }
    }
}
