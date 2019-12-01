import java.util.*;
/*
* author:FHhui
* description:NSGA-2 for 多目标优化问题 one:     two:
*
* */
public class populations {
    //种群类,在这里进行种群级别的具体操作
    //这里的方法应该是以特定的数组作为参数的。
    //Nsga2，啊啊啊啊啊啊啊啊啊啊啊啊啊啊，又开始不想写了
    //鬼知道我打这串注释的时候有多绝望，终终终于快写完了
    //日常不想写（虽然就差一点了），哈哈哈哈，沃特难了
    //冲冲冲，ddl打法，在环境概论的熏陶下写最环保的代码。
    //开始debug之旅
    //先来设定一下这两个函数的值ZDT1
    //哎 bug de到自闭，为啥就从一个解集变成了一个点了呢？？
    private static final boolean debug=false;//debug参数
    private int N=20;//种群的数目
    private double pc=0.6;//发生交叉行为的概率
    private double pm=0.1;//发生变异性为的概率
    private double BestX;//函数1的最佳x
    private double MinX;//函数1的最小X
    //private double BestX2;//函数2的最佳x
    //private double MinX2;//函数1的最小X
    private double BestY1;//函数1的最佳Y
    private double MinY1;//函数1的最小Y
    private double BestY2;//函数2的最佳Y
    private double MinY2;//函数2的最小Y
    private inidival[] pop;//种群的个体
    private inidival[] Cpop;//子种群
    private inidival[] Ppop;//交配种群
    private inidival[] p1;//函数1的排序列表
    private inidival[] p2;//函数2的排序列表
    private int round=500;//迭代次数

    public populations(){
        init();
    }
    private  void init(){
        p1=new inidival[N];
        p2=new inidival[N];
        pop=new inidival[N];
        Cpop=new inidival[N];
        Ppop=new inidival[N];
        //初始化操作，将种群初始化，并将进行非支配排序
        BestX=0;
        BestY1=0;
        MinY1=Integer.MAX_VALUE;
        BestY2=0;
        MinY1=Integer.MAX_VALUE;
        for (int i=0;i<N;i++){
            pop[i]=new inidival();//对种群中的每一个个体进行初始化操作
        }

        paixu(pop);
    }
    private  void choose(){
        if(debug) System.out.println("程序开始选择");
        for(int i=0;i<N;i++){
            //锦标赛选择
            Cpop[i]=new inidival(1);
            int m,n;
            m=(int)(Math.random()*N);
            n=(int)(Math.random()*N);
            if(pop[m].rank>(pop[n].rank)){
                Ppop[i]=pop[m];
            }else if(pop[m].rank==pop[n].rank){
                //如果帕累托等级相同的话，就比较距离
                Ppop[i]=pop[m].distance>pop[n].distance?pop[m]:pop[n];
            }else{
                Ppop[i]=pop[n];
        }
        }

            //这里不是一个三目运算符式子就可以决定的，因为拥挤度的存在
            //选择生成新的父本并把他放入到交配种群中

    }
    private void fast_Nondominated_sort(inidival[] popx){
        if(debug) System.out.println("程序开始快速非支配排序");
        ArrayList<ArrayList<inidival>> F=new ArrayList<>();//帕累托等级列表
        //快速非支配排序，
        ArrayList<inidival> fx=new ArrayList<>();//x等级的列表
        for(int i=0;i<N;i++){
            popx[i].nq=0;
            for (int j=0;j<N-1;j++){
                if (j!=i){
                    if (popx[i].fitness1>popx[j].fitness1&&popx[i].fitness2>popx[j].fitness2){//两个函数均大于
                        //支配关系
                        popx[i].Sp.add(popx[j]);
                    }else if(popx[i].fitness1<popx[j].fitness1&&popx[i].fitness2<popx[j].fitness2){//两个函数均小于
                        //被支配关系
                        popx[i].nq++;
                    }
                }
                if (popx[i].nq==0){//如果没有人可支配
                    popx[i].rank=1;
                    fx.add(popx[i]);
                }
            }
        }
        F.add(fx);
        int i=0;
        while(F.get(i).size() !=0){
            //开始一层一层的去掉帕累托层进行分级
            if(debug) System.out.println("程序开始快速帕累托分级");
            fx=new ArrayList<>();
            for (int j=0;j<F.get(i).size();j++){
                //实际在整个种群中的位置参数
                for (int m=0;m<F.get(i).get(j).Sp.size();m++){
                inidival h=F.get(i).get(j).Sp.get(m);
                h.nq=h.nq-1;
                if (h.nq==0){
                    h.rank=i+2;
                    fx.add(h);//新的一轮开始了
                }
            }
            }
            if(debug) System.out.println(i);
            F.add(fx);
            i++;
        }
    }
    private void paixu(inidival[] popx){
        //排序操作，对p1，p2进行排序
        p1=new inidival[popx.length];
        p2=new inidival[popx.length];
        for(int i=0;i<popx.length;i++) p1[i]=popx[i];
        for(int i=0;i<popx.length;i++) p2[i]=popx[i];
        //int ind2=0;
        if(debug) System.out.println("程序开始冒泡排序");
        boolean flag=true;
        for (int i=0;i<popx.length-1;i++){
            //利用冒泡算法进行的排序，
            // 如果要想提高速度可以修改为快速排序
            flag=true;
            for (int j=0;j<popx.length-i-1;j++){
                if (p1[j].fitness1 > p1[j+1].fitness1){
                    // 交换位置
                    inidival temp = p1[j];
                    p1[j] = p1[j+1];
                    p1[j+1] = temp;
                    //如果发生交换则flag改为false
                    flag = false;
                }
            }
            if (flag)   break;
        }
        BestY1=p1[p1.length-1].fitness1;
        MinY1=p1[0].fitness1;
        flag=true;
        for (int i=0;i<popx.length-1;i++){
            //利用冒泡算法进行的排序，
            // 如果要想提高速度可以修改为快速排序
            flag=true;
            for (int j=0;j<popx.length-i-1;j++){
                if (p2[j].fitness2 > p2[j+1].fitness2){
                    // 交换位置
                    inidival temp = p2[j];
                    p2[j] = p2[j+1];
                    p2[j+1] = temp;
                    //如果发生交换则flag改为false
                    flag = false;
                }
            }
            if (flag)   break;
        }
        BestY2=p2[p2.length-1].fitness2;
        MinY2=p2[0].fitness1;
    }
    private void calCrowing(inidival[] popx){
        //计算种群拥挤度的方法
        //计算种群拥挤度需要一个排序后的列表
        if(debug) System.out.println("程序开始计算拥挤度");
        paixu(popx);
        p1[N-1].distance=p1[0].distance=Integer.MAX_VALUE;
        p2[N-1].distance=p2[0].distance=Integer.MAX_VALUE;

        for(int i=1;i<popx.length-1;i++){
            popx[i].distance=0;
            paixu(popx);
            for(int j=0;j<2;j++){
                //计算拥挤度的部分
                if(j==0){
                    p1[i].distance=p1[i].distance+(p1[i+1].fitness1-p1[i-1].fitness1)/(BestY1-MinY1);//函数1的拥挤度计算
                }else{
                    p2[i].distance=p2[i].distance+(p2[i+1].fitness2+p2[i-1].fitness2)/(BestY2-MinY2);//函数2的拥挤度计算
                }
            }
        }
    }
    private void Crosser(){
        //这里是交叉操作，对两个基因组的基因分别进行交叉
        if(debug) System.out.println("程序开始交叉");
        boolean flag=false;
        for (int i=0;i<N-1;i++){
            if(Math.random()<pc){
                if (i==N-2) flag=true;
                int pos1=(int) (Math.random()*inidival.n1);//得到基因1发生交叉的位置
                //int pos2=(int)(Math.random()*inidival.n1);//得到基因发生交叉的位置
                if(debug){
                    System.out.println("position1:"+pos1);
                    //System.out.println("position2:"+pos2);
                }
                //截取字符串操作
                //if(debug) System.out.println(Ppop[i].bin1);
                String tmp1=Ppop[i].bin1.substring(pos1);
                //String tmp2=pop[i].bin2.substring(pos2);
                if(debug){
                    System.out.println("i"+i);
                }
                //对基因1的交叉操作
                if(debug) System.out.println(Ppop[i].bin1);
                if(debug) System.out.println(Ppop[i+1].bin1);
                Cpop[i].bin1=Ppop[i].bin1.substring(0,pos1)+Ppop[i+1].bin1.substring(pos1);
                Cpop[i+1].bin1=Ppop[i+1].bin1.substring(0,pos1)+tmp1;
            }else {
                Cpop[i]=Ppop[i];
            }
            if (flag==false) Cpop[N-1]=Ppop[N-1];
        }
    }
    private void Mutation(){
        //变异操作，分别对两个基因型固定位置的基因进行变异操作
        if(debug) System.out.println("程序开始变异");
        for(int i=0;i<N;i++){
            if(Math.random()<pm){
                int pos1=(int)(Math.random()*inidival.n1);//随机化一个位置
                StringBuilder strBuilder = new StringBuilder(Cpop[i].bin1);   //String 不可变,就是始终是一个对象，

                if(Cpop[i].bin1.charAt(pos1)=='0'){
                    strBuilder.setCharAt(pos1,'1');
                }
                else {
                    strBuilder.setCharAt(pos1,'0');
                }
                //具体的变异操作
                //具体的变异操作
                Cpop[i].bin1=strBuilder.toString();
                Cpop[i].calF();//重新计算个体的函数值
            }
        }
    }
    private void generate(){
        //更新子代种群，并且进行精英保留机制的迭代
        //精英保留机制原则，根据帕累托等级和距离进行排序，然后对其中的解进行精英选择，生成新的种群
        if(debug) System.out.println(("程序开始迭代"));
        fast_Nondominated_sort(Cpop);
        calCrowing(Cpop);
        inidival[] p=new inidival[2*N];//临时数组用来存放2n两个种群所有的个体
        for (int i=0;i<2*N;i++){
            //在这里先建立一个数组，将那些东西都存进去
            if (i<N){
                p[i]=Ppop[i];
            }else{
                p[i]=Cpop[i-N];
            }
        }
            fast_Nondominated_sort(p);//对p再次进行快速非支配排序
            calCrowing(p);//计算p的拥挤度
            //在这里使用冒泡排序，将总的数组优中选优
            for (int i=0;i<p.length-1;i++){
                //利用冒泡算法进行的排序，
                //如果要想提高速度可以修改为快速排序
                for (int j=0;j<p.length-i-1;j++){
                    if (p[j].rank > p[j+1].rank){
                        // 交换位置,等级越高的越靠后面
                        inidival temp = p[j];
                        p[j] = p[j+1];
                        p[j+1] = temp;
                    }else if ((p[j].rank==p[j+1].rank)&&(p[j].distance<p[j+1].distance)){
                        //距离越小的越靠后
                        inidival temp = p[j];
                        p[j] = p[j+1];
                        p[j+1] = temp;
                    }
                }
            }
            for (int i=0;i<N;i++){
                pop[i]=p[i];
            }
            Cpop=new inidival[N];
            Ppop=new inidival[N];
        }
    public void FindBest(){
        if(debug) System.out.println(("程序开始寻找最优解"));
        System.out.println(("程序开始寻找最优帕累托解"));
        for (int i=0;i<N;i++){
            if (pop[i].rank==1){
                System.out.println("最优函数1："+pop[i].fitness1+"最优函数2："+pop[i].fitness2);
            }

        }

    }
    public void run(){
        this.init();
        this.fast_Nondominated_sort(pop);//快速非支配排序
        this.calCrowing(pop);//拥挤度计算
        for (int i=0;i<round;i++){
            //开始迭代
            choose();//选择
            Crosser();//交叉
            Mutation();//变异
            generate();//迭代
            FindBest();

        }

    }

    public static void main(String args[]){
        populations p=new populations();
        if(debug) System.out.println("程序开始运行");
        p.run();
        if(debug) System.out.println("程序结束运行");
    }
}
