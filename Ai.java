package com.rockzhai.ai;
/**
 * AI算法和主函数入口
 */
import java.util.*;  
import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;  

public class Ai{  
	//
    private static DrawingPanel panel=new DrawingPanel(700,700);  
    private static Graphics g=panel.getGraphics();  
    public static boolean isBlack=false;//标志棋子的颜色  
    public static int[][] chessBoard=new int[17][17]; //棋盘棋子的摆放情况：0无子，1黑子，－1白子  
    private static HashSet<Point> toJudge=new HashSet<Point>(); // ai可能会下棋的点
    private static HashSet<Point> open=new HashSet<Point>(); //算杀的tojudge
    //因为可以有8个方位，所以对应的行和列的变化就有8种
    private static int dr[]=new int[]{-1,1,-1,1,0,0,-1,1}; // 关于行的8个方向向量 ， 
    private static int dc[]=new int[]{1,-1,-1,1,-1,1,0,0}; //关于列的8个方向向量  
    public static final int MAXN=1<<28;  
    public static final int MINN=-MAXN;    
    private static int searchDeep=2;    //搜索深度 
    private static int searchDeep2=2; 
    private static final int size=15;   //棋盘大小  
    public static boolean isFinished=false;  
    public static boolean flag5 = false;
    public static boolean flag6 = true;
    public static boolean flag8 = false;
    public static boolean flag9 = false;
    public static int count2 = 0;
    public static boolean flag10  = true;
    public static void main(String[] args){  
        MyMouseEvent myMouseEvent=new MyMouseEvent();  
        panel.addMouseListener(myMouseEvent);  
        initChessBoard();  
    }  
  
    // 初始化函数，绘图  
    public static void initChessBoard(){  
  
        isBlack=false;  
        toJudge.clear();  
        panel.clear();  
        panel.setBackground(Color.GRAY);  
        g.setColor(Color.BLACK);  
        for(int i=45;i<=675;i+=45){  
            g.drawLine(45,i,675,i);  
            g.drawLine(i,45,i,675);  
        }  
        // 棋盘上的五个定位基本点，图中的小圆圈  
        g.setColor(Color.BLACK);  
        g.fillOval(353,353,14,14);  
        g.fillOval(218,218,14,14);  
        g.fillOval(488,218,14,14);  
        g.fillOval(488,488,14,14);  
        g.fillOval(218,488,14,14);  
        // 初始化棋盘  
        for(int i=1;i<=15;++i)  
            for(int j=1;j<=15;++j)  
                chessBoard[i][j]=0;  
        // ai先手  
        g.fillOval(337,337,45,45);  
        chessBoard[8][8]=1;  //ai下棋的位置
        //对当前节点进行扩展，将其加入当tojudge队列中，
        for(int i=0;i<8;++i)  
            if(1<=8+dc[i] && 8+dc[i]<=size && 1<=8+dr[i] && 8+dr[i]<=size){  
                Point now=new Point(8+dc[i],8+dr[i]);  
                if(!toJudge.contains(now))  
                    toJudge.add(now);  
            }  
        isBlack=false;  
    }  
  
    // 通过点击事件，得到棋子位置进行下棋  
    public static void putChess(int x,int y){  
        if(isBlack)  
            g.setColor(Color.BLACK);  
        else   
            g.setColor(Color.WHITE);  
        g.fillOval(x-22,y-22,45,45);  //填充像素点
        chessBoard[y/45][x/45]=isBlack?1:-1;  
        if(isEnd(x/45,y/45)){  //判断是否结束
            String s=Ai.isBlack?"黑子胜":"白子胜";  
            JOptionPane.showMessageDialog(null,s);  
            isBlack=true;  
            initChessBoard();  
        }  
        else{  
            Point p=new Point(x/45,y/45);  
            if(toJudge.contains(p))  
                toJudge.remove(p);  
            for(int i=0;i<8;++i){  //对新加入的节点进行扩展
                Point now=new Point(p.x+dc[i],p.y+dr[i]);  
                if(1<=now.x && now.x<=size && 1<=now.y && now.y<=size && chessBoard[now.y][now.x]==0)  
                    toJudge.add(now);  
            }  
        }  
    }  
  
    // ai博弈入口函数  
    public static void myAI(){  
        Node node=new Node();  
        flag5=false;
        searchDeep = 2;
        int[][] score = new int[17][17]; 
        for(int i=1;i<=size;i++) {
        	for(int j=1;j<=size;j++) {
        		score[i][j]=0;
        	}
        }
        int res1 = getMark(score);
        dfs(0,node,MINN,MAXN,null,res1,score);  
        System.out.println("count : "+count2);
        System.out.println("flag10 : "+flag10);
        System.out.println("searchDeep : "+searchDeep);
        System.out.println("------------------------------------------over-----------------------------------------------");
        Point now=node.bestChild.p;  
        // toJudge.remove(now);  
        putChess(now.x*45,now.y*45);  
        isBlack=false;  //又换成人类下
    }  
    
    // alpha beta dfs  
    private static void dfs(int deep,Node root,int alpha,int beta,Point p,int res1,int[][] score1){  
        if(deep==searchDeep){  
        	if(flag5) {
        		 root.mark=res1;  //获取res的值，即估值函数中f（x）的值
        		 flag10 = false;
        		 //System.out.println("deeeeeeep-------------------------------------------------------------------------------------"+deep);
                 System.out.println("mark: "+root.mark);
                 System.out.println("alpha1: "+alpha);
                 System.out.println("beta1: "+beta);
                 return;  
        	}
    		else if(searchDeep<=2){
        		searchDeep +=2;
        	}
        	else if(searchDeep==4) 
        	{
        		 root.mark=res1;  //获取res的值，即估值函数中f（x）的值
                 System.out.println("mark: "+root.mark);
                 System.out.println("alpha1: "+alpha);
                 System.out.println("beta1: "+beta);
                 return; 
        	}
        }  
        count2++; 
        ArrayList<Point> judgeSet=new ArrayList<Point>(); 
        int[][] score = new int[17][17]; 
        for(int i=1;i<=size;i++) {
        	for(int j=1;j<size;j++) {
        		score[i][j]=0;
        	}
        }
     //   ArrayList<Point> judgeSet2=new ArrayList<Point>(); //记录当前节点的临近节点
        //对tojudge队列中得到的节点进行遍历，将得到的节点加入judegset表中
        Iterator it=toJudge.iterator();  //迭代器，可以从两个方向进行遍历
        while(it.hasNext()){  //判断是否有元素
            Point now=new Point((Point)it.next());  //取出第一个元素
            judgeSet.add(now);  
        }  

    	int[] d = new int[10000];
    	for(int i = 0;i<10000;i++) 
    	  d[i] = 0;
         //对上面产生的judegset表进行遍历，
        it=judgeSet.iterator(); //从后往前
        int count = 0;
        //取出里面的每一个元素，建立父与子节点之间的联系，并且对这个节点再次进行扩展，在递归调用这个函数，直到deep=4，也就是其至多只能往前看四步
        while(it.hasNext()) {//先对每一层待搜索的节点先估算出节点的估计函数值，然后在根据min和max层进行排序
        	Point a = new Point((Point)it.next()); 
        	Node b=new Node(); 
        	b.setPoint(a); 
        	chessBoard[a.y][a.x]=((deep&1)==1)?-1:1;
        	int c = getMark3(score1,a.x,a.y,res1,score);//有问题
            d[count]=c;
            count++;
            chessBoard[a.y][a.x]=0;
        }         
        //改成每次取出那个极值
        boolean flag4 = true;
        while(flag4){  //这个位置确定极值的下标
        	int min=MAXN;
        	int max=MINN;
        	int index=0;
        	if((deep&1)==1) {//奇数层，说明找找最小
          		min= d[0];
        		index = 0;
        		for (int i=1;i<count ;i++) {
        			if(min >d[i]) {
        				min = d[i];
        				index = i;
        			}
        		}
        	}
        	else {
        		max = d[0];
        	    index =0;
        	    for (int i=1;i<count ;i++) {
        			if(max < d[i]) {
        				max = d[i];
        				index = i;
        			}
        	    }
        	}
        	for(int i=0;i<count;i++) {
        		System.out.println("d["+i+"]: "+d[i]);
        	}
        	System.out.println("deep1:"+deep);
        	System.out.println("index:"+index);
            Point now=new Point((Point)judgeSet.get(index));  
            Node node=new Node();  
            node.setPoint(now);  
            root.addChild(node);  
            //判断当前取出的节点是不是由当前节点产生的。
            boolean flag=toJudge.contains(now);  
            chessBoard[now.y][now.x]=((deep&1)==1)?-1:1;  //判断当前该谁下，
            if(isEnd(now.x,now.y)){  //找到五连直接返回，不需要再便利其他的了
                root.bestChild=node;  
                root.mark=100000*chessBoard[now.y][now.x];  
                chessBoard[now.y][now.x]=0;    
                flag5=true;//说明找到最优解
                //System.out.println("-----------------------------best------------------------------------------"+deep);
                //把judgeset中的这个节点去掉
                return;  
            }  
            int t=getMark3(score1,now.x,now.y,res1,score);
            //对新得到的节点再次进行扩展
            boolean flags[]=new boolean[8]; //标记回溯时要不要删掉 
            Arrays.fill(flags,true);  
            for(int i=0;i<8;++i){  
                Point next=new Point(now.x+dc[i],now.y+dr[i]);  
                if(1<=now.x+dc[i] && now.x+dc[i]<=size && 1<=now.y+dr[i] && now.y+dr[i]<=size && chessBoard[next.y][next.x]==0){  
                    if(!toJudge.contains(next)){  
                        toJudge.add(next);  
                        //judgeSet2.add(next);
                    }  
                    else flags[i]=false;  //也就是说这个节点已经被其他的节点进行扩展了，所以回溯的时候就不应该删掉
                }  
            }  
            //如果tojudeg队列中的某个节点已经被扩展了就把这个节点去掉。相当于open表
            if(flag)   
                toJudge.remove(now);  
            //对新加进来的节点再次进行递归调用。即往前看一步
            deep++;
            dfs(deep,root.getLastChild(),alpha,beta,now,t,score);  //此时的root已经变成由。并且完成了父节点到子节点传输的alpha。beta
            deep--;
            //这个地方写把原来的极值去掉
            if((deep&1)==1)
                d[index]=MAXN;
            else 
                d[index]=MINN;
            if((deep&1)==1) {
            	for(int i = 0;i<count;i++) {
            		if(d[i]!=MAXN)
            			break;
            		else if(i==count-1)
            			flag4 = false;
            	}
            }
            else {
            	for(int i = 0;i<count;i++) {
            		if(d[i]!=MINN)
            			break;
            		else if(i==count-1)
            			flag4 = false;
            	}
            }
            chessBoard[now.y][now.x]=0;  
            if(flag)  
                toJudge.add(now);  
            for(int i=0;i<8;++i)  
                if(flags[i])  //这些flags[i]=true的节点都是由这个节点扩展的，所以回溯要删掉
                    toJudge.remove(new Point(now.x+dc[i],now.y+dr[i]));  
            // alpha beta剪枝  
            // min层 
            //注意这里的root泛指父节点
            if((deep&1)==1){  
                if(root.bestChild==null || root.getLastChild().mark<root.bestChild.mark){  //min层总是会选取子结点中，数最小的那个
                	System.out.println("sunyue");
                    root.bestChild=root.getLastChild();  
                    root.mark=root.bestChild.mark;  
//                    if(root.mark<=MINN) 
//                        root.mark+=deep;  
                    beta=Math.min(root.mark,beta);//在min层要改变beta  当返回的时候其父节点的alpha和beta都会发生相应的改变，完成了子节点向父节点传输了alpha和beta
                }  
                if(root.mark<=alpha)  
                    return; //如果这个节点的alpha>beta,那么不会给max带来任何的好处，所以不在遍历这个节点的其他子节点。所以return；
            }  
            // max层  
            else{  
                if(root.bestChild==null || root.getLastChild().mark>root.bestChild.mark){  //min层总是会选取子结点中，数最大的那个
                    root.bestChild=root.getLastChild();  
                    root.mark=root.bestChild.mark;  
                    alpha=Math.max(root.mark,alpha);  //在max层要改变alpha
                }  
                if(root.mark>=beta)  
                    return;  
            }
            System.out.println("deep: "+deep);
            System.out.println("alpha: "+alpha);
            System.out.println("beta: "+beta);
          //  System.out.println("flag4: "+flag4);
        }       	
        if(deep==0&&flag5==false) {
  	        flag8=false;
            searchDeep2=2;
            suansha(0,root,MINN,MAXN,null,res1,score1);
           
      }

}  
  public static void suansha(int deep,Node root,int alpha,int beta,Point p,int res1,int[][] score1) {
      if(deep==searchDeep2) {
    	  if(flag8) {//在这个深度已经搜索到了最优，所以不用继续深入
     		 root.mark=res1;  
              System.out.println("--------------------------------mark: "+root.mark);
              //flag9=true;
              System.out.println("--------------------------------deep: "+deep);
              System.out.println("--------------------------------alpha: "+alpha);
              System.out.println("--------------------------------beta: "+beta);
              return;  
     	}
     	else if(searchDeep2<=6){
     		searchDeep2 +=2;
     	}
     	else if(searchDeep2 == 8) {//没有找到最优解，所以要利用算杀
     		//root.mark=res1;
              return; 
     	} 
      }
     ArrayList<Point> open2= new ArrayList<Point>();
     int[][] score = new int[17][17]; 
     for(int i=1;i<size;i++) {
     	for(int j=1;j<size;j++) {
     		score[i][j]=0;
     	}
     }
	 int[] d = new int[10000];
  	 for(int i = 0;i<10000;i++) 
  	   d[i] = 0;
  	 Iterator it=toJudge.iterator(); //从后往前
      int count = 0;
      int count3 = 0;
      int count4=0;
      //取出里面的每一个元素，建立父与子节点之间的联系，并且对这个节点再次进行扩展，在递归调用这个函数，直到deep=4，也就是其至多只能往前看四步
      while(it.hasNext()) {//先对每一层待搜索的节点先估算出节点的估计函数值，然后在根据min和max层进行排序
    	//先对每一层待搜索的节点先估算出节点的估计函数值，然后在根据min和max层进行排序
	      count3++;	
    	  Point a = new Point((Point)it.next()); 
	      	Node b=new Node(); 
	      	b.setPoint(a); 
	      	chessBoard[a.y][a.x]=((deep&1)==1)?-1:1;
	      	boolean c = getMark2(a.x,a.y,deep);//只选出活3和冲4
	      	if(c) {
	      		open2.add(a);
	      	}
	           chessBoard[a.y][a.x]=0;
      }//在对选出来的部分点进行排序,注意这个地方是对整个局面进行分析
      it=open2.iterator();
      while(it.hasNext()) {
    	  count4++;
    	  Point a = new Point((Point)it.next()); 
          Node b=new Node(); 
          b.setPoint(a); 
          chessBoard[a.y][a.x]=((deep&1)==1)?-1:1;
          int c = getMark3(score1,a.x,a.y,res1,score);
          d[count]=c;
          count++;
          chessBoard[a.y][a.x]=0;
      }
      boolean flag7=true;
      while(flag7) {
    	if(open2.isEmpty()) {
    		System.out.println("-------------------------------count3--------------------------"+count3);
    		System.out.println("-------------------------------suansha----dfs--------------------------"+count4);
    		System.out.println("-------------------------------suansha----dfs--------------------------");
    		System.out.println("-------------------------------suansha----dfs--------------------------");
    		System.out.println("-------------------------------suansha----dfs--------------------------");
    		if(deep==0)
    			break;
    		else
    			return;
    	}
    	int min=MAXN;
      	int max=MINN;
      	int index=0;
      	if((deep&1)==1) {//奇数层，说明找找最小
        		min= d[0];
      		index = 0;
      		for (int i=1;i<count ;i++) {
      			if(min >d[i]) {
      				min = d[i];
      				index = i;
      			}
      		}
      	}
      	else {
      		max = d[0];
      	    index =0;
      	    for (int i=1;i<count ;i++) {
      			if(max < d[i]) {
      				max = d[i];
      				index = i;
      			}
      	    }
      	}     
      	for(int i=0;i<count;i++) {
    		System.out.println("dddddddd["+i+"]: "+d[i]);
    	}
    	System.out.println("deepppppppp:"+deep);
    	System.out.println("indexxxxxxxx:"+index);
      	 Point now=new Point((Point)open2.get(index));  
         Node node=new Node();  
         node.setPoint(now);  
         root.addChild(node);  
         //判断当前取出的节点是不是由当前节点产生的。
         boolean flag=toJudge.contains(now);  
         chessBoard[now.y][now.x]=((deep&1)==1)?-1:1;  //判断当前该谁下，
         if(isEnd(now.x,now.y)){  //找到五连直接返回，不需要再便利其他的了
        	 System.out.println("sunyue");
             root.bestChild=node;  
             root.mark=100000*chessBoard[now.y][now.x];  
             chessBoard[now.y][now.x]=0;    
             flag8=true;
             return; 
         }
        int t = getMark3(score1,now.x,now.y,res1,score);
         boolean flags[]=new boolean[8]; //标记回溯时要不要删掉 
         Arrays.fill(flags,true);  
         for(int i=0;i<8;++i){  
             Point next=new Point(now.x+dc[i],now.y+dr[i]);  
             if(1<=now.x+dc[i] && now.x+dc[i]<=size && 1<=now.y+dr[i] && now.y+dr[i]<=size && chessBoard[next.y][next.x]==0){  
                 if(!toJudge.contains(next)){  
                	chessBoard[next.y][next.x]=((deep&1)==1)?-1:1;
                	boolean c = getMark2(next.x,next.y,deep);//只选出活3和冲4
         	      	if(c) {
         	      		toJudge.add(next);
         	      	} 
         	      	else flags[i]=false;
         	      		
         	      	chessBoard[next.y][next.x]=0;
                 }  
                 else flags[i]=false;  //也就是说这个节点已经被其他的节点进行扩展了，所以回溯的时候就不应该删掉
             }  
         } 
         
         if(flag)   
             toJudge.remove(now);  
         //对新加进来的节点再次进行递归调用。即往前看一步
         deep++;
         suansha(deep,root.getLastChild(),alpha,beta,now,t,score);  //此时的root已经变成由。并且完成了父节点到子节点传输的alpha。beta  
         deep--;
         //这个地方写把原来的极值去掉
         if((deep&1)==1)
             d[index]=MAXN;
         else 
             d[index]=MINN;
         if((deep&1)==1) {
         	for(int i = 0;i<count;i++) {
         		if(d[i]!=MAXN)
         			break;
         		else if(i==count-1)
         			flag7 = false;
         	}
         }
         else {
         	for(int i = 0;i<count;i++) {
         		if(d[i]!=MINN)
         			break;
         		else if(i==count-1)
         			flag7 = false;
         	}
         }
         chessBoard[now.y][now.x]=0;  
         if(flag)  
             toJudge.add(now);  
         for(int i=0;i<8;++i)  {
             if(flags[i])  //这些flags[i]=true的节点都是由这个节点扩展的，所以回溯要删掉
                 toJudge.remove(new Point(now.x+dc[i],now.y+dr[i])); 
         }
         if((deep&1)==1){  
             if(root.bestChild==null || root.getLastChild().mark<root.bestChild.mark){  //min层总是会选取子结点中，数最小的那个
                 root.bestChild=root.getLastChild();  
                 root.mark=root.bestChild.mark;  
                 beta=Math.min(root.mark,beta);//在min层要改变beta  当返回的时候其父节点的alpha和beta都会发生相应的改变，完成了子节点向父节点传输了alpha和beta
             }  
             if(root.mark<=alpha)  
                 return; //如果这个节点的alpha>beta,那么不会给max带来任何的好处，所以不在遍历这个节点的其他子节点。所以return；
         }  
         // max层  
         else{  
             if(root.bestChild==null || root.getLastChild().mark>root.bestChild.mark){  //min层总是会选取子结点中，数最大的那个
                 root.bestChild=root.getLastChild();  
                 root.mark=root.bestChild.mark;  
                 alpha=Math.max(root.mark,alpha);  //在max层要改变alpha
             }  
             if(root.mark>=beta)  
                 return;  
         }
         System.out.println("---------------------------deep"+deep);
         System.out.println("---------------------------alpha: "+alpha);
         System.out.println("---------------------------beta: "+beta);
      }
  }
    public static int getMark(int[][] score){ //对棋盘上的所有棋子进行打分，总和即为当前的局势 
        int res=0;  
        for(int i=1;i<=size;++i){  
            for(int j=1;j<=size;++j){  
                if(chessBoard[i][j]!=0){  
                    int []a= {0,0,0,0,0};
                    int []b= {0,0,0,0,0};
                    // 行  
                    boolean flag1=false,flag2=false;  
                    int x=j,y=i;  
                    int cnt1=1;  
                    int col=x,row=y;  
                    while(--col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt1;  //判断行向右方向有没有颜色相同的棋子
                    if(col>0 && chessBoard[row][col]==0) flag1=true;  //判断行向右的方向有没有被堵住
                    col=x;row=y;  
                    while(++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt1;  //判断行向左的方向有没有相同颜色的棋子
                    if(col<=size && chessBoard[row][col]==0) flag2=true;  //判断行向左的方向有没有被堵住
                    if(cnt1>=5) { res+=100000*chessBoard[i][j]; 
                    score[i][j]+=100000*chessBoard[i][j];
                    continue;}
                    else {
                    	if(flag1&&flag2) {//活的
                    		a[cnt1]++;
                    	}
                    	else if(flag1||flag2) {//半活
                    		b[cnt1]++;
                    	}
                    }
                    // 同上，判断列的方向 
                   col=x;row=y;  
                   int  cnt2=1;flag1=false;flag2=false;  
                    while(--row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt2;  
                    if(row>0 && chessBoard[row][col]==0) flag1=true;  
                    col=x;row=y;  
                    while(++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt2;  
                    if(row<=size && chessBoard[row][col]==0) flag2=true;   
                    if(cnt2>=5) { res+=100000*chessBoard[i][j]; 
                    score[i][j]+=100000*chessBoard[i][j];
                    continue;}
                    else {
                    	if(flag1&&flag2) {//活的
                    		a[cnt2]++;
                    	}
                    	else if(flag1||flag2) {//半活
                    		b[cnt2]++;
                    	}
                    }
                    // 同上，判断左对角线  
                    col=x;row=y;  
                    int cnt3=1;flag1=false;flag2=false;  
                    while(--col>0 && --row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt3;  
                    if(col>0 && row>0 && chessBoard[row][col]==0) flag1=true;  
                    col=x;row=y;  
                    while(++col<=size && ++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt3;  
                    if(col<=size && row<=size && chessBoard[row][col]==0) flag2=true;  
                    if(cnt3>=5) { res+=100000*chessBoard[i][j];
                    score[i][j]+=100000*chessBoard[i][j];
                    continue;}
                    else {
                    	if(flag1&&flag2) {//活的
                    		a[cnt3]++;
                    	}
                    	else if(flag1||flag2) {//半活
                    		b[cnt3]++;
                    	}
                    }
                    // 同上，判断右对角线  
                    col=x;row=y;  
                    int cnt4=1;flag1=false;flag2=false;  
                    while(++row<=size && --col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt4;  
                    if(row<=size && col>0 && chessBoard[row][col]==0) flag1=true;  
                    col=x;row=y;  
                    while(--row>0 && ++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt4;  
                    if(row>0 && col<=size && chessBoard[i][j]==0) flag2=true;  
                    if(cnt4>=5) { res+=100000*chessBoard[i][j]; 
                    score[i][j]+=100000*chessBoard[i][j];
                    continue;}
                    else {
                    	if(flag1&&flag2) {//活的
                    		a[cnt4]++;
                    	}
                    	else if(flag1||flag2) {//半活
                    		b[cnt4]++;
                    	}
                    }
                if(a[4]>=1) {
                	res+=10000*chessBoard[i][j];
                	score[i][j]+=10000*chessBoard[i][j];
                } 	
                else if(b[4]>=1 && a[3]>=1) {
                	res+=10000*chessBoard[i][j];
                	score[i][j]+=10000*chessBoard[i][j];
                }	
                else if(b[4]>=2) {
                	res+=10000*chessBoard[i][j];
                	score[i][j]+=10000*chessBoard[i][j];
                }  	
                else if(a[3]>=2) {
                	res+=5000*chessBoard[i][j];
                	score[i][j]+=5000*chessBoard[i][j];
                }                	
                else if(a[3]>=1  && b[3]>=1) {
                	res+=1000*chessBoard[i][j];
                	score[i][j]+=1000*chessBoard[i][j];
                }               	
                else if (b[4]>=1) {
                	res+=500*chessBoard[i][j];
                	score[i][j]+=500*chessBoard[i][j];
                }                	
                else if(a[3]>=1) {
                	res+=200*chessBoard[i][j];
                	score[i][j]+=200*chessBoard[i][j];
                }             	
                else if(a[2]>=2) {
                	res+=100*chessBoard[i][j];
                	score[i][j]+=100*chessBoard[i][j];
                }               	
                else if(b[3]>=1) {
                	res+=50*chessBoard[i][j];
                	score[i][j]+=50*chessBoard[i][j];
                }               	
                else if(b[2]>=2) {
                	res+=20*chessBoard[i][j];
                	score[i][j]+=20*chessBoard[i][j];
                }   	
                else if(a[2]>=1) {
                	res+=10*chessBoard[i][j];
                	score[i][j]+=10*chessBoard[i][j];
                }        	
                else if(b[2]>=1) {
                	res+=5*chessBoard[i][j];
                	score[i][j]+=5*chessBoard[i][j];
                }
                }  
            }  
        }  
        return res;  
    }  
    public static boolean getMark2(int x,int y,int deep){ //对棋盘上的所有棋子进行打分，总和即为当前的局势 
                int res=0;  
                int score1=0;//人
                int score2=0;//电脑
                boolean flag11 = true;
                if((deep&1)==1) {//min
                	for(int i=1;i<=size;++i){  
                        for(int j=1;j<=size;++j){  //考虑自己和对方的所有活3和冲4的棋
                            if(chessBoard[i][j]!=0){  
                                int []a= {0,0,0,0,0};
                                int []b= {0,0,0,0,0};
                                // 行  
                                boolean flag1=false,flag2=false;   
                                int cnt1=1;  
                                int col=x,row=y;  
                                while(--col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt1;  //判断行向右方向有没有颜色相同的棋子
                                if(col>0 && chessBoard[row][col]==0) flag1=true;  //判断行向右的方向有没有被堵住
                                col=x;row=y;  
                                while(++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt1;  //判断行向左的方向有没有相同颜色的棋子
                                if(col<=size && chessBoard[row][col]==0) flag2=true;  //判断行向左的方向有没有被堵住
                                if(cnt1>=5) { res+=100000*chessBoard[i][j]; 
                                continue;}
                                else {
                                	if(flag1&&flag2) {//活的
                                		a[cnt1]++;
                                	}
                                	else if(flag1||flag2) {//半活
                                		b[cnt1]++;
                                	}
                                }
                                // 同上，判断列的方向 
                               col=x;row=y;  
                               int  cnt2=1;flag1=false;flag2=false;  
                                while(--row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt2;  
                                if(row>0 && chessBoard[row][col]==0) flag1=true;  
                                col=x;row=y;  
                                while(++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt2;  
                                if(row<=size && chessBoard[row][col]==0) flag2=true;   
                                if(cnt2>=5) { res+=100000*chessBoard[i][j]; 
                                continue;}
                                else {
                                	if(flag1&&flag2) {//活的
                                		a[cnt2]++;
                                	}
                                	else if(flag1||flag2) {//半活
                                		b[cnt2]++;
                                	}
                                }
                                // 同上，判断左对角线  
                                col=x;row=y;  
                                int cnt3=1;flag1=false;flag2=false;  
                                while(--col>0 && --row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt3;  
                                if(col>0 && row>0 && chessBoard[row][col]==0) flag1=true;  
                                col=x;row=y;  
                                while(++col<=size && ++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt3;  
                                if(col<=size && row<=size && chessBoard[row][col]==0) flag2=true;  
                                if(cnt3>=5) { res+=100000*chessBoard[i][j]; 
                                continue;}
                                else {
                                	if(flag1&&flag2) {//活的
                                		a[cnt3]++;
                                	}
                                	else if(flag1||flag2) {//半活
                                		b[cnt3]++;
                                	}
                                }
                                // 同上，判断右对角线  
                                col=x;row=y;  
                                int cnt4=1;flag1=false;flag2=false;  
                                while(++row<=size && --col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt4;  
                                if(row<=size && col>0 && chessBoard[row][col]==0) flag1=true;  
                                col=x;row=y;  
                                while(--row>0 && ++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt4;  
                                if(row>0 && col<=size && chessBoard[i][j]==0) flag2=true;  
                                if(cnt4>=5) { res+=100000*chessBoard[i][j]; 
                                continue;}
                                else {
                                	if(flag1&&flag2) {//活的
                                		a[cnt4]++;
                                	}
                                	else if(flag1||flag2) {//半活
                                		b[cnt4]++;
                                	}
                                }
                            if(a[4]>=1) 
                            	res+=10000*chessBoard[i][j];
                            else if(b[4]>=1 && a[3]>=1)
                            	res+=10000*chessBoard[i][j];
                            else if(b[4]>=2)
                            	res+=10000*chessBoard[i][j];
                            else if(a[3]>=2)
                            	res+=5000*chessBoard[i][j];
                            else if(a[3]>=1  && b[3]>=1)
                            	res+=1000*chessBoard[i][j];
                            else if (b[4]>=1)
                            	res+=500*chessBoard[i][j];
                            else if(a[3]>=1)
                            	res+=200*chessBoard[i][j];
                            }  
                        }  
                    }  
                	if(res<0)
                     return false;
                }else {//max
                	for(int i=1;i<=size;++i){  
                        for(int j=1;j<=size;++j){  
                            if(chessBoard[i][j]==1){//只考虑自己  
                            	 int []a= {0,0,0,0,0};
                                 int []b= {0,0,0,0,0};
                                 // 行  
                                 boolean flag1=false,flag2=false;  
                                 int cnt1=1;  
                                 int col=x,row=y;  
                                 while(--col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt1;  //判断行向右方向有没有颜色相同的棋子
                                 if(col>0 && chessBoard[row][col]==0) flag1=true;  //判断行向右的方向有没有被堵住
                                 col=x;row=y;  
                                 while(++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt1;  //判断行向左的方向有没有相同颜色的棋子
                                 if(col<=size && chessBoard[row][col]==0) flag2=true;  //判断行向左的方向有没有被堵住
                                 if(cnt1>=5)  return true;
                                 else {
                                 	if(flag1&&flag2) {//活的
                                 		a[cnt1]++;
                                 	}
                                 	else if(flag1||flag2) {//半活
                                 		b[cnt1]++;
                                 	}
                                 }
                                 // 同上，判断列的方向 
                                col=x;row=y;  
                                int  cnt2=1;flag1=false;flag2=false;  
                                 while(--row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt2;  
                                 if(row>0 && chessBoard[row][col]==0) flag1=true;  
                                 col=x;row=y;  
                                 while(++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt2;  
                                 if(row<=size && chessBoard[row][col]==0) flag2=true;   
                                 if(cnt2>=5) return true;
                                 else {
                                 	if(flag1&&flag2) {//活的
                                 		a[cnt2]++;
                                 	}
                                 	else if(flag1||flag2) {//半活
                                 		b[cnt2]++;
                                 	}
                                 }
                                 // 同上，判断左对角线  
                                 col=x;row=y;  
                                 int cnt3=1;flag1=false;flag2=false;  
                                 while(--col>0 && --row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt3;  
                                 if(col>0 && row>0 && chessBoard[row][col]==0) flag1=true;  
                                 col=x;row=y;  
                                 while(++col<=size && ++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt3;  
                                 if(col<=size && row<=size && chessBoard[row][col]==0) flag2=true;  
                                 if(cnt3>=5) return true;
                                 else {
                                 	if(flag1&&flag2) {//活的
                                 		a[cnt3]++;
                                 	}
                                 	else if(flag1||flag2) {//半活
                                 		b[cnt3]++;
                                 	}
                                 }
                                 // 同上，判断右对角线  
                                 col=x;row=y;  
                                 int cnt4=1;flag1=false;flag2=false;  
                                 while(++row<=size && --col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt4;  
                                 if(row<=size && col>0 && chessBoard[row][col]==0) flag1=true;  
                                 col=x;row=y;  
                                 while(--row>0 && ++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt4;  
                                 if(row>0 && col<=size && chessBoard[row][col]==0) flag2=true;  
                                 if(cnt4>=5) return true;
                                 else {
                                 	if(flag1&&flag2) {//活的
                                 		a[cnt4]++;
                                 	}
                                 	else if(flag1||flag2) {//半活
                                 		b[cnt4]++;
                                 	}
                                 }
                             if((a[4]>=1)||(b[4]>=1 && a[3]>=1)||(b[4]>=2)||(a[3]>=2)||(a[3]>=1  && b[3]>=1)||(b[4]>=1)||(a[3]>=1)) {
                            	 flag11=false;
                             	return true;
                              }  
                            }
                          }
                    } 
                	if(flag11)
                		return false;
                } 
                return true;
    }
    
    public static int getMark3(int[][] score1,int x,int y,int res1,int[][] score){ //对棋盘上的所有棋子进行打分，总和即为当前的局势 
        int res=0;  
        int t=res1;
        int col=x;
        int row=y;
        t+=getMark4(x,y);
        for(int i=1;i<size;i++) {
        	for(int j=1;j<size;j++) {
        		score[i][j]=score1[i][j];
        	}
        }
        while(--col>0) {
        	if(chessBoard[row][col]!=0) {
        		t=t-score1[row][col];
        		score[row][col]=getMark4(col,row);
        		t+=score[row][col];
        	}
        }
        col=x;
        row=y;
        while(++col<=size) {
        	if(chessBoard[row][col]!=0) {
        		t=t-score1[row][col];
        		score[row][col]=getMark4(col,row);
        		t+=score[row][col];
        	}
        }
        col=x;
        row=y;
        while(++row<=size) {
        	if(chessBoard[row][col]!=0) {
        		t=t-score1[row][col];
        		score[row][col]=getMark4(col,row);
        		t+=score[row][col];
        	}
        }
        col=x;
        row=y;
        while(--row>0) {
        	if(chessBoard[row][col]!=0) {
        		t=t-score1[row][col];
        		score[row][col]=getMark4(col,row);
        		t+=score[row][col];
        	}
        }
        col=x;
        row=y;
        while(--col>0 && --row>0) {
        	if(chessBoard[row][col]!=0) {
        		t=t-score1[row][col];
        		score[row][col]=getMark4(col,row);
        		t+=score[row][col];
        	}
        }
        col=x;
        row=y;
        while(++col<=size && ++row<=size) {
        	if(chessBoard[row][col]!=0) {
        		t=t-score1[row][col];
        		score[row][col]=getMark4(col,row);
        		t+=score[row][col];
        	}
        }
        col=x;
        row=y;
        while(++row<=size && --col>0) {
        	if(chessBoard[row][col]!=0) {
        		t=t-score1[row][col];
        		score[row][col]=getMark4(col,row);
        		t+=score[row][col];
        	}
        }
        col=x;
        row=y;
        while(--row>0 && ++col<=size) {
        	if(chessBoard[row][col]!=0) {
        		t=t-score1[row][col];
        		score[row][col]=getMark4(col,row);
        		t+=score[row][col];
        	}
        }
        res=t;
        return res;
    }
    
    public static int getMark4(int x,int y){ //对棋盘上的所有棋子进行打分，总和即为当前的局势 
        int res=0;  
        if(chessBoard[y][x]!=0){  
            int []a= {0,0,0,0,0};
            int []b= {0,0,0,0,0};
            // 行  
            boolean flag1=false,flag2=false;  
            int cnt1=1;  
            int col=x,row=y;  
            while(--col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt1;  //判断行向右方向有没有颜色相同的棋子
            if(col>0 && chessBoard[row][col]==0) flag1=true;  //判断行向右的方向有没有被堵住
            col=x;row=y;  
            while(++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt1;  //判断行向左的方向有没有相同颜色的棋子
            if(col<=size && chessBoard[row][col]==0) flag2=true;  //判断行向左的方向有没有被堵住
            if(cnt1>=5) { res+=100000*chessBoard[y][x]; 
            return res;}
            else {
            	if(flag1&&flag2) {//活的
            		a[cnt1]++;
            	}
            	else if(flag1||flag2) {//半活
            		b[cnt1]++;
            	}
            }
            // 同上，判断列的方向 
           col=x;row=y;  
           int  cnt2=1;flag1=false;flag2=false;  
            while(--row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt2;  
            if(row>0 && chessBoard[row][col]==0) flag1=true;  
            col=x;row=y;  
            while(++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt2;  
            if(row<=size && chessBoard[row][col]==0) flag2=true;   
            if(cnt2>=5) { res+=100000*chessBoard[y][x]; 
            return res;}
            else {
            	if(flag1&&flag2) {//活的
            		a[cnt2]++;
            	}
            	else if(flag1||flag2) {//半活
            		b[cnt2]++;
            	}
            }
            // 同上，判断左对角线  
            col=x;row=y;  
            int cnt3=1;flag1=false;flag2=false;  
            while(--col>0 && --row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt3;  
            if(col>0 && row>0 && chessBoard[row][col]==0) flag1=true;  
            col=x;row=y;  
            while(++col<=size && ++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt3;  
            if(col<=size && row<=size && chessBoard[row][col]==0) flag2=true;  
            if(cnt3>=5) { res+=100000*chessBoard[y][x]; 
            return res;
            }
            else {
            	if(flag1&&flag2) {//活的
            		a[cnt3]++;
            	}
            	else if(flag1||flag2) {//半活
            		b[cnt3]++;
            	}
            }
            // 同上，判断右对角线  
            col=x;row=y;  
            int cnt4=1;flag1=false;flag2=false;  
            while(++row<=size && --col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt4;  
            if(row<=size && col>0 && chessBoard[row][col]==0) flag1=true;  
            col=x;row=y;  
            while(--row>0 && ++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt4;  
            if(row>0 && col<=size && chessBoard[row][col]==0) flag2=true;  
            if(cnt4>=5) { res+=100000*chessBoard[y][x]; 
           return res;}
            else {
            	if(flag1&&flag2) {//活的
            		a[cnt4]++;
            	}
            	else if(flag1||flag2) {//半活
            		b[cnt4]++;
            	}
            }
        if(a[4]>=1) 
        	res+=10000*chessBoard[y][x];
        else if(b[4]>=1 && a[3]>=1)
        	res+=10000*chessBoard[y][x];
        else if(b[4]>=2)
        	res+=10000*chessBoard[y][x];
        else if(a[3]>=2)
        	res+=5000*chessBoard[y][x];
        else if(a[3]>=1  && b[3]>=1)
        	res+=1000*chessBoard[y][x];
        else if (b[4]>=1)
        	res+=500*chessBoard[y][x];
        else if(a[3]>=1)
        	res+=200*chessBoard[y][x];
        else if(a[2]>=2)
        	res+=100*chessBoard[y][x];
        else if(b[3]>=1)
        	res+=50*chessBoard[y][x];
        else if(b[2]>=2)
        	res+=20*chessBoard[y][x];
        else if(a[2]>=1)
        	res+=10*chessBoard[y][x];
        else if(b[2]>=1)
        	res+=5*chessBoard[y][x];
        }  
return res;  
}  
  
    // for debug  
    public static void debug(){  
        for(int i=1;i<=size;++i){  
            for(int j=1;j<=size;++j){  
                System.out.printf("%d\t",chessBoard[i][j]);  
            }  
            System.out.println("");  
        }  
    }  
  
    // 判断是否一方取胜  
    public static boolean isEnd(int x,int y){  
        // 判断一行是否五子连珠  
        int cnt=1;  
        int col=x,row=y;  
        while(--col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
        col=x;row=y;  
        while(++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
        if(cnt>=5){  
            isFinished=true;  
            return true;  
        }  
        // 判断一列是否五子连珠  
        col=x;row=y;  
        cnt=1;  
        while(--row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
        col=x;row=y;  
        while(++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
        if(cnt>=5){  
            isFinished=true;  
            return true;  
        }  
        // 判断左对角线是否五子连珠  
        col=x;row=y;  
        cnt=1;  
        while(--col>0 && --row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
        col=x;row=y;  
        while(++col<=size && ++row<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
        if(cnt>=5){  
            isFinished=true;  
            return true;  
        }  
        // 判断右对角线是否五子连珠  
        col=x;row=y;  
        cnt=1;  
        while(++row<=size && --col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
        col=x;row=y;  
        while(--row>0 && ++col<=size && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
        if(cnt>=5){  
            isFinished=true;  
            return true;  
        }  
        return false;  
    }  
}  
  
  
  
// 树节点  
class Node{  
    public Node bestChild=null;  
    public ArrayList<Node> child=new ArrayList<Node>();  
    public Point p=new Point();  
    public int mark;  
    Node(){  
        this.child.clear();  
        bestChild=null;  
        mark=0;  
    }  
    public void setPoint(Point r){  
        p.x=r.x;  
        p.y=r.y;  
    }  
    public void addChild(Node r){  
        this.child.add(r);  
    }  
    public Node getLastChild(){  
        return child.get(child.size()-1);  
    }  
}  
  
// 实现鼠标事件接口  
class MyMouseEvent implements MouseListener{  
    public void mouseClicked(MouseEvent e){  //鼠标点击
        int x=round(e.getX()),y=round(e.getY());  
        if(x>=45 && x<=675 && y>=45 && y<=675 && Ai.chessBoard[y/45][x/45]==0 && Ai.isBlack==false){  //判断像素点是否在区域内，并判断该点是否已经有棋子，并且该人下棋
            Ai.putChess(x,y);  
            if(!Ai.isFinished){  
                Ai.isBlack=true; //轮到黑子下 
                Ai.myAI();  
            }  
            Ai.isFinished=false;  
        }  
    }  
    // 得到鼠标点击点附近的棋盘精准点  
    public static int round(int x){  
        return (x%45<22)?x/45*45:x/45*45+45;  
    }  
    public void mouseExited(MouseEvent e){}  
    public void mouseEntered(MouseEvent e){}  
    public void mouseReleased(MouseEvent e){}  
    public void mousePressed(MouseEvent e){}  
}
