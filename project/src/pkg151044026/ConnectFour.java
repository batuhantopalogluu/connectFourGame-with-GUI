/*
    Batuhan TOPALOĞLU 151044026 CSE241 hw08
*/

package pkg151044026;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ConnectFour extends  JFrame
{
    private int size;//board size ını tuttuğum değişken
    private int GameType ;
    private Cell[][] gameCell ; //board 
    private int [] colPosition; // her sütunda en son nereye ekleme yaptığımı tuttuğum arr
    JPanel jp = new JPanel();
    
    JButton[] buttons = new JButton[10];// max size 10 da max 10 buton olabilir
    // butonlara loop içerisinde işlem atama ile ilgili sorun yaşadığım böyle bir 
    //yol seçmek zorunda kaldım
    
    private int siram;// sıranın kimde olduğunu ve livingcell sayısını tutuğum değişken
 
    private JLabel [] userN ;// en son satırdaki user bilgilerini tutan labellar
  
    private int [] ıconLocation; // bitme durumunda değişecek ikonların konumlarını tuttuğum değişken
    
    private int statu=1,statu2=1;// play computer modunda kullandığım değişkenler class ın member ı olmalıydı
    private int hedef;//play computer modunda kullandığım değişken
    
    // en son satırdaki labellar için fontlar
    private Font f1,f2;
    
    private class Cell extends JLabel
    {
        JLabel jl;
        private int type ;
        public Cell() // Boş Cell in verisi ve görseli default olarak atanıyor
        { 
            type = 0; //'0' = boş '1'=user1 '2'=user2
            jl = new JLabel(new ImageIcon(getClass().getResource("null.png")));/////////////
        }
        public int getType(){ return type;}
        public void SetType(int t){ type = t;}
    }
    
    public ConnectFour(int gametype,int sizem)
    {       
        setTitle("ConnectFour      Batuhan TOPALOĞLU");
        setVisible(true);
        setSize(1000,1000);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        jp.setBackground(Color.blue);
       
        size = (int)sizem;
        GameType =(int)gametype;
  
        //+2 nin biri butonlar diğeri ise en alt satırdakı user bilgileri
        jp.setLayout(new GridLayout(size+2,size));      
        
        //mape ve onunla ilgili veri tutan değişkenler için yer alınır
        gameCell = new Cell[size][size];
        colPosition = new int[size];
        userN = new JLabel[size];
        
        f1 = new Font("Arial",Font.BOLD,200/(size));
        f2 = new Font("Arial",Font.PLAIN,150/(size));
    }
    
    private void domaze()
    {        
        userN[0] = new JLabel(new ImageIcon(getClass().getResource("user111.png")));
        userN[size-1] = new JLabel(new ImageIcon(getClass().getResource("user222.png")));
        for(int i=1;i<size-1;i++) userN[i] = new JLabel();
        Font f = new Font("Arial",Font.BOLD,150/(size));
        userN[1].setText("    USER 1");
        userN[1].setFont(f);   
        
        //en alt satırda kullanıcıları yazan labellara atamaları yapılır
        if(GameType == 1)userN[size-2].setText("  COMPUTER");
        if(GameType == 2)userN[size-2].setText("    USER 2");
        userN[size-2].setFont(f);
        
        for(int i=0;i<size;i++) colPosition[i]=size-1;
       
        for(int i=0;i<size;i++)
        {
            buttons[i] = new JButton(new ImageIcon(getClass().getResource("benimok2.png")));  
            buttons[i].setPreferredSize(new Dimension(1000/(size+1),1000/(size+1)));                     
        }
        // Bu kısım banada mantıklı gelmiyor daha etkili yolları olabilir ama acele ile uğraşırken böyle şeyler ortaya çıktı         
        if(size>=4)doact0();// butonlara işlevlerinin atanması
        if(size>=5)doact4();
        if(size>=6)doact5();
        if(size>=7)doact6();
        if(size>=8)doact7();
        if(size>=9)doact8();
        if(size>=10)doact9();
        
        for (int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {  //Board un Cell leri için yer alınır bu esnada cell in default constructor ı gerekli atamaları yapar               
                gameCell[i][j]= new Cell();
                // size 7 dan daha büyük olduğunda daha küçük bir ıcon kullanıyorum
                if(size>7)gameCell[i][j].jl.setIcon(new ImageIcon(getClass().getResource("null2.png")));
            }
        }
        //önce butonları ardından boş celleri panele ekliyorum
        for(int k=0;k<size;k++) jp.add(buttons[k]);
      
        for (int a=0;a<size;a++)
            for(int j=0;j<size;j++)           
                jp.add(gameCell[a][j].jl);
        
        
        for (int a=0;a<size;a++) jp.add(userN[a]);
        add(jp);
        validate();
    }
    private boolean finishHorizontal()// yatay bitme durumunu kontrol eden method
    {
        for(int i=0;i<size;i++)
        {
            for(int j = 0;j<size;j++)
            {
                if((j+3 < size) && (gameCell[i][j].getType() !=0 ) 
                &&(gameCell[i][j].getType() == gameCell[i][j+1].getType()) 
                &&(gameCell[i][j+1].getType()== gameCell[i][j+2].getType())
                &&(gameCell[i][j+2].getType() == gameCell[i][j+3].getType()))
                {// bitme koşulu sağlanırsa sağlayan hüclerin konumlarını ıconları değiştiren
                 // method a gönderiyorum ardından kazanma mesajını basan methodu çağırıyorum   
                    ıconLocation=new int[]{i,j,i,j+1,i,j+2,i,j+3};
                    setIconsEnd(ıconLocation);
                    Integer a = new Integer(siram);
                    return true;
                } 
            }
        }
        return false;
    }
    private boolean finishVertical()//dikey bitme durumunu kontrol eden method
    {
        for(int i=0;i<size;i++)
        {
            for(int j = 0;j<size;j++)
            {
                if((i+3 < size) && (gameCell[i][j].getType() !=0) 
                &&(gameCell[i][j].getType() == gameCell[i+1][j].getType()) 
                &&(gameCell[i+1][j].getType()== gameCell[i+2][j].getType())
                &&(gameCell[i+2][j].getType() == gameCell[i+3][j].getType()))
                {
                    ıconLocation=new int[]{i,j,i+1,j,i+2,j,i+3,j};
                    setIconsEnd(ıconLocation);                    
                    return true;
                } 
            }
        }
        return false;
    }
    private boolean diagonal()//çapraz bitme durumlarını kontrol eden method
    {
        int statu = 0;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if((i+3<size) && (j+3<size) && (gameCell[i][j].getType() !=0) 
                && (gameCell[i][j].getType()==gameCell[i+1][j+1].getType()) 
                && (gameCell[i+1][j+1].getType() == gameCell[i+2][j+2].getType()) 
                && (gameCell[i+2][j+2].getType() == gameCell[i+3][j+3].getType()))
                { // sağ üsten sağ alta tarama yapan kısım
                    ıconLocation=new int[]{i,j,i+1,j+1,i+2,j+2,i+3,j+3};
                    setIconsEnd(ıconLocation);
                    statu=1;
                }   
                if((i+3<size) && (j-3>=0) && (gameCell[i][j].getType() !=0) 
                && (gameCell[i][j].getType()==gameCell[i+1][j-1].getType())
                && (gameCell[i+1][j-1].getType()== gameCell[i+2][j-2].getType()) 
                && (gameCell[i+2][j-2].getType()== gameCell[i+3][j-3].getType()))
                {// sağ üstten  sol alta tarama yapan kısım 
                    ıconLocation=new int[]{i,j,i+1,j-1,i+2,j-2,i+3,j-3};
                    setIconsEnd(ıconLocation);
                    statu=1;
                }
                if(statu==1)
                {
                    return true;
                }              
            }
        }
        return false;
    }
     private void playComputer()
    {
        hedef = 0;
        statu=1;
        statu2=1;
        for(int i=0;i<size;i++)
        {// Uygun ihtimaller sağlandığında ve oynan istenen yer legal bir konum ise bu kontrolleri yaparak hamle yapıyorum.
            for(int j=0;j<size;j++)
            {   
                /// XXX. / OOO. / .OOO / .XXX  DURUMLARINDA OYNAMA
                if(statu==1) auxiliarXXX_OOO(i,j);
                //// XX.X / OO.O DURUMLARINDA OYNAMA
                if(statu==1) auxiliarXX_X_OO_O(i,j);
                //X.XX / O.OO DURUMLARINDA OYNAMA ///
                if(statu==1) auxiliarX_XX_O_OO(i,j);
                ////// ÇAPRAZ DURUMLAR İÇİN OYNAMA
                if(statu==1) auxiliarVertical(i,j);
                
                if(statu==1 && (j+3<size) && (i+4 <= size)&& (gameCell[i][j].getType()!=0) 
                && (gameCell[i][j].getType()==gameCell[i+1][j+1].getType()) 
                && (gameCell[i+1][j+1].getType()==gameCell[i+2][j+2].getType())   	
                && ((i+4==size) || (gameCell[i+4][j+3].getType()==1) 
                   || (gameCell[i+4][j+3].getType()==2)))
                {
                    if(i>0 && j>0 && (j+3<size)&&(i+3<size)&& (gameCell[i+3][j+3].getType()!=0) 
                       && ( colPosition[j-1]!=-1)){////??????????????
                        hedef=j-1;
                        statu=0;                                 //    .         |   X
                    }                                            //      X       |    X
                    else if((j+3<size) && colPosition[j+3]!=-1){ //       X      |     X        
                        hedef=j+3;                               //        X     |      .
                        statu=0;         
                    }
                }
                if(statu==1 &&  (j-2>=0) && (i+2 <size) && (gameCell[i][j].getType()!=0) 
                && (gameCell[i][j].getType() == gameCell[i+1][j-1].getType()) 
                && (gameCell[i+1][j-1].getType()==gameCell[i+2][j-2].getType()) 
            	&& ((i+4==size) ||((i+4<size) && (gameCell[i+4][j-3].getType()==1)) || ((i+4<size)&&(gameCell[i+4][j-3].getType()==2))))
                { 
                    if((i-1>=0) && (j+1 < size) && (gameCell[i-1][j+1].getType()!=0) && (colPosition[j+1]!=-1)){
                        hedef=j+1;
                        statu=0;                                    //        .    |         X
                    }                                               //       X     |        X
                    else if ((j-3>=0) && (colPosition[j-3]!=-1)){   //      X      |       X             
                        hedef=j-3;                                  //     X       |      .
                        statu=0;        
                    }
                }
                if((statu==1) && (j+3<size) && (i+3 <size) && (gameCell[i][j].getType()!=0) 
                && (gameCell[i][j].getType()==gameCell[i+1][j+1].getType()) 
                && (gameCell[i+1][j+1].getType()==gameCell[i+3][j+3].getType()) 
            	&& (gameCell[i+3][j+2].getType()==1 || gameCell[i+3][j+2].getType()==2))
                {                
                    if((j+3<size) && (gameCell[i+2][j+2].getType()!=0) && (colPosition[j+3]!=-1)){
                        hedef=j+3;
                        statu=0;                                    //     X    
                    }                                               //      X
                    else if(colPosition[j+2]!=-1){    		    //       .     
                        hedef=j+2;                           	    //        X
                        statu=0;
                    }
                }
            }
        }
        ///// YENME DURUMUNU SAĞLAYACAK İHTİMALLER OLMADIĞINDA OYNAYACAK KISIM
        if(statu==1 || colPosition[hedef]==-1)
        {
            for(int i=0;i<size;i++)
            {
                for(int j=0;j<size;j++)
                {
                    if(gameCell[i][j].getType()==1 || gameCell[i][j].getType()==2){
                        if((i==0) && (j+1<size)&& (colPosition[j+1]!=-1)) hedef=j+1;
                        else if((colPosition[j]!=-1)) hedef = j;
                        else if((j>0) && (colPosition[j-1]!=-1)) hedef=j-1;
                        else if((gameCell[i][j].getType()==0) && (colPosition[j]!=-1)) hedef=j;
                   }
                }
            }
           
        }
        //yukarıdan hatalı bir hedef gelirse onu yapalayıp başka bir değerle değiştiriyorum
        if((hedef>=size) || (hedef<0 || colPosition[hedef]==-1))
        {
            for(int i=0; i<size;i++)
            {           
                if(colPosition[i]!=-1) hedef = i;
            }
        }
        if(size>7)gameCell[colPosition[hedef]][hedef].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
        else gameCell[colPosition[hedef]][hedef].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
        gameCell[colPosition[hedef]][hedef].SetType(2);
        colPosition[hedef]--;
        siram++;             
    }
    // play computer methodu için yardımcı method
    private void auxiliarXXX_OOO(int i,int j)
    {
        if((j+3<size) && (gameCell[i][j].getType()!=0) 
        && (gameCell[i][j].getType()==gameCell[i][j+1].getType()) 
        && (gameCell[i][j+1].getType()==gameCell[i][j+2].getType()) 
        && ((i+1==size) || ((i+1<size) && gameCell[i+1][j+3].getType()!=0)
            || ((j>0) && gameCell[i+1][j-1].getType()!=0)))
        {
            if((j > 0) &&( colPosition[j-1]!=-1)){ 
                hedef= j-1;
                statu=0;
                statu2=0;
            }
            else if ((j+3<size) && (colPosition[j+3]!=-1)){
                hedef=j+3;
                statu=0;
                statu2=0;
            }
        }
    }
    // play computer için yardımcı fonksiyon XX_X durumları için
    private void auxiliarXX_X_OO_O(int i,int j)
    {
        if((j+3<size)&&(gameCell[i][j].getType()!=0) 
        && (gameCell[i][j].getType()==gameCell[i][j+1].getType()) 
        && (gameCell[i][j+1].getType()==gameCell[i][j+3].getType()) 
        && ((i+1==size) || ((i+1<size)&&gameCell[i+1][j+2].getType()==1)
           || ((i+1<size)&&gameCell[i+1][j+2].getType()==2)))
        {        
            if((j>0) && (gameCell[i][j+2].getType()!=0 )&& (colPosition[j-1]!=-1)){
                hedef= j-1;
                statu2=0;
                statu=0;
            }    
            else if((j+2<size) && (colPosition[j+2]!=-1)){
                hedef=j+2;
                statu=0;
                statu2=0;
            }
        }  
    }
    //play komputer için yardımcı fonksiyon x_xx durumları için
    private void auxiliarX_XX_O_OO(int i,int j)
    {
        if((j+3<size)&& (gameCell[i][j].getType()!=0) 
        && (gameCell[i][j].getType()==gameCell[i][j+2].getType()) 
        && (gameCell[i][j+2].getType()==gameCell[i][j+3].getType()) 
        && ((i+1==size) || ((i+1<size) && gameCell[i+1][j+1].getType()==1)
           || ((i+1<size) && gameCell[i+1][j+1].getType()==2)))
        {
	    if((gameCell[i][j+1].getType()==0) && (colPosition[j+1]!=-1)){
	        hedef= j+1;
	        statu2=0;
	        statu=0;
	    }        
	    else if((j-1>=0) &&(colPosition[j-1]!=-1)){
	        hedef=j-1;
	        statu=0;
	    }
	}   
    }
    // play computer için dikey yardımcı fonksiyon
    private void auxiliarVertical(int i,int j)
    {
        if((i+2<size) && (gameCell[i][j].getType()!=0) 
        && (gameCell[i][j].getType()== gameCell[i+1][j].getType()) 
        && (gameCell[i+1][j].getType()== gameCell[i+2][j].getType()))
        {
	    if((j+1<size) && (i>0) &&((gameCell[i-1][j].getType()==1) 
               || (gameCell[i-1][j].getType()==2)) && (colPosition[j+1]!=-1)){ 
	        hedef=j+1;
	        statu2=0;
	        statu=0;
	    }
	    else if(colPosition[j]!=-1){
	        hedef=j;
	        statu=0;
	    }
	}	
    }
    // oyunun ana işleme fonksiyonu
    public boolean playGame()
    {
        if(siram==(size*size))
        {
            JOptionPane.showMessageDialog( null,"Mape Doldu Kazanan Yok");
            return true;
        }
        if(siram%2==1 && GameType == 1 )playComputer();
        // koşul oyunun bitme durumu olduğu için onu sağlarını true returne ediyor
        if(finishVertical())return true;
        if(finishHorizontal()) return true;
        if(diagonal())return true;
        
        // en satırdaki sıranın hangi kullanıca olduğu gösteren labelların ayarlanması
        if(siram%2==0){
            userN[1].setForeground(Color.red);
            userN[1].setFont(f1); 
            userN[size-2].setForeground(Color.black);
            userN[size-2].setFont(f2); 
        }
        else if(siram%2==1)
        {
            userN[1].setFont(f2); 
            userN[1].setForeground(Color.black);
            userN[size-2].setForeground(Color.red);
            userN[size-2].setFont(f1); 
        }        
        return false;               
    }
    private void setIconsEnd(int [] a)// bitme koşulunu sağlayan cellerin ıconları değiştiren ve
    {                                 // Bitme mesajını basan method
        if(size>7)
        { // ıcon boyutları size a göre şekil alıyor 7 kritik nokta
            gameCell[a[0]][a[1]].jl.setIcon(new ImageIcon(getClass().getResource("kazanma2.png")));
            gameCell[a[2]][a[3]].jl.setIcon(new ImageIcon(getClass().getResource("kazanma2.png")));
            gameCell[a[4]][a[5]].jl.setIcon(new ImageIcon(getClass().getResource("kazanma2.png")));
            gameCell[a[6]][a[7]].jl.setIcon(new ImageIcon(getClass().getResource("kazanma2.png")));
        }
        else 
        {
            gameCell[a[0]][a[1]].jl.setIcon(new ImageIcon(getClass().getResource("kazanma.png")));
            gameCell[a[2]][a[3]].jl.setIcon(new ImageIcon(getClass().getResource("kazanma.png")));
            gameCell[a[4]][a[5]].jl.setIcon(new ImageIcon(getClass().getResource("kazanma.png")));
            gameCell[a[6]][a[7]].jl.setIcon(new ImageIcon(getClass().getResource("kazanma.png")));
        }
       
        if(GameType == 2 && (gameCell[a[0]][a[1]].getType()==2)) JOptionPane.showMessageDialog( null,"TEBRİKLER USER 2 KAZANDI");
        else if(GameType == 1 && (gameCell[a[0]][a[1]].getType()==2)) JOptionPane.showMessageDialog( null,"BİLGİSAYAR KAZANDI");
        else if(gameCell[a[0]][a[1]].getType()==1) JOptionPane.showMessageDialog( null,"TEBRİKLER USER 1 KAZANDI");
    }
 
    public static void main(String [] args)
    { 
        String gametype;
        String boardSize;
        String cop ="batuhan";
        int answer1,answer2;
 
        int statu=1;
        int statu2=1;
        int check=0;
        try 
        {
            // Hatalı giriş durumlarına karşı girdi alma işlemini loop içerisinde yapıyorum
            gametype = JOptionPane.showInputDialog(null,"-1- SINGLE PLAYER / -2- MULTIPLAYER");
            restart:while(true)
            {   // game type doğru girildiyse hatalı size da tekrar tip sormaması  için
                if(check==1)gametype = JOptionPane.showInputDialog(null,"-1- SINGLE PLAYER / -2- MULTIPLAYER"); 
             
                if(!(gametype.equals("1") || gametype.equals("2")))
                {
                    answer1=0;
                    gametype = JOptionPane.showInputDialog(null,"-1- SINGLE PLAYER / -2- MULTIPLAYER");
                }
                else{
                    statu =0;
                    check = 0;
                    boardSize = JOptionPane.showInputDialog(null,"ENTER BOARD SİZE (4-10)");
                    while(statu2==1)
                    {                         
                        answer2 = Integer.valueOf(boardSize);//Burada string gelme hata durumunu henüz çözemedim
                        if(answer2>= 4 && answer2 <=10 )
                        {
                            answer1 = Integer.parseInt(gametype);                                
                            ConnectFour game = new ConnectFour(answer1,answer2);
                            game.domaze();
                            while(!(game.playGame()))
                            {
                             // oyunun işleme süreci döngü kırılımca oyun bitmiş demektir
                            }
                            // bu adıma geçmesi demek oyun bitmiştir demektir
                            int confirm = JOptionPane.showOptionDialog(null,"Tekrar Oynamak İstermisin ?",
                               null,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, null, null);
                            // oyun bittikten sonra tekrar başa dönebilmesi için atlama yöntemi kullanıyorum
                            if (confirm != 1)
                            {
                                game.dispose();
                                check=1;
                                statu=1;
                                statu2=1;
                                continue restart;//ana föngünün oraya tekrar dönüp oyunu yeniden başlatıyorum
                            }
                            else 
                            {
                                game.dispose();
                                statu=1;
                                break restart;
                            }
                        }
                        else
                        {               
                            answer2=0;
                            boardSize = JOptionPane.showInputDialog("ENTER BOARD SİZE (4-10)");
                        }
                    }
                }
            }
        }
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog( null,"HATALI BİR GİRİŞ YAPTINIZ LÜTFEN RAKAM GİRİN");
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog( null,"OYUN KAPANIYOR...");
        }
    }
    // butonlara bir loop içerisinde işlem tanımlayamadığım için böyle yapıyorum teslimden 
    // sonra daha çok araştırıp öğrenicem 
    private void doact0()
    {  
        buttons[0].addActionListener(new ActionListener()
        {
            int statu=1;
            public void actionPerformed(ActionEvent e)
            {
                if(colPosition[0]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1)
                {
                    if(siram%2==0)
                    {
                        if(size>7)gameCell[colPosition[0]][0].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[0]][0].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[0]][0].SetType(1);
                    }
                    else if(siram%2==1)
                    {
                        if(size>7)gameCell[colPosition[0]][0].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[0]][0].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[0]][0].SetType(2);
                    }
                    colPosition[0]--;
                    siram++;                     
                }
            }
        });
        buttons[1].addActionListener(new ActionListener()
        {
            int statu=1;
            public void actionPerformed(ActionEvent e)
            {
                if(colPosition[1]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1)
                {
                    if(siram%2==0)
                    {
                        if(size>7)gameCell[colPosition[1]][1].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[1]][1].jl.setIcon(new ImageIcon(getClass().getResource("user11.png"))); 
                        gameCell[colPosition[1]][1].SetType(1);
                    }
                    else if(siram%2==1)
                    {
                        if(size>7) gameCell[colPosition[1]][1].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[1]][1].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[1]][1].SetType(2);
                    }
                    colPosition[1]--;
                    siram++;                     
                }
            }
        });
        buttons[2].addActionListener(new ActionListener()
        {
            int statu=1;           
            public void actionPerformed(ActionEvent e)
            {
                if(colPosition[2]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1)
                {
                    if(siram%2==0)
                    {
                        if(size>7)gameCell[colPosition[2]][2].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[2]][2].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[2]][2].SetType(1);
                    }
                    else if(siram%2==1)
                    {
                        if(size>7) gameCell[colPosition[2]][2].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[2]][2].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[2]][2].SetType(2);
                    }
                    colPosition[2]--;
                    siram++;                     
                }
            }
            //durum = finish();
        });
        buttons[3].addActionListener(new ActionListener()
        {
            int statu=1;
            public void actionPerformed(ActionEvent e)
            {

                if(colPosition[3]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1)
                {
                    if(siram%2==0)
                    {
                        if(size>7)gameCell[colPosition[3]][3].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[3]][3].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[3]][3].SetType(1);
                    }
                    else if(siram%2==1)
                    {
                        if(size>7) gameCell[colPosition[3]][3].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[3]][3].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[3]][3].SetType(2);
                    }
                    colPosition[3]--;
                    siram++;                     
                }
            }
        }); 
    }
    private void doact4(){// butonlara bir loop içerisinde işlem tanımlayamadığım için böyle yapıyorum
        buttons[4].addActionListener(new ActionListener()
        {
            int statu=1;           
            public void actionPerformed(ActionEvent e){
                if(colPosition[4]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1){
                    if(siram%2==0){
                        if(size>7) gameCell[colPosition[4]][4].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[4]][4].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[4]][4].SetType(1);
                    }
                    else if(siram%2==1){
                        if(size>7) gameCell[colPosition[4]][4].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[4]][4].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[4]][4].SetType(2);
                    }
                    colPosition[4]--;
                    siram++;                     
                }
            }
        });
    }
    private void doact5(){// butonlara bir loop içerisinde işlem tanımlayamadığım için böyle yapıyorum
        buttons[5].addActionListener(new ActionListener(){
            int statu=1;
            public void actionPerformed(ActionEvent e){
                if(colPosition[5]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1){
                    if(siram%2==0){
                        if(size>7)gameCell[colPosition[5]][5].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[5]][5].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[5]][5].SetType(1);
                    }
                    else if(siram%2==1){
                        if(size>7) gameCell[colPosition[5]][5].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[5]][5].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[5]][5].SetType(2);
                    }
                    colPosition[5]--;
                    siram++;                     
                }
            }
         });
    }
    private void doact6(){// butonlara bir loop içerisinde işlem tanımlayamadığım için böyle yapıyorum
        buttons[6].addActionListener(new ActionListener(){
            int statu=1;
            public void actionPerformed(ActionEvent e){
                if(colPosition[6]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1){
                    if(siram%2==0){
                        if(size>7) gameCell[colPosition[6]][6].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[6]][6].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[6]][6].SetType(1);
                    }
                    else if(siram%2==1){
                        if(size>7) gameCell[colPosition[6]][6].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[6]][6].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[6]][6].SetType(2);
                    }
                    colPosition[6]--;
                    siram++;                     
                }
            }
        });
    }
    private void doact7(){// butonlara bir loop içerisinde işlem tanımlayamadığım için böyle yapıyorum
        buttons[7].addActionListener(new ActionListener(){
            int statu=1;
            public void actionPerformed(ActionEvent e){      
                if(colPosition[7]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİNE");
                    statu =0;
                }
                if(statu==1){
                    if(siram%2==0){
                        if(size>7) gameCell[colPosition[7]][7].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[7]][7].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[7]][7].SetType(1);
                    }
                    else if(siram%2==1){
                        if(size>7) gameCell[colPosition[7]][7].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[7]][7].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[7]][7].SetType(2);
                    }
                    colPosition[7]--;
                    siram++;                     
                }
            }
        });    
    }
    private void doact8(){// butonlara bir loop içerisinde işlem tanımlayamadığım için böyle yapıyorum
        buttons[8].addActionListener(new ActionListener(){
            int statu=1;
            public void actionPerformed(ActionEvent e){      
                if(colPosition[8]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1){
                    if(siram%2==0){
                        if(size>7)gameCell[colPosition[8]][8].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[8]][8].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[8]][8].SetType(1);
                    }
                    else if(siram%2==1){
                        if(size>7) gameCell[colPosition[8]][8].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[8]][8].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[8]][8].SetType(2);
                    }
                    colPosition[8]--;
                    siram++;                     
                }
            }
        });    
    }
    private void doact9(){// butonlara bir loop içerisinde işlem tanımlayamadığım için böyle yapıyorum
        buttons[9].addActionListener(new ActionListener(){
            int statu=1;
            public void actionPerformed(ActionEvent e){      
                if(colPosition[9]==-1){
                    JOptionPane.showMessageDialog( null,"BU SUTÜN DOLU BAŞKA BİR SÜTUN SEÇİN");
                    statu =0;
                }
                if(statu==1){
                    if(siram%2==0){
                        if(size>7) gameCell[colPosition[9]][9].jl.setIcon(new ImageIcon(getClass().getResource("user122.png")));
                        else gameCell[colPosition[9]][9].jl.setIcon(new ImageIcon(getClass().getResource("user11.png")));
                        gameCell[colPosition[9]][9].SetType(1);
                    }
                    else if(siram%2==1){
                        if(size>7) gameCell[colPosition[9]][9].jl.setIcon(new ImageIcon(getClass().getResource("user221.png")));
                        else gameCell[colPosition[9]][9].jl.setIcon(new ImageIcon(getClass().getResource("user12.png")));
                        gameCell[colPosition[9]][9].SetType(2);
                    }
                    colPosition[9]--;
                    siram++;                     
                }
            }
        });    
    }    
}