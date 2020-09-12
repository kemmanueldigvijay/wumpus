/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

/**
 *
 * @author K Em DigVijay
 */
public class QalgoTable 
{
    private static double[][] Q = null;
 
    public static double[][] setInstance()
    {
        if(Q == null){
            Q = new double[45][5];
            int x=1;
            

            
            while( x<=4){
                int y=1;
                
                while(y <= 4){
                    int r=0;
                    
                    while(r <=4)
                    {
                        
                        int p = x * 10 + y;
                        
                        Q[p][r] = 0;
                        r=r+1;
                        
                    }
                    y=y+1;
                }
                x=x+1; 
            }
        }
        
        return Q;

    }
    
    public static int getMaxQValueAction(int position)
    {
        
        int bestAction = 1;
        int i=1;
        while(i<=4){
            if(Q[position][bestAction] < Q[position][i]){
                bestAction = i;
            }
            i=i+1;
        }
        return bestAction;
    }
    
    public static double getMaxQValue(int position)
    {
        
        double bestValue = Q[position][1];
        int i=1;
        while(i<=4){
            if(bestValue < Q[position][i]){
                bestValue = Q[position][i];
            }
            i=i+1;
        }
        return bestValue;
    }
    
    public static void printQTable()
    {
        int x=1;
        while(x <= 4){ 
            int y=1;
                while(y <= 4){   
                    
                    System.out.println(x * 10 + y +  " \nAction: ");
                    int r=0;
                    while(r <= 4){                     
                        int pos = x * 10 + y;
                        
                        if(r == 0){
                            System.out.println("Reward: " + Q[pos][r]);
                        }
                        System.out.println(Q[pos][r]);
                        r=r+1;                      
                    }
                    y=y+1;                  
                }             
                x=x+1;  
        }
    }
}
