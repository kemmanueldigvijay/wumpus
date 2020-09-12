package wumpusworld;
import java.util.Random;

/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author K Em DigVijay
 */
public class MyAgent implements Agent
{
    private World w;
    private double[][] Q = QalgoTable.setInstance();
    int rnd;
    int currentPosition;
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;
        currentPosition = 11;   
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */
    public void trainAgent() 
    {       
        int oldPosition = currentPosition;
        int action = selectAction();
        trainAction(action);
        int newPosition = currentPosition;
        double reward = Q[oldPosition][0];
        double maxQValueOfNextState = QalgoTable.getMaxQValue(newPosition);
        Q[oldPosition][action] = 
                Q[oldPosition][action] + QalgoConfigure.LEARNINGRATE * (reward + (QalgoConfigure.DISCOUNTFACTOR * maxQValueOfNextState) - Q[oldPosition][action]);
    }
    public void doAction()
    {
        int action = QalgoTable.getMaxQValueAction(currentPosition);

        trainAction(action);

        int cX = w.getPlayerX();
        int cY = w.getPlayerY();

        ////Grab Gold if we can.
        if (w.hasGlitter(cX, cY)) {
            System.out.println("Glitter");
            w.doAction(World.A_GRAB); 
            return;
        } 
        
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }

        if (w.hasBreeze(cX, cY))
        {
            System.out.println("I am in a Breeze");
        }
        if (w.hasStench(cX, cY))
        {
            System.out.println("I am in a Stench");
        }  
        if (w.hasPit(cX, cY)) {
            System.out.println("I am in a Pit");  
        }    
        if (w.hasWumpus(cX, cY)) {
            System.out.println("Wumpus");
        } 
    }
    
     /**
     * Asks your solver agent to select an action.
     */
    public int selectAction() {
        Random rand = new Random();
        if (Math.random() < QalgoConfigure.PROBABILITY) {
            return rand.nextInt(4) + 1;
        } else {
            return QalgoTable.getMaxQValueAction(currentPosition);
        }
    }
    
    /**
     * Asks your solver agent to train the selected action.
     */
    public void trainAction(int action) {
        checkState();
        
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
               

        // Right
        if (action == 1) {
            if (w.isValidPosition(cX + 1, cY)) {
                currentPosition = (cX + 1) * 10 + cY;
                switch (w.getDirection()) {
                    case World.DIR_RIGHT:
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_DOWN:
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_LEFT:
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_UP:
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        break;
                    default:
                        break;
                }
            }
        }
        // Up
        if (action == 2) {
            if (w.isValidPosition(cX, cY - 1)) {
                switch (w.getDirection()) {
                    case World.DIR_RIGHT:
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_DOWN:
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_LEFT:
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_UP:
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        break;
                    default:
                        break;
                }
                currentPosition = cX * 10 + (cY - 1);
            } 
        }
        // Left
        if (action == 3) {
            if (w.isValidPosition(cX - 1, cY)) {
                switch (w.getDirection()) {
                    case World.DIR_RIGHT:
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_DOWN:
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_LEFT:
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_UP:
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_MOVE);
                        break;
                    default:
                        break;
                }
                currentPosition = (cX - 1) * 10 + cY;
            } 
        }
        // Down
        if (action == 4) {
            if (w.isValidPosition(cX, cY + 1)) {
                switch (w.getDirection()) {
                    case World.DIR_RIGHT:
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_DOWN:
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_LEFT:
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                        break;
                    case World.DIR_UP:
                        w.doAction(World.A_MOVE);
                        break;
                    default:
                        break;
                }
                currentPosition = cX * 10 + (cY + 1);
            }
        }
    }
    
    // Used to check if the current square has either a wumpus, pit or gold in it.
    public void checkState() 
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        if (w.gameOver()) {
            if (w.hasWumpus(x, y)) {
                // Set the Reward for the currentPos 
                Q[currentPosition][0] = QalgoConfigure.WUMPUSREWARD;
            }
        } else if (w.hasGlitter(x, y)) {
            // Set the Reward for the currentPos 
            Q[currentPosition][0] = QalgoConfigure.GOLDREWARD;
            w.doAction(World.A_GRAB);
        } else if (w.hasPit(x, y)) {
            // Set the Reward for the currentPos 
            Q[currentPosition][0] = QalgoConfigure.PITREWARD;
            w.doAction(World.A_CLIMB);
        }
        
    }
}



   // public void doAction()
   //  {
   //      //Location of the player
   //      int cX = w.getPlayerX();
   //      int cY = w.getPlayerY();
        
        
   //      //Basic action:
   //      //Grab Gold if we can.
   //      if (w.hasGlitter(cX, cY))
   //      {
   //          w.doAction(World.A_GRAB);
   //          return;
   //      }
        
   //      //Basic action:
   //      //We are in a pit. Climb up.
   //      if (w.isInPit())
   //      {
   //          w.doAction(World.A_CLIMB);
   //          return;
   //      }
        
   //      //Test the environment
   //      if (w.hasBreeze(cX, cY))
   //      {
   //          System.out.println("I am in a Breeze");
   //      }
   //      if (w.hasStench(cX, cY))
   //      {
   //          System.out.println("I am in a Stench");
   //      }
   //      if (w.hasPit(cX, cY))
   //      {
   //          System.out.println("I am in a Pit");
   //      }
   //      if (w.getDirection() == World.DIR_RIGHT)
   //      {
   //          System.out.println("I am facing Right");
   //      }
   //      if (w.getDirection() == World.DIR_LEFT)
   //      {
   //          System.out.println("I am facing Left");
   //      }
   //      if (w.getDirection() == World.DIR_UP)
   //      {
   //          System.out.println("I am facing Up");
   //      }
   //      if (w.getDirection() == World.DIR_DOWN)
   //      {
   //          System.out.println("I am facing Down");
   //      }
        
   //      //decide next move
   //      rnd = decideRandomMove();
   //      if (rnd==0)
   //      {
   //          w.doAction(World.A_TURN_LEFT);
   //          w.doAction(World.A_MOVE);
   //      }
        
   //      if (rnd==1)
   //      {
   //          w.doAction(World.A_MOVE);
   //      }
                
   //      if (rnd==2)
   //      {
   //          w.doAction(World.A_TURN_LEFT);
   //          w.doAction(World.A_TURN_LEFT);
   //          w.doAction(World.A_MOVE);
   //      }
                        
   //      if (rnd==3)
   //      {
   //          w.doAction(World.A_TURN_RIGHT);
   //          w.doAction(World.A_MOVE);
   //      }
                
   //  }    
    
     /**
     * Genertes a random instruction for the Agent.
     */
//     public int decideRandomMove()
//     {
//       return (int)(Math.random() * 4);
//     }
    
    
// }

