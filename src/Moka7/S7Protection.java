package Moka7;

// See §33.19 of "System Software for S7-300/400 System and Standard Functions"
public class S7Protection {
   public int sch_schal;
   public int sch_par;
   public int sch_rel;
   public int bart_sch;
   public int anl_sch;
   protected void Update(byte[] Src)
   {
       sch_schal = S7.GetIntAt(Src,2);
       sch_par   = S7.GetIntAt(Src,4);
       sch_rel   = S7.GetIntAt(Src,6);
       bart_sch  = S7.GetIntAt(Src,8);
       anl_sch   = S7.GetIntAt(Src,10);
   }
}
